package it.polimi.ingsw.server;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

/**
 * Abstract class for Collective Objective cards
 *
 * @author Federica
 */
public abstract class CollectiveObjectiveCard {
    public abstract boolean checkObjective(Shelf shelf);

    public static CollectiveObjectiveCard getRandomCard(CollectiveObjectiveCard other) {
        List<Class<? extends CollectiveObjectiveCard>> subclasses = getAllPossibleCards();

        Random random = new Random();

        int index = random.nextInt(subclasses.size());

        Class<? extends CollectiveObjectiveCard> subclass = subclasses.get(index);

        while (other.equals(subclass)) {
            index = random.nextInt(subclasses.size());
            subclass = subclasses.get(index);
        }

        try {
            return subclass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Class<? extends CollectiveObjectiveCard>> getAllPossibleCards() {
        List<Class<? extends CollectiveObjectiveCard>> subclasses = new ArrayList<>();

        Reflections reflections = new Reflections("it.polimi.ingsw.server");

        Set<Class<? extends CollectiveObjectiveCard>> allClasses = reflections.getSubTypesOf(CollectiveObjectiveCard.class);

        for (Class<? extends CollectiveObjectiveCard> clazz : allClasses) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                subclasses.add(clazz);
            }
        }
        return subclasses;
    }
}
