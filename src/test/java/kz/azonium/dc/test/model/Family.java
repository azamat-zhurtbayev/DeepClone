package kz.azonium.dc.test.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Family {

    public static final Family firstPeople = new Family();

    static {
        firstPeople.setHusband(Man.adam);
        firstPeople.setWife(Woman.eva);
        firstPeople.setChildren(Stream.of(Man.kain, Man.avel).collect(Collectors.toList()));
    }

    private Man husband;
    private Woman wife;
    private List<Person> children;

    public Man getHusband() {
        return husband;
    }

    public void setHusband(Man husband) {
        this.husband = husband;
        this.husband.setFamily(this);
    }

    public Woman getWife() {
        return wife;
    }

    public void setWife(Woman wife) {
        this.wife = wife;
        this.wife.setFamily(this);
    }

    public List<Person> getChildren() {
        return children;
    }

    public void setChildren(List<Person> children) {
        this.children = children;
        children.stream()
                .forEach(
                        c -> c.setParentFamily(Family.this)
                );
    }
}
