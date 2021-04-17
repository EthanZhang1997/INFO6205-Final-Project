package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;
import edu.neu.coe.info6205.util.MathUtil;

import java.util.List;
import java.util.Random;

import static edu.neu.coe.info6205.util.MathUtil.*;

/**
 * @author Ethan Zhang
 * @description class for persons
 * @createTime  13/04/2021
 */
public class Person {

    // the person's state
    private int state;

    // the person's coordinate
    private Point p;

    // the person's infected time, if the person hasn't been infected, the number is -1
    private int infectedTime = -1;

    // the person's destroyed time, if the person hasn't been destroyed, the number is -1
    private int destroyedTime = -1;

    // the virus this person carries
    private Virus virus = null;

    // whether this person wears a mask
    private boolean isMasked;

    // whether this person receives a vaccine
    private boolean isVaccined;

    // whether this person has been contact traced
    private boolean isContactTraced;

    public Person(Point p, int state) {
        this.p = p;
        this.state = state;
    }

    public Person(int x, int y, int state) {
        this.p = new Point(x, y);
        this.state = state;
        this.isMasked = getResultForProbability(ConfigUtil.get("MASK", "USAGE"));
        this.isVaccined = getResultForProbability(ConfigUtil.get("VACCINE", "AVAILABILITY"));
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Point getPoint() {
        return p;
    }

    public void setPoint(Point p) {
        this.p = p;
    }

    public Virus getVirus() {
        return virus;
    }

    public void setVirus(Virus v) {
        this.virus = v;
    }

    public boolean getMaskState() {
        return isMasked;
    }

    public void setMaskState(boolean b) {
        this.isMasked = b;
    }

    public boolean getVaccineState() {
        return isVaccined;
    }

    public void setVaccineState(boolean b) {
        this.isVaccined = b;
    }

    public boolean getContactTraceState() {
        return isContactTraced;
    }

    public void setContactTraceState(boolean b) {
        this.isContactTraced = b;
    }

    public int getInfectedTime() {
        return infectedTime;
    }

    public void setInfectedTime(int t) {
        this.infectedTime = t;
    }

    public int getDestroyedTime() {
        return destroyedTime;
    }

    public void setDestroyedTime(int t) {
        this.destroyedTime = t;
    }

    /**
     * @author Ethan Zhang
     * @description a person's action in one day, people with different states acts differently
     * @createTime  13/04/2021
     */
    public void action() {
        // if this person is destroyed or dead, no need to handle him/her any more
        if (state == State.DEATH || state == State.DESTROYED) {
            return;
        }

        // quarantined people's action
        if (state == State.QUARANTINED) {
            PersonAction.quarantinedPersonAction(this);
            return;
        }

        // confirmed people's action
        if (state == State.CONFIRMED) {
            PersonAction.confirmedPersonAction(this);
            return;
        }

        // symtomatic people's action
        if (state == State.SYMPTOMATIC) {
            PersonAction.symptomaticPersonAction(this);
            return;
        }

        // shadowed people's action
        if (state == State.SHADOW) {
            PersonAction.shadowPersonAction(this);
            return;
        }

        // normal and recovered people's action
        if (state == State.NORMAL || state == State.RECOVERED) {
            PersonAction.normalAndRecoveredPersonAction(this);
        }
    }

}
