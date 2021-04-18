package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonPoolTest {

    @Test
    void getPeopleSize() {
        // create a new city
        City city = new City();
        assertEquals(PersonPool.getInstance().getPeopleSize(-1), 0);

        // add initial citizens to the city
        PersonPool.getInstance().addInitialCitizens();
        assertEquals(PersonPool.getInstance().getPeopleSize(State.NORMAL), ConfigUtil.get("CITY", "PERSON_SIZE"));
        assertEquals(PersonPool.getInstance().getPeopleSize(-1), ConfigUtil.get("CITY", "PERSON_SIZE"));

        // add initial patients to the city
        Virus.Viruses v = Virus.Viruses.COVID19;
        PersonPool.getInstance().addInitialPatients(v);
        assertEquals(PersonPool.getInstance().getPeopleSize(State.NORMAL), ConfigUtil.get("CITY", "PERSON_SIZE"));
        assertEquals(PersonPool.getInstance().getPeopleSize(State.SHADOW), ConfigUtil.get("CITY", "INITIAL_PATIENTS"));
        assertEquals(PersonPool.getInstance().getPeopleSize(-1), ConfigUtil.get("CITY", "PERSON_SIZE") + ConfigUtil.get("CITY", "INITIAL_PATIENTS"));
    }
}