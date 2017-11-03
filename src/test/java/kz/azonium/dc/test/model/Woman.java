package kz.azonium.dc.test.model;

import java.util.Optional;

public class Woman extends Person {

    public static final Woman eva = new Woman("Eva", 0, Color.WHITE);

    static {
        eva.age = 1 << 30;
    }

    public Woman(String name, int age, Color favoriteColor) {
        super(name, age, favoriteColor);
    }

    public Man getHusband() {
        return Optional.ofNullable(getFamily()).map(f -> f.getHusband()).orElse(null);
    }
}
