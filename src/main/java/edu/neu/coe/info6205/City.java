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

    /**
     * @author Ethan Zhang
     * @description constructor for this city, initialize it with center coordinate
     * @createTime  13/04/2021
     */
    public City() {
        this.centerX = (int) ConfigUtil.get("CITY", "CENTERX");
        this.centerY = (int) ConfigUtil.get("CITY", "CENTERY");
    }

    /**
     * @author Ethan Zhang
     * @description simulate one day in this city
     * @createTime  13/04/2021
     */
    public void dayPass() {
        List<Person> people = PersonPool.getInstance().getPersonList();
        for (Person p : people) {
            p.action();
        }
        dayTime++;
    }

    /**
     * @author Ethan Zhang
     * @description print the state of the city in the console
     * @createTime  13/04/2021
     */
    public static void printTheStateOfCity() {
        System.out.println("The " + dayTime + " day in the city:");
        System.out.println("The number of normal people in the city: " + PersonPool.getInstance().getPeopleSize(State.NORMAL));
        System.out.println("The number of shadowed people in the city: " + PersonPool.getInstance().getPeopleSize(State.SHADOW));
        System.out.println("The number of symptomatic people in the city: " + PersonPool.getInstance().getPeopleSize(State.SYMPTOMATIC));
        System.out.println("The number of confirmed people in the city: " + PersonPool.getInstance().getPeopleSize(State.CONFIRMED));
        System.out.println("The number of quarantined people in the city: " + PersonPool.getInstance().getPeopleSize(State.QUARANTINED));
        System.out.println("The number of dead people in the city: " + PersonPool.getInstance().getPeopleSize(State.DEATH));
        System.out.println("The number of recovered people in the city: " + PersonPool.getInstance().getPeopleSize(State.RECOVERED));
        System.out.println("The number of destroyed people in the city: " + PersonPool.getInstance().getPeopleSize(State.DESTROYED));
    }

}
