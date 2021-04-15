package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

public class Hospital {
    public static final int X = 50;
    public static final int Y = 800;
    private int width = 600;
    private int height;

    private final int capacity = (int) ConfigUtil.get("HOSPITAL", "CAPACITY");
    private int patients = 0;

    private static Hospital hospital = new Hospital();

    public static Hospital getInstance() {
        return hospital;
    }

    private Point point = new Point(X, Y);//第一个床位所在坐标，用于给其他床位定绝对坐标
    private List<Bed> beds = new ArrayList<>();

    public List<Bed> getBeds() {
        return beds;
    }

    // adjust the size of hospital according to the capacity
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

    public boolean addPatient() {
        if (isFull()) {
            return false;
        }
        beds.get(patients).setEmpty(false);
        patients++;
        return true;
    }

    public void dischargePatient() {
        if (isEmpty()) {
            return;
        }
        beds.get(patients - 1).setEmpty(true);
        patients--;
    }

    public boolean isFull () {
        return patients >= capacity;
    }

    public boolean isEmpty () {
        return patients == 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getBed() {
        return capacity - patients;
    }
}
