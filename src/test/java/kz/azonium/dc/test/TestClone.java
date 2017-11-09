package kz.azonium.dc.test;

import kz.azonium.dc.DeepCloneException;
import kz.azonium.dc.DeepCloneUtil;
import kz.azonium.dc.test.model.*;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestClone {

    @Test
    public void testClonePerson() throws DeepCloneException {
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

    @Test
    public void testRecursive() throws DeepCloneException {
        RecursiveModel main = new RecursiveModel(UUID.randomUUID().toString());
        for (int i = 0; i < 10; i++) {
            main.getChildren().add(new RecursiveModel(UUID.randomUUID().toString()));
        }
        main.getChildren().forEach(t -> t.getChildren().addAll(main.getChildren()));
        main.getChildren().add(new Random(new Date().getTime()).nextInt(main.getChildren().size()), main);

        List<RecursiveModel[]> tested = new LinkedList<>();
        RecursiveModel clone = DeepCloneUtil.clone(main);
        testClonedRecursiveModel(tested, main, clone);

        System.out.println(String.format("Main Id: %s", main.getId()));
        for (int i = 0; i < main.getChildren().size(); i++) {
            System.out.println(String.format("Children[%d] Id: %s", i, main.getChildren().get(i).getId()));
        }
    }

    private static void testClonedRecursiveModel(List<RecursiveModel[]> tested, RecursiveModel main, RecursiveModel clone) {
        if (main == null && clone == null) return;
        assert main != null && clone != null;
        if (tested.stream().anyMatch(t -> t[0] == main && t[1] == clone)) return;
        assert main != clone;
        assert (main.getId().equals(clone.getId()));
        tested.add(new RecursiveModel[] {main, clone});

        if (main.getChildren() == null && clone.getChildren() == null) return;
        assert main.getChildren() != null && clone.getChildren() != null;
        assert main.getChildren() != clone.getChildren();
        assert main.getChildren().size() == clone.getChildren().size();
        for (int i = 0; i < main.getChildren().size(); i++) {
            testClonedRecursiveModel(tested, main.getChildren().get(i), clone.getChildren().get(i));
        }
    }
}
