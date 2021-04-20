package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.Point;
import umontreal.iro.lecuyer.probdist.NegativeBinomialDist;

import java.util.Random;

/**
 * @description Math Utilities
 * @createTime  13/04/2021
 */
public class MathUtil {
    // set a static Random generator
    private static final Random randomGen = new Random();

    /**
     * @description return a double obeys the Gaussian distribution
     * @createTime  13/04/2021
     * @param sigma Normal standard deviation, u Normal mean parameter
     * @return
     */
    public static double stdGaussian(double sigma, double u) {
        double X = randomGen.nextGaussian();
        return sigma * X + u;
    }

    /**
     * @author Gan Li
     * @description get the infection number by K and R factor using negative binomial distribution function
     * @createTime  17/04/2021
     * @return
     */
    public static int getInfectNumberFromNB(float r, float k){
        double u = 1 - randomGen.nextDouble();
        //Get p from definition - mean = n(1 - p)/p
        //mean is r and n is k according to the paper
        double p = (double) k / (r + k);
        NegativeBinomialDist nb = new NegativeBinomialDist(k, p);
        return nb.inverseFInt(u);
    }

    /**
     * @author Ethan Zhang
     * @description format a person's coordinate, if it's out of bounds, just set it at the bound line
     * @createTime  13/04/2021
     * @param p the person's coordinate
     * @return
     */
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

    /**
     * @author Ethan Zhang
     * @description get the result according to the possibility
     * @createTime  13/04/2021
     * @param p possibility for this event happening
     * @return
     */
    public static boolean getResultForProbability(double p) {
        return randomGen.nextInt(100) + 1 <= 100 * p;
    }

    /**
     * @author Ethan Zhang
     * @description get the distance between 2 coordinates
     * @createTime  13/04/2021
     * @param p1 the first coordinate, p2 the second coorordinate
     * @return the distance between 2 coordinates
     */
    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    public static void main(String[] args) {
        System.out.println(getInfectNumberFromNB(3.0f, 0.1f));
    }
}
