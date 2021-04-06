package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;

import java.util.List;

/**
 * class for city
 */
public class City {
    private int centerX;
    private int centerY;

    // the number of days passed in the city
    public static int dayTime = 0;

    public City() {
        this.centerX = (int) ConfigUtil.get("CITY", "CENTERX");
        this.centerY = (int) ConfigUtil.get("CITY", "CENTERY");
    }

    // one day passes
    public static void dayPass() {
        List<Person> people = PersonPool.getInstance().getPersonList();
        for (Person p : people) {
            p.action();
        }
        dayTime++;
    }

}
