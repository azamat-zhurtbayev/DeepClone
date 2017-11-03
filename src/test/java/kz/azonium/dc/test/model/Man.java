package kz.azonium.dc.test.model;

import java.util.Optional;

public class Man extends Person {

    public static final Man adam = new Man("Adam", 0, Color.ORANGE);
    public static final Man avel = new Man("Avel", 0, Color.GREEN);
    public static final Man kain = new Man("Kain", 0, Color.BLACK);

    static {
        adam.age = 1 << 30;
        avel.age = 1 << 30 - 20;
        kain.age = 1 << 30 - 18;
    }

    public Man(String name, int age, Color favoriteColor) {
        super(name, age, favoriteColor);
    }

    public Woman getWife() {
        return Optional.ofNullable(getFamily()).map(f -> f.getWife()).orElse(null);
    }

}
