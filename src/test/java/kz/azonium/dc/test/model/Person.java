package kz.azonium.dc.test.model;

public abstract class Person {

    public static final int MAX_AGE = 120;
    public static final int MIN_AGE = 0;

    public static Person youngestPerson;

    private Family family;
    private Family parentFamily;
    private final String name;
    protected int age;
    private Color favoriteColor;

    public Person(String name, int age, Color color) {
        this.name = name;
        setAge(age);
        setFavoriteColor(color);
        if (youngestPerson == null || youngestPerson.age > age) {
            youngestPerson = this;
        }
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Family getParentFamily() {
        return parentFamily;
    }

    public void setParentFamily(Family parentFamily) {
        this.parentFamily = parentFamily;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = Math.max(Math.min(age, MAX_AGE), MIN_AGE);
    }

    public Color getFavoriteColor() {
        return favoriteColor;
    }

    public void setFavoriteColor(Color favoriteColor) {
        this.favoriteColor = favoriteColor;
    }
}
