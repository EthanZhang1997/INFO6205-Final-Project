package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;

import static edu.neu.coe.info6205.util.MathUtil.getResultForProbability;

public class Virus {
    public enum Viruses {
        COVID19, SARS
    }

    private String name;
    private double k;
    private double r;
    private double transmissionRate;
    private double mortality;
    private double cureRate;
    private double shadowTime;
    private double dieTime;

    private boolean fatality;
    private boolean numbersOfSecondInfection;


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

    public double getCureRate() {
        return cureRate;
    }

    public void setCureRate(double cureRate) {
        this.cureRate = cureRate;
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

    public boolean getFatality() {
        return fatality;
    }

    public void setFatality(boolean fatality) {
        this.fatality = fatality;
    }

    public Virus(Viruses v) {
        this.name = v.toString();
        this.k = ConfigUtil.get(name, "K");
        this.r = ConfigUtil.get(name, "R");
        this.transmissionRate = ConfigUtil.get(name, "TRANSMISSION_RATE");
        this.mortality = ConfigUtil.get(name, "MORTALITY");
        this.cureRate = ConfigUtil.get(name, "CURE_RATE");
        this.shadowTime = ConfigUtil.get(name, "SHADOW_TIME");
        this.dieTime = ConfigUtil.get(name, "DIE_TIME");
        if (getResultForProbability(mortality)) {
            this.fatality = true;
        } else {
            this.fatality = false;
        }
    }
}
