package ru.mihaly4.vkmd.model;

public class Link {
    private String url;
    private String name;

    public Link(String url, String name) {
        this.url = url;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
