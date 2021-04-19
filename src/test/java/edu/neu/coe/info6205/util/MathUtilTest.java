package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilTest {

    @Test
    void formatCoordinate() {
        Point p = new Point(-1 ,900);
        MathUtil.formatCoordinate(p);
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), ConfigUtil.get("CITY", "HEIGHT"));
    }

    @Test
    void distance() {
        Point p1 = new Point(0 ,0);
        Point p2 = new Point(3, 4);
        assertEquals(MathUtil.distance(p1, p2), 5);
    }
}