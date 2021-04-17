package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static edu.neu.coe.info6205.util.MathUtil.formatCoordinate;
import static edu.neu.coe.info6205.util.MathUtil.stdGaussian;

/**
 * @author Ethan Zhang
 * @description person pool, which contains the people living in the city
 * @createTime  13/04/2021
 */
public class PersonPool {
    private static PersonPool personPool = new PersonPool();

    public static PersonPool getInstance() {
        return personPool;
    }

    List<Person> personList = new ArrayList<Person>();

    /**
     * @author Ethan Zhang
     * @description get the list of the people living in the pool
     * @createTime  13/04/2021
     * @return the list of the people living in the pool
     */
    public List<Person> getPersonList() {
        return personList;
    }

    /**
     * @author Ethan Zhang
     * @description get the number of people with specific state
     * @createTime  13/04/2021
     * @param state
     * @return number of people with the given state
     */
    public int getPeopleSize(int state) {
        if (state == -1) {
            return personList.size();
        }
        int i = 0;
        for (Person person : personList) {
            if (person.getState() == state) {
                i++;
            }
        }
        return i;
    }

    /**
     * @author Ethan Zhang
     * @description add new person to the person pool
     * @createTime  13/04/2021
     * @param state the state of the person, v the virus this person carrys
     */
    public void addNewPersonToPool(int state, Virus.Viruses v) {
        Random random = new Random();
        // randomly generate coordinates around the center
        int x = (int) (stdGaussian(100, ConfigUtil.get("CITY", "CENTERX")));
        int y = (int) (stdGaussian(100, ConfigUtil.get("CITY", "CENTERY")));
        Point point = new Point(x, y);
        formatCoordinate(point);
        Person person = new Person(point, state);
        if (!(state == State.NORMAL)) {
            person.setInfectedTime(City.dayTime);
            person.setVirus(new Virus(v));
        }
        personList.add(person);
    }

    /**
     * @author Ethan Zhang
     * @description add patients who carry specific virus to the person pool
     * @createTime  13/04/2021
     * @param v the specific virus
     **/
    public void addInitialPatients(Virus.Viruses v) {
        // add initial patients to the pool
        for (int i = 0; i < ConfigUtil.get("CITY", "INITIAL_PATIENTS"); i++) {
            addNewPersonToPool(State.SHADOW, v);
        }
    }

    /**
     * @author Ethan Zhang
     * @description add normal persons to the pool, constructor for this class
     * @createTime  13/04/2021
     */
    private PersonPool() {
        // add normal persons to the pool
        for (int i = 0; i < ConfigUtil.get("CITY", "PERSON_SIZE"); i++) {
            addNewPersonToPool(State.NORMAL, null);
        }
    }
}
