package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    @Test
    void basic() {
        // contents
    }

    @Test
    public static void hasModifiers() {
        // contents
    }

    @Test
    String hasReturnTy() {
        // contents
    }

    @Test
    void hasParams(int x, int y) {
        // contents
        int expected = x;
        int actual = SomeClass.magic(x, y);
        assertEquals(expected, actual);
    }

    @Test
    <A, B> void hasTypeParams() {
        // contents
    }

    @Test
    void throwsException() throws Exception {
        // contents
    }

    @Test
    void hasAssertion() {
        assertEquals(2, 1 + 1)
    }
}