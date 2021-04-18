package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

public class Hospital {
    public static final int X = 50;
    public static final int Y = 800;
    private int width = 600;
    private int height;

    // the capacity for this hospital
    private final int capacity = (int) ConfigUtil.get("HOSPITAL", "CAPACITY");

    // the number of patients quarantined in this hospital
    private int patients = 0;

    private static Hospital hospital = new Hospital();

    public static Hospital getInstance() {
        return hospital;
    }

    // the coordinate for the first bed, which is used to set absolute coordinates for other beds
    private Point point = new Point(X, Y);

    private List<Bed> beds = new ArrayList<>();

    public int getNumberOfPatients() {
        return patients;
    }

    public List<Bed> getBeds() {
        return beds;
    }

    /**
     * @author Gan Li
     * @description adjust the size of hospital according to the capacity
     * @createTime  13/04/2021
     */
    private Hospital() {
        int column = capacity / 100;
        height = column * 6;
        for (int i = 0; i < column; i++) {

            for (int j = 0; j <= 596; j += 6) {

                Bed bed = new Bed(point.getX() + j, point.getY() + i * 6);
                beds.add(bed);
                if (beds.size() >= capacity) {
                    break;
                }
            }

        }
    }

    /**
     * @author Ethan Zhang
     * @description add a patient to this hospital
     * @createTime  13/04/2021
     * @return true if add successfully, false if fail
     */
    public boolean addPatient() {
        if (isFull()) {
            return false;
        }
        beds.get(patients).setEmpty(false);
        patients++;
        return true;
    }

    /**
     * @author Ethan Zhang
     * @description discharge a patient from this hospital
     * @createTime  13/04/2021
     */
    public void dischargePatient() {
        if (isEmpty()) {
            return;
        }
        beds.get(patients - 1).setEmpty(true);
        patients--;
    }

    /**
     * @author Ethan Zhang
     * @description return if this hospital reaches its capacity
     * @createTime  13/04/2021
     */
    public boolean isFull () {
        return patients >= capacity;
    }

    /**
     * @author Ethan Zhang
     * @description return if this hospital is empty
     * @createTime  13/04/2021
     */
    public boolean isEmpty () {
        return patients == 0;
    }

    /**
     * @author Gan Li
     * @description return the hospital's width
     * @createTime  13/04/2021
     */
    public int getWidth() {
        return width;
    }

    /**
     * @author Gan Li
     * @description return the hospital's height
     * @createTime  13/04/2021
     */
    public int getHeight() {
        return height;
    }

    /**
     * @author Gan Li
     * @description return the hospital's capacity
     * @createTime  13/04/2021
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @author Gan Li
     * @description return the hospital's remaining beds 
     * @createTime  13/04/2021
     */
    public int getBed() {
        return capacity - patients;
    }
}
