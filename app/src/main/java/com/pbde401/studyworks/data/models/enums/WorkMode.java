package com.pbde401.studyworks.data.models.enums;

public enum WorkMode {
    REMOTE("Remote"),
    ON_SITE("On-site"),
    HYBRID("Hybrid");

    private final String value;

    WorkMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static WorkMode fromString(String text) {
        for (WorkMode mode : WorkMode.values()) {
            if (mode.value.equalsIgnoreCase(text)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
