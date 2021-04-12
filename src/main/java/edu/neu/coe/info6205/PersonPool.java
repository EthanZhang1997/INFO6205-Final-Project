package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static edu.neu.coe.info6205.util.MathUtil.formatCoordinate;
import static edu.neu.coe.info6205.util.MathUtil.stdGaussian;

public class PersonPool {
    private static PersonPool personPool = new PersonPool();

    public static PersonPool getInstance() {
        return personPool;
    }

    List<Person> personList = new ArrayList<Person>();

    public List<Person> getPersonList() {
        return personList;
    }

    /**
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

    private PersonPool() {
        // add normal persons in the pool
        for (int i = 0; i < ConfigUtil.get("CITY", "PERSON_SIZE"); i++) {
            addNewPersonToPool(State.NORMAL, null);
        }

        for (int i = 0; i < ConfigUtil.get("CITY", "INITIAL_PATIENTS"); i++) {
            addNewPersonToPool(State.SHADOW, Virus.Viruses.COVID19);
        }
    }
}
