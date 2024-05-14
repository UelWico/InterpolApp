package com.example.oop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

import com.example.oop.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class FilterCaseTest {
    MainActivity activity = Robolectric.buildActivity(MainActivity.class).create().get();
    Card[] cards;

    @Before
    public void setUp() throws IOException {
        cards = activity.readFromJson(R.raw.man);
    }

    @Test
    public void cards_areSame() throws IOException {

        Card[] cardTest, cardRight;
        cardTest = activity.readFromJson(R.raw.test);
        cardRight = new Card[]{new Card(1, "KRISTINA", "YAKUNINA", 34, "female",
                "Kazakhstan", "Kazakhstan", "Colour of eyes \tDark",
                "Language(s) spoken \tEnglish, Russian", "co-participation in murder, robbery, extortion and theft or damage of documents",
                "yakunina", false), new Card(2, "ADAM", "BURSAGOV", 33,
                "male", "Kazakhstan", "Russia", "", "Language(s) spoken \tRussian",
                "Participation in an illegal armed formation", "bursagov", false), new Card(3, "SERGEY", "LINNIK",
                36, "male", "Kazakhstan", "United States", "Height \t1.73 metres\nWeight \t78 kilograms\nColour of hair \tBrown\nColour of eyes \tBlue",
                "Language(s) spoken \tRussian, English", "1) Robbery in the first degree 2) Robbery in the second degree 3) Robbery in the second degree causing physical injury 4) Assault in the second degree 5) Criminal possession of a weapon in the fourth degree",
                "linnik", false)};
        for (int i = 0; i < cardTest.length; i++) {
            assertEquals(cardRight[i].id, cardTest[i].id);
            assertEquals(cardRight[i].name, cardTest[i].name);
            assertEquals(cardRight[i].lastName, cardTest[i].lastName);
            assertEquals(cardRight[i].age, cardTest[i].age);
            assertEquals(cardRight[i].gender, cardTest[i].gender);
            assertEquals(cardRight[i].nationality, cardTest[i].nationality);
            assertEquals(cardRight[i].wantedBy, cardTest[i].wantedBy);
            assertEquals(cardRight[i].physicalDescription, cardTest[i].physicalDescription);
            assertEquals(cardRight[i].details, cardTest[i].details);
            assertEquals(cardRight[i].charges, cardTest[i].charges);
            assertEquals(cardRight[i].photoPath, cardTest[i].photoPath);
            assertEquals(cardRight[i].archived, cardTest[i].archived);
        }

    }

    @Test
    public void test_genderFilter() {
        String gender = "female";
        Card[] cardsFiltered = activity.getByGender(gender, cards);
        for (int i = 0; i < cardsFiltered.length; i++) {
            assertEquals(gender, cardsFiltered[i].gender);
        }
    }

    @Test
    public void test_nationalityFilter() {
        String nationality = "Kazakhstan";
        Card[] cardsFiltered = activity.getByNationality(nationality, cards);
        for (int i = 0; i < cardsFiltered.length; i++) {
            assertEquals(nationality, cardsFiltered[i].nationality);
        }
    }

    @Test
    public void test_ageFilter() {
        int minAge = 10;
        int maxAge = 70;
        Card[] cardsFiltered = activity.getByAge(minAge, maxAge, cards);
        for (int i = 0; i < cardsFiltered.length; i++) {
            assertTrue(cardsFiltered[i].age >= minAge && cardsFiltered[i].age <= maxAge);
        }
    }

    @Test
    public void test_WBFilter() {
        String WB = "Kazakhstan";
        Card[] cardsFiltered = activity.getByWB(WB, cards);
        for (int i = 0; i < cardsFiltered.length; i++) {
            assertEquals(WB, cardsFiltered[i].wantedBy);
        }
    }

    @Test
    public void test_archivedFilter() {
        boolean arch = true;
        Card[] cardsFiltered = activity.getByArchived(arch, cards);
        for (int i = 0; i < cardsFiltered.length; i++) {
            assertEquals(arch, cardsFiltered[i].archived);
        }
    }

    @Test
    public void test_listOfN() {
        String[] natRight = {"", "Gallifrey", "Kazakhstan", "Poland", "Russia", "Netherlands"};
        String[] natTest = activity.listOfN();
        assertArrayEquals(natRight, natTest);
    }

    @Test
    public void test_listOfWB() {
        String[] wbRight = {"", "Universe", "Kazakhstan", "Russia", "United States", "Poland", "Netherlands"};
        String[] wbTest = activity.listOfWB();
        assertArrayEquals(wbRight, wbTest);
    }
}