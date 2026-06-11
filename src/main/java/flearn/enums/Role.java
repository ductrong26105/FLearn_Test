package flearn.enums;

public enum Role {
    ADMIN(0),
    TEACHER(1),
    STUDENT(2);

    private final int code;

    Role(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Role fromCode(Integer code) {
        if (code == null) {
            return STUDENT;
        }
        return switch (code) {
            case 0 -> ADMIN;
            case 1 -> TEACHER;
            default -> STUDENT;
        };
    }
}
