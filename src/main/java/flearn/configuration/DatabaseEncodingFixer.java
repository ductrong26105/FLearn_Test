package flearn.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Chạy khi ứng dụng khởi động để chuyển đổi các cột VARCHAR -> NVARCHAR
 * trong SQL Server nhằm hỗ trợ lưu trữ tiếng Việt Unicode.
 *
 * Chỉ convert các cột KHÔNG có UNIQUE INDEX vì:
 *   - Các cột unique (username, email, inviteCode, courseCode) là mã định danh —
 *     không cần lưu tiếng Việt có dấu.
 *   - SQL Server không cho phép ALTER COLUMN trực tiếp trên cột có unique index
 *     (phải drop index trước → nguy cơ mất constraint).
 *
 * Phía ghi Unicode đã được xử lý bởi sendStringParametersAsUnicode=true trong JDBC URL.
 * Hibernate đọc NVARCHAR columns bằng getString() hoàn toàn bình thường.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseEncodingFixer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=== DatabaseEncodingFixer: Converting VARCHAR -> NVARCHAR for non-indexed columns ===");

        // Chỉ convert cột VARCHAR/CHAR/TEXT mà KHÔNG có unique index hoặc primary key
        // Các cột có unique index (username, email, inviteCode, courseCode) sẽ được bỏ qua
        // vì chúng không cần lưu tiếng Việt và không thể ALTER trực tiếp.
        String sql = """
            DECLARE @tableName  NVARCHAR(256);
            DECLARE @columnName NVARCHAR(256);
            DECLARE @dataType   NVARCHAR(256);
            DECLARE @maxLength  INT;
            DECLARE @isNullable BIT;
            DECLARE @sql        NVARCHAR(MAX);
            DECLARE @newType    NVARCHAR(256);
            DECLARE @converted  INT = 0;
            DECLARE @skipped    INT = 0;

            DECLARE col_cursor CURSOR LOCAL FAST_FORWARD FOR
            SELECT
                t.name        AS TableName,
                c.name        AS ColumnName,
                ty.name       AS TypeName,
                c.max_length  AS MaxLength,
                c.is_nullable AS IsNullable
            FROM sys.columns  c
            JOIN sys.tables   t  ON c.object_id    = t.object_id
            JOIN sys.types    ty ON c.user_type_id = ty.user_type_id
            WHERE t.is_ms_shipped = 0
              AND ty.name IN ('varchar', 'char', 'text')
              -- Bỏ qua cột là PRIMARY KEY
              AND c.column_id NOT IN (
                  SELECT ic.column_id
                  FROM sys.index_columns ic
                  JOIN sys.indexes i ON ic.object_id = i.object_id AND ic.index_id = i.index_id
                  WHERE i.object_id = c.object_id AND i.is_primary_key = 1
              )
              -- Bỏ qua cột có UNIQUE INDEX
              AND NOT EXISTS (
                  SELECT 1
                  FROM sys.index_columns ic
                  JOIN sys.indexes i ON ic.object_id = i.object_id AND ic.index_id = i.index_id
                  WHERE ic.object_id = c.object_id
                    AND ic.column_id = c.column_id
                    AND i.is_unique = 1
                    AND i.is_primary_key = 0
              )
            ORDER BY t.name, c.name;

            OPEN col_cursor;
            FETCH NEXT FROM col_cursor INTO @tableName, @columnName, @dataType, @maxLength, @isNullable;

            WHILE @@FETCH_STATUS = 0
            BEGIN
                SET @newType = CASE
                    WHEN @dataType IN ('varchar', 'char') AND @maxLength = -1 THEN 'NVARCHAR(MAX)'
                    WHEN @dataType IN ('varchar', 'char')                     THEN 'NVARCHAR(' + CAST(@maxLength AS NVARCHAR(10)) + ')'
                    WHEN @dataType = 'text'                                   THEN 'NVARCHAR(MAX)'
                    ELSE NULL
                END;

                IF @newType IS NOT NULL
                BEGIN
                    SET @sql = 'ALTER TABLE [' + @tableName + '] ALTER COLUMN ['
                               + @columnName + '] ' + @newType + ' '
                               + CASE WHEN @isNullable = 1 THEN 'NULL' ELSE 'NOT NULL' END + ';';
                    BEGIN TRY
                        EXEC sp_executesql @sql;
                        SET @converted = @converted + 1;
                    END TRY
                    BEGIN CATCH
                        SET @skipped = @skipped + 1;
                    END CATCH
                END

                FETCH NEXT FROM col_cursor INTO @tableName, @columnName, @dataType, @maxLength, @isNullable;
            END

            CLOSE col_cursor;
            DEALLOCATE col_cursor;

            PRINT 'DatabaseEncodingFixer: converted=' + CAST(@converted AS NVARCHAR) + ', skipped=' + CAST(@skipped AS NVARCHAR);
            """;

        try {
            jdbcTemplate.execute(sql);
            log.info("=== DatabaseEncodingFixer: Completed. Non-indexed VARCHAR columns converted to NVARCHAR. ===");
        } catch (Exception e) {
            log.error("=== DatabaseEncodingFixer: FAILED — {}", e.getMessage(), e);
        }
    }
}
