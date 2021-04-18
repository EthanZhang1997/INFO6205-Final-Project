package edu.neu.coe.info6205;

import static org.junit.jupiter.api.Assertions.*;
class PersonActionTest {

    @org.junit.jupiter.api.Test
    void die() {
        City city = new City();
        City.dayTime = 20;

        // person who should die
        Person p1 = new Person(0, 0, State.CONFIRMED);
        p1.setVirus(new Virus(Virus.Viruses.COVID19));
        p1.setInfectedTime(10);
        p1.getVirus().setFatality(true);
        p1.getVirus().setDieTime(10);
        PersonAction.die(p1);
        assertEquals(p1.getState(), State.DEATH);

        // person who should die but doesn't reach his/her die time
        Person p2 = new Person(0, 0, State.CONFIRMED);
        p2.setVirus(new Virus(Virus.Viruses.COVID19));
        p2.setInfectedTime(11);
        p2.getVirus().setFatality(true);
        p2.getVirus().setDieTime(10);
        PersonAction.die(p2);
        assertEquals(p2.getState(), State.CONFIRMED);

        // person who should not die
        Person p3 = new Person(0, 0, State.CONFIRMED);
        p3.setVirus(new Virus(Virus.Viruses.COVID19));
        p3.setInfectedTime(11);
        p3.getVirus().setFatality(false);
        PersonAction.die(p3);
        assertEquals(p3.getState(), State.CONFIRMED);
    }

    @org.junit.jupiter.api.Test
    void selfCure() {
        City city = new City();
        City.dayTime = 20;

        // person who should self-cure
        Person p1 = new Person(0, 0, State.CONFIRMED);
        p1.setVirus(new Virus(Virus.Viruses.COVID19));
        p1.setInfectedTime(10);
        p1.getVirus().setFatality(false);
        p1.getVirus().setSelfCureState(true);
        p1.getVirus().setSelfCureTime(10);
        PersonAction.selfCure(p1);
        assertEquals(p1.getState(), State.RECOVERED);

        // person who should self-cure but doesn't reach his/her self-cure time
        Person p2 = new Person(0, 0, State.CONFIRMED);
        p2.setVirus(new Virus(Virus.Viruses.COVID19));
        p2.setInfectedTime(10);
        p2.getVirus().setFatality(false);
        p2.getVirus().setSelfCureState(true);
        p2.getVirus().setSelfCureTime(11);
        PersonAction.selfCure(p2);
        assertEquals(p2.getState(), State.CONFIRMED);

        // person who should not self-cure
        Person p3 = new Person(0, 0, State.CONFIRMED);
        p3.setVirus(new Virus(Virus.Viruses.COVID19));
        p3.setInfectedTime(10);
        p3.getVirus().setSelfCureState(false);
        PersonAction.selfCure(p3);
        assertEquals(p3.getState(), State.CONFIRMED);
    }

    @org.junit.jupiter.api.Test
    void beDestroyed() {
        City city = new City();
        City.dayTime = 20;

        // person who should recover with aftereffects
        Person p1 = new Person(0, 0, State.CONFIRMED);
        p1.setVirus(new Virus(Virus.Viruses.COVID19));
        p1.setInfectedTime(10);
        p1.getVirus().setFatality(false);
        p1.getVirus().setSelfCureState(false);

        int count = 0;
        while (true) {
            if (PersonAction.beDestroyed(p1)) {
                assertEquals(p1.getState(), State.DESTROYED);
                break;
            }
            count++;
            if (count > 10000) {
                assertEquals("Could be destroyed", "Could not be destroyed");
                break;
            }
        }
    }

    @org.junit.jupiter.api.Test
    void infect() {
    }

    @org.junit.jupiter.api.Test
    void quarantinedPersonAction() {
    }

    @org.junit.jupiter.api.Test
    void confirmedPersonAction() {
    }

    @org.junit.jupiter.api.Test
    void symptomaticPersonAction() {
    }

    @org.junit.jupiter.api.Test
    void shadowPersonAction() {
    }

    @org.junit.jupiter.api.Test
    void normalAndRecoveredPersonAction() {
    }
}