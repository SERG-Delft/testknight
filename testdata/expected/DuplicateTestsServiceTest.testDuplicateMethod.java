package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    @BeforeAll
    public static void before() throws Exception {
        return;
    }

    @AfterAll
    public static void after() throws Exception {
        return;
    }

    @Test
    void translateTest() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(1, 2);
        p1.translate(1, 2);
        assertEquals(p1, p2);
    }

    @Test
    void translateTest() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(1, 2);
        p1.translate(1, 2);
        assertEquals(p1, p2);
    }


    @Test
    void setXTest() {
        Point p = new Point(0, 0);
        p.setX(5);
        assertEquals(5, p.getX());
    }

    @Test
    void setYTest() {
        Point p = new Point(0, 0);
        p.setY(5);
        assertEquals(5, p.getY());
    }

    @ParameterizedTest(name = "x1={0}, y1={1}, x2={2}, y2={3}, x3={4}, y3={5}")
    @CsvSource({
            "1,1,0,0,1,1",
            "-1,-1,1,1,0,0",
            "1,2,0,3,1,5"
    })
    void parameterizedTest(int x1, int y1, int x2, int y2, int x3, int y3) {
        Point p = new Point(x1, y1);
        Point expected = new Point(x3, y3);
        p.translate(x2, y2);
        assertEquals(expected, p);
    }

    // this is not a test, should not be returned
    void notATest() {
        return;
    }

    void simple() {
        assertTrue(true);
    }
}