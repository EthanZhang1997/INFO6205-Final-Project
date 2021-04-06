package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.Point;

import java.util.Random;

/**
 * Math Utilities
 */
public class MathUtil {
    private static final Random randomGen = new Random();

    public static double stdGaussian(double sigma, double u) {
        double X = randomGen.nextGaussian();
        return sigma * X + u;
    }

    public static int randomDisplacement() {
        double X = randomGen.nextGaussian();
        return (int) (ConfigUtil.get("PERSON", "STEP") * X);
    }

    public static void formatCoordinate(Point p) {
        if (p.getX() < 0) {
            p.setX(0);
        }
        if (p.getY() < 0) {
            p.setY(0);
        }
        if (p.getX() > ConfigUtil.get("City", "WIDTH")) {
            p.setX((int) ConfigUtil.get("City", "WIDTH"));;
        }

        if (p.getY() > ConfigUtil.get("City", "HEIGHT")) {
            p.setY((int) ConfigUtil.get("City", "WIDTH"));
        }
    }

    public static boolean getResultForProbability(double p) {
        return randomGen.nextInt(100) + 1 <= 100 * p;
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }
}
