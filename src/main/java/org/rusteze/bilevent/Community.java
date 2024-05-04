package org.rusteze.bilevent;

public class Community {

    private String name;

    public Community(String name) {
        this.name = name;
    }
    public boolean isAdmin(User user) {
        return false;
    }
    public String getName() {
        return name;
    }
}
