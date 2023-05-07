package it.polimi.ingsw.server.controller;

import org.junit.Test;

import java.util.regex.Pattern;

public class ViewCLITest {
    @Test
    public void viewSubstitutionTest() {
        String twoPlayerView = """
            You (?)         | Board             | Your objective:
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            * * * * *       | ^ ^ ^ ^ ^ ^ ^ ^ ^ | $ $ $ $ $
            --------------- | ^ ^ ^ ^ ^ ^ ^ ^ ^ | ---------------
            ¥2       (?)    | ^ ^ ^ ^ ^ ^ ^ ^ ^ |
            @ @ @ @ @       | ^ ^ ^ ^ ^ ^ ^ ^ ^ |
            @ @ @ @ @       | ----------------- |
            @ @ @ @ @       |                   |
            @ @ @ @ @       |                   |
            @ @ @ @ @       |                   |
            @ @ @ @ @       |                   |
            -----------------------------------------------------
            Objective I (&1): £1
            Objective II (&2): £2
        """;
        System.out.println(twoPlayerView);
        String userShelf = "eeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
        String playerTwoShelf = "222222222222222222222222222222";
        String view = twoPlayerView;
        for(int i = 0; i < 81; i++)
            view = Pattern.compile("\\^").matcher(view).replaceFirst("b");
        for(int i = 0; i < 30; i++) {
            view = Pattern.compile("\\*").matcher(view).replaceFirst(userShelf.substring(i, i + 1));
            view = Pattern.compile("@").matcher(view).replaceFirst(playerTwoShelf.substring(i, i + 1));
        }
        System.out.println(view);
    }
}
