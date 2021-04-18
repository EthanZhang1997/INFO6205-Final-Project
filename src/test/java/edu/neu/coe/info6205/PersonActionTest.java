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
        City city = new City();

        Person p1 = new Person(0, 0, State.NORMAL);

        final int maxTrialCount = 10000000;

        // person who can't infect others
        Person p2 = new Person(1, 1, State.RECOVERED);
        for (int i = 0; i < maxTrialCount; i++) {
            if (PersonAction.infect(p1, p2)) {
                break;
            }
        }
        assertEquals(p1.getState(), State.NORMAL);

        // person who made enough number of infections
        Person p3 = new Person(1, 1, State.CONFIRMED);
        p3.setVirus(new Virus(Virus.Viruses.COVID19));
        p3.getVirus().setNumberOfInfections(p3.getVirus().getMaxNumberOfInfections());
        for (int i = 0; i < maxTrialCount; i++) {
            if (PersonAction.infect(p1, p3)) {
                break;
            }
        }
        assertEquals(p1.getState(), State.NORMAL);

        // person who may make an infection
        Person p4 = new Person(0, 0, State.CONFIRMED);
        p4.setVirus(new Virus(Virus.Viruses.COVID19));
        p4.getVirus().setMaxNumberOfInfections(1);
        for (int i = 0; i < maxTrialCount; i++) {
            if (PersonAction.infect(p1, p4)) {
                break;
            }
        }
        assertEquals(p1.getState(), State.SHADOW);
    }

    @org.junit.jupiter.api.Test
    void quarantinedPersonAction() {
        City city = new City();
        City.dayTime = 20;
        final int maxTrialCount = 10000000;

        Person p1 = new Person(50, 800, State.QUARANTINED);
        p1.setVirus(new Virus(Virus.Viruses.COVID19));
        p1.setInfectedTime(10);
        p1.getVirus().setFatality(false);
        p1.getVirus().setSelfCureState(true);
        p1.getVirus().setSelfCureTime(15);
        for (int i = 0; i < maxTrialCount; i++) {
            PersonAction.quarantinedPersonAction(p1);
            if (p1.getState() != State.QUARANTINED) {
                break;
            }
        }
        assertEquals(p1.getState(), State.RECOVERED);

    }

    @org.junit.jupiter.api.Test
    void shadowPersonAction() {
        City city = new City();
        City.dayTime = 20;

        Person p1 = new Person(0, 0, State.SHADOW);
        p1.setVirus(new Virus(Virus.Viruses.COVID19));
        p1.setInfectedTime(10);
        p1.getVirus().setShadowTime(10);
        PersonAction.shadowPersonAction(p1);
        assertEquals(p1.getState(), State.SYMPTOMATIC);
    }
}