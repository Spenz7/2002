package models.enums;

public enum FlatType {
    TWO_ROOM("2-room"),
    THREE_ROOM("3-room");

    private final String displayName;

    FlatType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Method to get FlatType from string (case-insensitive)
    public static FlatType fromString(String flatType) {
        for (FlatType type : FlatType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(flatType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid flat type: " + flatType);
    }
}
