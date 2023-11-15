package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Тел.", false),
    MOBILE("Мобильный", false),
    HOME_PHONE("Домашний тел.", false),
    SKYPE("Skype", false) {
        @Override
        public String toHtml0(String value) {
            return getTitle() + ": " + toLink("skype:" + value, value);
        }
    },
    MAIL("Почта", false) {
        @Override
        public String toHtml0(String value) {
            return getTitle() + ": " + toLink("mailto:" + value, value);
        }
    },
    LINKEDIN("Профиль LinkedIn", true) {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    },
    GITHUB("Профиль GitHub", true) {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    },
    STACKOVERFLOW("Профиль Stackoverflow", true) {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    },
    HOMEPAGE("Домашняя страница", true) {
        @Override
        public String toHtml0(String value) {
            return toLink(value);
        }
    };

    private final String title;
    private boolean isWebsite;

    ContactType(String title, boolean isWebsite) {
        this.title = title;
        this.isWebsite = isWebsite;
    }

    public String getTitle() {
        return title;
    }

    protected String toHtml0(String value) {
        return title + ": " + value;
    }

    public String toHtml(String value) {
        return (value == null) ? "" : toHtml0(value);
    }

    public String toLink(String href) {
        return toLink(href, title);
    }

    public static String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }

    public boolean isWebsite() {
        return isWebsite;
    }
}