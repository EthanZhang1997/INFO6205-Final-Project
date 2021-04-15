package edu.neu.coe.info6205;

public class Hospital {
    private final int capacity = 400;
    private int patients = 0;

    private static Hospital hospital = new Hospital();

    public static Hospital getInstance() {
        return hospital;
    }

    public boolean addPatient() {
        if (isFull()) {
            return false;
        }
        patients++;
        return true;
    }

    public void dischargePatient() {
        if (isEmpty()) {
            return;
        }
        patients--;
    }

    public boolean isFull () {
        return patients == capacity;
    }

    public boolean isEmpty () {
        return patients == 0;
    }
}
