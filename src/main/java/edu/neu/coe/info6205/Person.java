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
    private Virus virus = null;

    private boolean isMasked;
    private boolean isVaccined;

    private int numOfInfections = 0;

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

    public int getInfectedTime() {
        return infectedTime;
    }

    public void setInfectedTime(int t) {
        this.infectedTime = t;
    }

    public void randomMove() {
        if (state == State.CONFIRMED || state == State.QUARANTINED || state == State.DEATH) {
            return;
        }
        int targetX = (int) stdGaussian(10, p.getX());
        int targetY = (int) stdGaussian(10, p.getY());
        Point target = new Point(targetX, targetY);
        formatCoordinate(target);
        p = target;
    }

    /**
     * a person's action in one day
     */
    public void action() {
        // if this person is quarantined or dead, no need to handle him/her any more
        if (state == State.DEATH) {
            return;
        }

        if (state == State.QUARANTINED) {
            if (virus.getFatality() && City.dayTime >= infectedTime + virus.getDieTime()) {
                state = State.DEATH;
                Hospital.getInstance().dischargePatient();
                return;
            }
            if (!virus.getFatality()) {
                boolean beCured = getResultForProbability(virus.getCureRate());
                if (beCured) {
                    state = State.NORMAL;
                    Hospital.getInstance().dischargePatient();
                    return;
                }
            }
        }

        if (state == State.CONFIRMED) {
            if (virus.getFatality() && City.dayTime >= infectedTime + virus.getDieTime()) {
                state = State.DEATH;
                return;
            }

            if (Hospital.getInstance().addPatient()) {
                state = State.QUARANTINED;
                return;
            }
        }

        if (state == State.SYMPTOMATIC) {
            boolean isTested = getResultForProbability(ConfigUtil.get("TEST", "TEST_RATE"));
            if (isTested) {
                state = State.CONFIRMED;
                if (Hospital.getInstance().addPatient()) {
                    state = State.QUARANTINED;
                    return;
                }
            }
            randomMove();
        }

        if (state == State.SHADOW) {
            if (City.dayTime >= infectedTime + virus.getShadowTime()) {
                state = State.SYMPTOMATIC;
            }
            randomMove();
        }

        if (state == State.NORMAL) {
            List<Person> people = PersonPool.getInstance().personList;
            for (Person person : people) {
                // infect simulation, not implemented
            }
            randomMove();
        }

    }

}
