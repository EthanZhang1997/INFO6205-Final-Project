package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.Point;
import umontreal.iro.lecuyer.probdist.NegativeBinomialDist;

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

    public static int getInfectNumberFromNB(float r, float k){
        double u = 1 - randomGen.nextDouble();
        //Get p from definition - mean = n(1 - p)/p
        //mean is r and n is k according to the paper
        double p = (double) k / (r + k);
        NegativeBinomialDist nb = new NegativeBinomialDist(k, p);
        return nb.inverseFInt(u);
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
        if (p.getX() > ConfigUtil.get("CITY", "WIDTH")) {
            p.setX((int) ConfigUtil.get("CITY", "WIDTH"));;
        }

        if (p.getY() > ConfigUtil.get("CITY", "HEIGHT")) {
            p.setY((int) ConfigUtil.get("CITY", "HEIGHT"));
        }
    }

    public static boolean getResultForProbability(double p) {
        return randomGen.nextInt(100) + 1 <= 100 * p;
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    public static void main(String[] args) {
        System.out.println(getInfectNumberFromNB(3.0f, 0.1f));
    }
}
