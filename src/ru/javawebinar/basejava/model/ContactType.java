package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Тел.", false),
    SKYPE("Skype", false),
    MAIL("Почта", false),
    LINKEDIN("Профиль LinkedIn", true),
    GITHUB("Профиль GitHub", true),
    STACKOVERFLOW("Профиль StackOverflow", true),
    HOMEPAGE("Домашняя страница", true);

    private String title;
    private boolean isWebsite;

    ContactType(String title, boolean isWebsite) {
        this.title = title;
        this.isWebsite = isWebsite;
    }

    public String getTitle() {
        return title;
    }
    public boolean isWebsite() {
        return isWebsite;
    }
}
