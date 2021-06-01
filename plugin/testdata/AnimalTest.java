package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {

    //contrived example intentionally made large to test scrolling functionality
    @BeforeAll
    public static void before() throws Exception {
        return;
    }

    @AfterAll
    public static void after() throws Exception {
        return;
    }

    @org.junit.jupiter.api.Test
    void AnimalSoundTest() {
        Animal cat = new Animal("cat");
        Animal dog = new Animal("dog");
        assertEquals(cat.getSound(), "meow");
    }

    @org.junit.jupiter.api.Test
    void setSoundTest() {
        Animal dog = new Animal("dog");
        dog.setSound("meow");
        assertEquals("meow", dog.getSound());
    }

    @Test
    void overloadedGetFeetTest() {
        Animal chicken = new Animal("chicken",2);
        assertEquals(chicken.getFeet(), 2);
    }


    @Test
    void overloadedGetFeetTestHorse() {
        Animal horse = new Animal("horse",2);
        horse.setFeet(4);
        assertEquals(horse.getFeet(), 4);
    }

    @Test
    void overloadedGetFeetTestCow() {
        Animal cow = new Animal("cow",2);
        horse.setFeet(4);
        assertEquals(horse.getFeet(), 4);
    }

    @Test
    void setSoundTest() {
        Animal cat = new Animal("cat");
        cat.setSound("woof");
        assertEquals("woof", cat.getSound());
    }

    void notATest() {
        return;
    }

    @Test
    void simple() {
        assertTrue(true);
    }


}