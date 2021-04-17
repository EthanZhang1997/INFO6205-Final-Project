package edu.neu.coe.info6205;

/**
 * state for citizens
 */
public class State {
    // normal people
    public static int NORMAL = 0;

    // shadowed people with virus and without sympton
    public static int SHADOW = NORMAL + 1;

    //symptomatic patients
    public static int SYMPTOMATIC = SHADOW + 1;

    // confirmed people with viurs
    public static int CONFIRMED = SYMPTOMATIC + 1;

    // quarantined patients
    public static int QUARANTINED = CONFIRMED + 1;

    // dead people
    public static int DEATH = QUARANTINED + 1;

    // recovered people
    public static int RECOVERED = DEATH + 1;

    // people recovered with aftereffects
    public static int DESTROYED = RECOVERED + 1;
}
