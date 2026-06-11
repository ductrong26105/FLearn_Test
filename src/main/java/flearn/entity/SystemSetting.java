package flearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "[SystemSettings]")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[SettingID]")
    private Integer id;

    @Column(name = "[SettingKey]", nullable = false, unique = true, length = 100)
    private String settingKey;

    @Column(name = "[SettingValue]", nullable = false, length = 500)
    private String settingValue;
}
