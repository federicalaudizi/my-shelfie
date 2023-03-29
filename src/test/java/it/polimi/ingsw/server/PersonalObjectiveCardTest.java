package it.polimi.ingsw.server;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class PersonalObjectiveCardTest {

    @Test
    public void toStringTest(){
        PersonalObjectiveCard card = new PersonalObjectiveCard();
        System.out.println(card.toString());
    }
}