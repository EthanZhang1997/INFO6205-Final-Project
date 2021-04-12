package edu.neu.coe.info6205;

/**
 * state for citizens
 */
public class State {
    public static int NORMAL = 0;
    public static int SHADOW = NORMAL + 1;
    public static int SYMPTOMATIC = SHADOW + 1;
    public static int CONFIRMED = SYMPTOMATIC + 1;
    public static int QUARANTINED = CONFIRMED + 1;
    public static int DEATH = QUARANTINED + 1;
    public static int RECOVERED = DEATH + 1;
    public static int DESTROYED = RECOVERED + 1;
}
