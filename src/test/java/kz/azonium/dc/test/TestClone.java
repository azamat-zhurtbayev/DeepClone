package kz.azonium.dc.test;

import kz.azonium.dc.DeepCloneUtil;
import kz.azonium.dc.test.model.*;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestClone {

    @Test
    public void testClonePerson() throws InstantiationException, IllegalAccessException {
        Family family = new Family();
        family.setHusband(new Man("Alex", 30, Color.BLACK));
        family.setWife(new Woman("Ann", 28, Color.BLUE));
        family.setChildren(
                Stream.of(
                        new Man("Max", 8, Color.ORANGE),
                        new Woman("Melissa", 5, Color.ORANGE),
                        new Man("Victor", 1, Color.GREEN)
                ).collect(Collectors.toList())
        );


        Family familyHusband = new Family();
        familyHusband.setHusband(new Man("Mike", 53, Color.YELLOW));
        familyHusband.setWife(new Woman("Alice", 52, Color.RED));
        familyHusband.setChildren(
                Stream.of(
                        new Man("Bob", 35, Color.WHITE),
                        family.getHusband(),
                        new Woman("Chloe", 27, Color.BLACK)
                ).collect(Collectors.toList())
        );

        Family familyWife = new Family();
        familyWife.setHusband(new Man("George", 50, Color.GREEN));
        familyWife.setChildren(
                Stream.of(
                        family.getWife()
                ).collect(Collectors.toList())
        );

        Person youngest = Person.youngestPerson;
        Family clonedFamily = DeepCloneUtil.clone(family);

        testClonedFamily(clonedFamily, family);
        assert clonedFamily.getHusband().getParentFamily().getChildren().contains(clonedFamily.getHusband());
        assert clonedFamily.getWife().getParentFamily().getChildren().contains(clonedFamily.getWife());
        for (Person child : clonedFamily.getChildren()) {
            assert child.getParentFamily() == clonedFamily;
        }
        assert youngest == Person.youngestPerson;
    }

    private static void testClonedFamily(Family clonedFamily, Family family) {
        if (clonedFamily == null && family == null) return;

        assert clonedFamily != null && family != null;
        assert clonedFamily != family;

        testClonedPerson(clonedFamily.getHusband(), family.getHusband());
        testClonedPerson(clonedFamily.getWife(), family.getWife());

        if (family.getHusband() != null) {
            testClonedFamily(clonedFamily.getHusband().getParentFamily(), family.getHusband().getParentFamily());
        }
        if (family.getWife() != null) {
            testClonedFamily(clonedFamily.getWife().getParentFamily(), family.getWife().getParentFamily());
        }

        if (clonedFamily.getChildren() != null || family.getChildren() != null) {
            assert clonedFamily.getChildren() != null && family.getChildren() != null;
            assert clonedFamily.getChildren() != family.getChildren();
            assert clonedFamily.getChildren().size() == family.getChildren().size();
            int size = clonedFamily.getChildren().size();
            for (int i = 0; i < size; i++) {
                testClonedPerson(clonedFamily.getChildren().get(i), family.getChildren().get(i));
            }
        }
    }

    private static void testClonedPerson(Person clonedPerson, Person person) {
        if (person == null && clonedPerson == null) return;

        assert clonedPerson != null && person != null;
        assert clonedPerson != person;
        assert clonedPerson.getName() != null;
        assert clonedPerson.getName().equals(person.getName());
        assert clonedPerson.getAge() == person.getAge();
        assert clonedPerson.getFavoriteColor() == person.getFavoriteColor();
    }
}
