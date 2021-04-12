package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;
import edu.neu.coe.info6205.util.MathUtil;

import java.util.List;
import java.util.Random;

import static edu.neu.coe.info6205.util.MathUtil.*;

/**
 * class for persons
 */

public class Person {

    private int state;
    private Point p;

    private int infectedTime = -1;
    private int destroyedTime = -1;
    private Virus virus = null;

    private boolean isMasked;
    private boolean isVaccined;
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
     * a person's action in one day
     */
    public void action() {
        // if this person is quarantined or dead, no need to handle him/her any more
        if (state == State.DEATH || state == State.DESTROYED) {
            return;
        }

        if (state == State.QUARANTINED) {
            PersonAction.quarantinedPersonAction(this);
        }

        if (state == State.CONFIRMED) {
            PersonAction.confirmedPersonAction(this);
        }

        if (state == State.SYMPTOMATIC) {
            PersonAction.symptomaticPersonAction(this);
        }

        if (state == State.SHADOW) {
            PersonAction.shadowPersonAction(this);
        }

        if (state == State.NORMAL || state == State.RECOVERED) {
            PersonAction.normalAndRecoveredPersonAction(this);
        }

    }

}
