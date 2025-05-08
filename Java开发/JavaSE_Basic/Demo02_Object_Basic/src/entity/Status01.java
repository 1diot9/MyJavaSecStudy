package entity;

public enum Status01 {
    Running("跑步"),Sleeping("睡觉");

    private final String name;

    private Status01(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
