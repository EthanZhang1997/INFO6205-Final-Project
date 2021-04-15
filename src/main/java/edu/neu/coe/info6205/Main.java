package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Virus.Viruses v = Virus.Viruses.COVID19;
        City city = new City(v);
        initPanel(v.toString());
    }

    private static void initPanel(String v) {
        MyPanel p = new MyPanel();
        Thread panelThread = new Thread(p);
        JFrame frame = new JFrame();
        frame.add(p);
        frame.setSize((int) ConfigUtil.get("CITY", "WIDTH"), (int) ConfigUtil.get("CITY", "HEIGHT") + Hospital.getInstance().getHeight() + 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle(v + " Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelThread.start();
    }
}
