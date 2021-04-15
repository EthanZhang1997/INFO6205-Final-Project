package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;
import edu.neu.coe.info6205.util.MathUtil;

import java.util.List;

import static edu.neu.coe.info6205.util.MathUtil.*;

public class PersonAction {

    private final static int step = 10;

    public static void randomMove(Person person1) {

        // quarantined,dead and destroyed people can't move
        if (person1.getState() == State.QUARANTINED || person1.getState() == State.DEATH ||
                person1.getState() == State.DESTROYED) {
            return;
        }

        // only 50% person want to move
        if (MathUtil.getResultForProbability(0.5)) {
            return;
        }

        // randomly generate a new coordinate around the former point
        int targetX = (int) stdGaussian(step, person1.getPoint().getX());
        int targetY = (int) stdGaussian(step, person1.getPoint().getY());
        Point target = new Point(targetX, targetY);

        // format the generated coordinate
        formatCoordinate(target);

        person1.setPoint(target);
    }

    public static boolean die(Person person1) {
        // if the virus is fatal, make this person die in a certain day
        if (person1.getVirus().getFatality() && City.dayTime >= person1.getInfectedTime() + person1.getVirus().getDieTime()) {
            person1.setState(State.DEATH);
            Hospital.getInstance().dischargePatient();
            return true;
        }
        return false;
    }

    public static boolean selfCure(Person person1) {
        if (!person1.getVirus().getFatality() && person1.getVirus().getSelfCureState() &&
                City.dayTime >= person1.getInfectedTime() + person1.getVirus().getSelfCureTime()) {
            person1.setState(State.RECOVERED);
            person1.setContactTraceState(false);
            person1.setInfectedTime(-1);
            person1.setVirus(null);
            return true;
        }
        return false;
    }

    public static boolean beDestroyed(Person person1) {
        // if the virus is not fatal but it can't be cured, this patient has chance to be destroyed by this virus
        if (!person1.getVirus().getFatality() && !person1.getVirus().getSelfCureState()) {
            person1.setState(State.DESTROYED);
            person1.setDestroyedTime(City.dayTime);
            person1.setContactTraceState(false);
            person1.setInfectedTime(-1);
            person1.setVirus(null);
            return true;
        }
        return false;
    }

    public static boolean infect(Person person1, Person person2) {

        // if the second person doesn't be infected, there won't be an infection between these 2 people
        if (person2.getState() != State.SHADOW && person2.getState() != State.SYMPTOMATIC && person2.getState() != State.CONFIRMED) {
            return false;
        }

        // if the second person has infected enough people, there won't be an infection between these 2 people
        if (person2.getVirus().isMadeAllInfections()) {
            return false;
        }

        // if these 2 people keep a safe distance, there won't be an infection
        double distance = MathUtil.distance(person1.getPoint(), person2.getPoint());
        if (distance >= ConfigUtil.get(person2.getVirus().getName(), "SAFE_DISTANCE")) {
            return false;
        }

        // infection rate is lower when people have greater distance
        double infectionRate = Math.pow(ConfigUtil.get(person2.getVirus().getName(), "INFECTION_RATE"), distance);

        // people who get vaccined have lower possibility to be infected
        if (person1.getVaccineState()) {
            infectionRate *= 1 - ConfigUtil.get("VACCINE", "EFFICIENCY");
        }

        // people who wear masks have lower possibility to be infected
        if (person1.getMaskState() || person2.getMaskState()) {
            infectionRate *= 1 - ConfigUtil.get("Mask", "EFFICIENCY");
        }

        // if the first person has antibodyies, it has lower possibility to be infected
        if (person1.getState() == State.RECOVERED) {
            infectionRate *= 1 - ConfigUtil.get(person2.getVirus().getName(), "RECOVERED_RESISTENCE");
        }

        // get the result whether this person has been infected
        boolean isInfected = MathUtil.getResultForProbability(infectionRate);

        // update the state of the newly infected patient and the infection source
        if (isInfected) {
            person1.setVirus(new Virus(Virus.Viruses.COVID19));
            person1.setState(State.SHADOW);
            person1.setInfectedTime(City.dayTime);
            person2.getVirus().makeAnInfection();
            return true;
        }

        return false;
    }

    public static void quarantinedPersonAction(Person person1) {

        if (die(person1)) {
            Hospital.getInstance().dischargePatient();
            return;
        }

        if (beDestroyed(person1)) {
            Hospital.getInstance().dischargePatient();
            return;
        }

        if (selfCure(person1)) {
            Hospital.getInstance().dischargePatient();
            return;
        }

        // if the virus is not fatal, everyday this patient has chance to be cured in the hospital
        if (!person1.getVirus().getFatality()) {
            boolean beCured = getResultForProbability(ConfigUtil.get(person1.getVirus().getName(), "CURE_RATE"));
            if (beCured) {
                person1.setState(State.RECOVERED);
                Hospital.getInstance().dischargePatient();
                person1.setVirus(null);
                person1.setInfectedTime(-1);
                person1.setContactTraceState(false);
            }
        }
    }

    public static void confirmedPersonAction(Person person1) {

        // if the virus is fatal, make this person die in a certain day
        if (die(person1)) {
            return;
        }

        if (beDestroyed(person1)) {
            return;
        }

        if (selfCure(person1)) {
            return;
        }

        // if the hospital has enough beds, quarantine the patient to the hosptial
        if (Hospital.getInstance().addPatient()) {
            person1.setState(State.QUARANTINED);
            person1.setContactTraceState(false);
            return;
        }

        if (!person1.getContactTraceState()) {
            randomMove(person1);
        }
    }


    public static void symptomaticPersonAction(Person person1) {

        if (die(person1)) {
            return;
        }

        if (beDestroyed(person1)) {
            return;
        }

        if (selfCure(person1)) {
            return;
        }

        // if a person performs symptomatic, he/she has chance to be tested
        boolean isTested = getResultForProbability(ConfigUtil.get("TEST", "TEST_RATE"));
        if (isTested) {
            person1.setState(State.CONFIRMED);
            person1.setContactTraceState(getResultForProbability(ConfigUtil.get("TEST", "CONTACT_TRACE_RATE")));
            if (Hospital.getInstance().addPatient()) {
                person1.setState(State.QUARANTINED);
                person1.setContactTraceState(false);
                return;
            }
        }

        if (!person1.getContactTraceState()) {
            randomMove(person1);
        }
    }

    public static void shadowPersonAction(Person person1) {
        if (City.dayTime >= person1.getInfectedTime() + person1.getVirus().getShadowTime()) {
            person1.setState(State.SYMPTOMATIC);
        }
        randomMove(person1);
    }

    public static void normalAndRecoveredPersonAction(Person person1) {
        List<Person> people = PersonPool.getInstance().personList;
        for (Person person2 : people) {
            if (infect(person1, person2)) {
                break;
            }
        }
        randomMove(person1);
    }




}
