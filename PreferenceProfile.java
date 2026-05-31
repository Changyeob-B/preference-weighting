package custom;

public enum PreferenceProfile {
    NONE("none"),
    META("meta"),
    PATH("path"),

    private final String key;

    PreferenceProfile(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
