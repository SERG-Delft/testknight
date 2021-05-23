package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    @Test
    void duplicate() {
        Point p = new Point(0, 1);
    }

    @Test
    void containing() {
        Point p = new Point(0, somefunc(1, 2));
    }

    @Test
    void nestedContains() {
        Point p = new Point(0, somefunc(foo(), bar(1, 2, 3)), dar());
    }
}
