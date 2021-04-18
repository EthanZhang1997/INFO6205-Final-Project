package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;
import edu.neu.coe.info6205.util.MathUtil;

import java.util.List;

import static edu.neu.coe.info6205.util.MathUtil.*;

public class PersonAction {

    // a person's step (average distance a single person can move in one day)
    private final static int step = 10;

    /**
     * @author Ethan Zhang
     * @description the action of a person's randomly move
     * @createTime  13/04/2021
     * @param person1 the person who may move
     */
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

        // randomly generate a new coordinate around the person's former coordinate
        int targetX = (int) stdGaussian(step, person1.getPoint().getX());
        int targetY = (int) stdGaussian(step, person1.getPoint().getY());
        Point target = new Point(targetX, targetY);

        // format the generated coordinate
        formatCoordinate(target);

        // set new coordinate for this person
        person1.setPoint(target);
    }

    /**
     * @author Ethan Zhang
     * @description simulate a person's death
     * @createTime  13/04/2021
     * @param person1 the person who may die
     * @return true if the people die, false if not die
     */
    public static boolean die(Person person1) {
        // if the virus is fatal, and the current time reaches the person's die time, make this person die
        if (person1.getVirus().getFatality() && City.dayTime >= person1.getInfectedTime() + person1.getVirus().getDieTime()) {
            person1.setState(State.DEATH);
            Hospital.getInstance().dischargePatient();
            return true;
        }
        return false;
    }

    /**
     * @author Ethan Zhang
     * @description simulate a person's self-cure action
     * @createTime  13/04/2021
     * @param person1 the person who may self-cure
     * @return true if the people self-cure, false if not self-cure
     */
    public static boolean selfCure(Person person1) {
        // if the virus is not fatal, and the virus can be cured by people themselves
        // when the current time reaches the person's self-cure time, make this person self-cure
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

    /**
     * @author Ethan Zhang
     * @description simulate a person's recover with aftereffect
     * @createTime  13/04/2021
     * @param person1 the person who may recover with aftereffect
     * @return true if the people recover with aftereffect, false if not
     */
    public static boolean beDestroyed(Person person1) {
        // if the virus is not fatal but it can't be cured by people themselves, this patient has chance to recover with aftereffect
        if (!person1.getVirus().getFatality() && !person1.getVirus().getSelfCureState()) {
            if (getResultForProbability(ConfigUtil.get(person1.getVirus().getName(), "DESTROYED_RATE"))) {
                person1.setState(State.DESTROYED);
                person1.setDestroyedTime(City.dayTime);
                person1.setContactTraceState(false);
                person1.setInfectedTime(-1);
                person1.setVirus(null);
                return true;
            }
        }
        return false;
    }

    /**
     * @author Ethan Zhang
     * @description simulate the infection between 2 persons
     * @createTime  13/04/2021
     * @param person1 the person who may be infected, person2 the person who may infect others
     * @return true if there is an infection, false if there isn't
     */
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

        // if the first person has antibodyies, he/she has lower possibility to be infected
        if (person1.getState() == State.RECOVERED) {
            infectionRate *= 1 - ConfigUtil.get(person2.getVirus().getName(), "RECOVERED_RESISTENCE");
        }

        // get the result whether this person has been infected
        boolean isInfected = MathUtil.getResultForProbability(infectionRate);

        // if there is an infection, update the state of the newly infected patient and the infection source person
        if (isInfected) {
            person1.setVirus(new Virus(Virus.Viruses.valueOf(person2.getVirus().getName())));
            person1.setState(State.SHADOW);
            person1.setInfectedTime(City.dayTime);
            person2.getVirus().makeAnInfection();
            return true;
        }

        return false;
    }

    /**
     * @author Ethan Zhang
     * @description quarantined peoples' action
     * @createTime  13/04/2021
     * @param person1 person with state of quarantined
     */
    public static void quarantinedPersonAction(Person person1) {

        // check if he/she would die
        if (die(person1)) {
            Hospital.getInstance().dischargePatient();
            return;
        }

        // check if he/she would recover with aftereffects
        if (beDestroyed(person1)) {
            Hospital.getInstance().dischargePatient();
            return;
        }

        // check if he/she would self-cure
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

    /**
     * @author Ethan Zhang
     * @description confirmed peoples' action
     * @createTime  13/04/2021
     * @param person1 person with state of confirmed
     */
    public static void confirmedPersonAction(Person person1) {

        // check if he/she would die
        if (die(person1)) {
            return;
        }

        // check if he/she would recover with aftereffects
        if (beDestroyed(person1)) {
            return;
        }

        // check if he/she would self-cure
        if (selfCure(person1)) {
            return;
        }

        // if the hospital has enough beds, quarantine the patient to the hosptial
        if (Hospital.getInstance().addPatient()) {
            person1.setState(State.QUARANTINED);
            person1.setContactTraceState(false);
            return;
        }

        // if this patient has not been contact traced, he/she may still move randomly in the city
        if (!person1.getContactTraceState()) {
            randomMove(person1);
        }
    }

    /**
     * @author Ethan Zhang
     * @description symptomatic peoples' action
     * @createTime  13/04/2021
     * @param person1 person with state of symptomatic
     */
    public static void symptomaticPersonAction(Person person1) {

        // check if he/she would die
        if (die(person1)) {
            return;
        }

        // check if he/she would recover with aftereffects
        if (beDestroyed(person1)) {
            return;
        }

        // check if he/she would self-cure
        if (selfCure(person1)) {
            return;
        }

        // if a person performs symptomatic, he/she has chance to be tested
        boolean isTested = getResultForProbability(ConfigUtil.get("TEST", "TEST_RATE"));
        if (isTested) {
            person1.setState(State.CONFIRMED);
            person1.setContactTraceState(getResultForProbability(ConfigUtil.get("TEST", "CONTACT_TRACE_RATE")));
            // if the hospital still have beds, quarantine this patient to the hospital
            if (Hospital.getInstance().addPatient()) {
                person1.setState(State.QUARANTINED);
                person1.setContactTraceState(false);
                return;
            }
        }

        // if this patient has not been contact traced, he/she may still move randomly in the city
        if (!person1.getContactTraceState()) {
            randomMove(person1);
        }
    }

    /**
     * @author Ethan Zhang
     * @description shadowed peoples' action
     * @createTime  13/04/2021
     * @param person1 person with state of shadow
     */
    public static void shadowPersonAction(Person person1) {
        // if the current time reaches the virus's shadow time, this person would start performing symptomatically
        if (City.dayTime >= person1.getInfectedTime() + person1.getVirus().getShadowTime()) {
            person1.setState(State.SYMPTOMATIC);
        }

        // shadowed person may move randomly in the city
        randomMove(person1);
    }

    /**
     * @author Ethan Zhang
     * @description normal and recovered peoples' action
     * @createTime  13/04/2021
     * @param person1 person with state of normal or recovered
     */
    public static void normalAndRecoveredPersonAction(Person person1) {
        // check if there is an infection on this person
        List<Person> people = PersonPool.getInstance().personList;
        for (Person person2 : people) {
            if (infect(person1, person2)) {
                break;
            }
        }

        // normal and recovered person may move randomly in the city
        randomMove(person1);
    }




}
