package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;
import edu.neu.coe.info6205.util.MathUtil;

import static edu.neu.coe.info6205.util.MathUtil.getResultForProbability;
import static edu.neu.coe.info6205.util.MathUtil.stdGaussian;

public class Virus {
    public enum Viruses {
        COVID19, SARS
    }

    // the deviation for die time of the virus
    private static final int DIE_TIME_DEVIATION = 10;

    // the deviation for self-cure time of the virus
    private static final int SELF_CURE_TIME_DEVIATION = 5;

    // the name of the virus
    private String name;

    // average infections made by a single patient
    private double k;

    // the distribution for infections made by patients
    private double r;

    // transmission rate for this viurs
    private double transmissionRate;

    // mortality for this virus
    private double mortality;

    // whether the virus could be cured by people themselves
    private boolean selfCure;

    // average shadow time for this virus
    private double shadowTime;

    // average die time for this virus
    private double dieTime;

    // self-cure time for this virus if it can be cured by people themselves
    private double selfCureTime;

    // number of infections made by this virus
    private int numberOfInfections;

    // max number of infections could made by this virus
    private final int maxNumberOfInfections;

    // whether this virus can lead to death
    private boolean fatality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getTransmissionRate() {
        return transmissionRate;
    }

    public void setTransmissionRate(double transmissionRate) {
        this.transmissionRate = transmissionRate;
    }

    public double getMortality() {
        return mortality;
    }

    public void setMortality(double mortality) {
        this.mortality = mortality;
    }

    public boolean getSelfCureState() {
        return selfCure;
    }

    public void setSelfCureState(boolean selfCure) {
        this.selfCure = selfCure;
    }

    public double getShadowTime() {
        return shadowTime;
    }

    public void setShadowTime(double shadowTime) {
        this.shadowTime = shadowTime;
    }

    public double getDieTime() {
        return dieTime;
    }

    public void setDieTime(double dieTime) {
        this.dieTime = dieTime;
    }

    public double getSelfCureTime() {
        return selfCureTime;
    }

    public void setSelfCureTime(double selfCureTime) {
        this.selfCureTime = selfCureTime;
    }

    public boolean getFatality() {
        return fatality;
    }

    public void setFatality(boolean fatality) {
        this.fatality = fatality;
    }

    public int getNumberOfInfections() {
        return numberOfInfections;
    }

    /**
     * @author Ethan Zhang
     * @description simulate the infection made by this virus
     * @createTime  13/04/2021
     */
    public void makeAnInfection() {
        if (isMadeAllInfections()) {
            return;
        }
        numberOfInfections++;
    }

    /**
     * @author Ethan Zhang
     * @description check if this virus reaches the max number of infections it could made
     * @createTime  13/04/2021
     */
    public boolean isMadeAllInfections() {
        return numberOfInfections >= maxNumberOfInfections;
    }

    /**
     * @author Ethan Zhang
     * @description constructor for generate a virus, attributes are from the config.ini
     * @createTime  13/04/2021
     */
    public Virus(Viruses v) {
        this.name = v.toString();
        this.k = ConfigUtil.get(name, "K");
        this.r = ConfigUtil.get(name, "R");
        this.transmissionRate = ConfigUtil.get(name, "TRANSMISSION_RATE");
        this.mortality = ConfigUtil.get(name, "MORTALITY");
        this.shadowTime = ConfigUtil.get(name, "SHADOW_TIME");
        this.dieTime = (int) stdGaussian(DIE_TIME_DEVIATION, ConfigUtil.get(name, "DIE_TIME"));
        if (getResultForProbability(mortality)) {
            this.fatality = true;
        } else {
            this.fatality = false;
        }
        if (!fatality) {
            this.selfCure = MathUtil.getResultForProbability(ConfigUtil.get(name, "SELF_CURE_RATE"));
            this.selfCureTime = (int) stdGaussian(SELF_CURE_TIME_DEVIATION, ConfigUtil.get(name, "SELF_CURE_TIME"));
        }
        this.numberOfInfections = 0;
        this.maxNumberOfInfections = MathUtil.getInfectNumberFromNB(ConfigUtil.get(name, "R"), ConfigUtil.get(name, "K"));
    }
}
