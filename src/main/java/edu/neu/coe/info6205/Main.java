package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;
import edu.neu.coe.info6205.util.CsvAndPlotUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        // create a new city
        City city = new City();

        // add initial citizens to the city
        PersonPool.getInstance().addInitialCitizens();

        // add initial patients to the city
        Virus.Viruses v = Virus.Viruses.COVID19;
        PersonPool.getInstance().addInitialPatients(v);

        // initialize the panel
        initPanel(v.toString());
    }

    private static void initPanel(String v) {
        MyPanel p = new MyPanel();
        Thread panelThread = new Thread(p);
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout ());
        frame.add(p);

        JButton jbStart = new JButton("Start");
        JButton jbEnd = new JButton("Stop");
        p.add(jbStart);
        p.add(jbEnd);

        frame.setSize((int) ConfigUtil.get("CITY", "WIDTH"), (int) ConfigUtil.get("CITY", "HEIGHT") + Hospital.getInstance().getHeight() + 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle(v + " Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // click button to restart simulation
        jbStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.setPaused(false);
                System.out.println("Simulation continued!!!");
            }
        });

        // click button to stop simulation
        jbEnd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.setPaused(true);
                System.out.println("Simulation stopped!!!");
            }
        });

        panelThread.start();

        // stop simulation after 100 days
        while (true) {
            if (City.dayTime >= 100) {
                p.setPaused(true);
            }
        }
    }
}
