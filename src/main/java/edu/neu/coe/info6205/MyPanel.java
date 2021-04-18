package edu.neu.coe.info6205;

import edu.neu.coe.info6205.util.ConfigUtil;
import edu.neu.coe.info6205.util.CsvAndPlotUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyPanel extends JPanel implements Runnable{

    public MyPanel() {
        super();
        this.setBackground(new Color(0x444444));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // draw hospital
        g.setColor(new Color(0xFFFFFF));
        g.drawRect(Hospital.X, Hospital.Y, Hospital.getInstance().getWidth(), Hospital.getInstance().getHeight());
        g.setFont(new Font("Helvetica", Font.BOLD, 16));
        g.setColor(new Color(0xFFFFFF));
        g.drawString("Hospital", (Hospital.X + Hospital.getInstance().getWidth()) / 2, Hospital.Y - 16);

        // draw bed
        List<Bed> beds = Hospital.getInstance().getBeds();
        for (Bed b : beds){
            if (b.isEmpty()) {
                g.setColor(new Color(0x14E02F));
            }
            else
                g.setColor(new Color(0xE01414));

            g.fillOval(b.getX(), b.getY(), 3, 3);
        }

        // draw people
        List<Person> people = PersonPool.getInstance().getPersonList();
        if (people == null) {
            return;
        }
        for (Person p : people) {
            switch (p.getState()) {
                case 0: {
                    g.setColor(new Color(0xdddddd));
                    break;
                }
                case 1: {
                    g.setColor(new Color(0xC69E0F));
                    break;
                }
                case 2: {
                    g.setColor(new Color(0xC60F6E));
                    break;
                }
                case 3: {
                    g.setColor(new Color(0xE01425));
                    break;
                }
                case 5: {
                    g.setColor(Color.black);
                    break;
                }
            }
            g.fillOval(p.getPoint().getX(), p.getPoint().getY(), 3, 3);
        }

        int y = (int) ConfigUtil.get("CITY", "HEIGHT") + Hospital.getInstance().getHeight();
        int interval = 30;

        g.setColor(new Color(0xdddddd));
        g.drawString("Healthy population：" + PersonPool.getInstance().getPeopleSize(State.NORMAL), 50, y + interval);
        g.setColor(new Color(0xC69E0F));
        g.drawString("Shadow population：" + PersonPool.getInstance().getPeopleSize(State.SHADOW), 50, y + 2 * interval);
        g.setColor(new Color(0xC60F6E));
        g.drawString("Symptomatic population：" + PersonPool.getInstance().getPeopleSize(State.SYMPTOMATIC), 50, y + 3 * interval);
        g.setColor(new Color(0xE01425));
        g.drawString("Confirmed population：" + (PersonPool.getInstance().getPeopleSize(State.CONFIRMED) + PersonPool.getInstance().getPeopleSize(State.QUARANTINED)), 50, y + 4 * interval);
        g.setColor(Color.black);
        g.drawString("Deaths：" + PersonPool.getInstance().getPeopleSize(State.DEATH), 50, y + 5 * interval);
        g.setColor(new Color(0x14E0E0));
        g.drawString("Patients at hospital：" + PersonPool.getInstance().getPeopleSize(State.QUARANTINED), 80 + (Hospital.X + Hospital.getInstance().getWidth()) / 2, y + interval);
        g.setColor(new Color(0x14E02F));
        g.drawString("Available bed at hospital：" + Hospital.getInstance().getBed(), 80 + (Hospital.X + Hospital.getInstance().getWidth()) / 2, y + 2 * interval);
        g.setColor(new Color(0x14A6E0));
        g.drawString("Recovered population：" + PersonPool.getInstance().getPeopleSize(State.RECOVERED), 80 + (Hospital.X + Hospital.getInstance().getWidth()) / 2, y + 3 * interval);

        int needBeds = PersonPool.getInstance().getPeopleSize(State.CONFIRMED);

        g.setColor(new Color(0xD2710A));
        g.drawString("Bed needed：" + needBeds, 80 + (Hospital.X + Hospital.getInstance().getWidth()) / 2, y + 4 * interval);

        g.setColor(new Color(0xdddddd));
        g.drawString("Day: ：" + City.dayTime, 20, 20);

        g.setColor(new Color(0xdddddd));
        g.drawString("Total population: ：" + (int) (ConfigUtil.get("CITY", "PERSON_SIZE") + ConfigUtil.get("CITY", "INITIAL_PATIENTS")), 20, 20 + interval);
    }

    public Timer timer = new Timer();

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                CsvAndPlotUtil.record();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MyPanel.this.repaint();
            List<Person> people = PersonPool.getInstance().getPersonList();
            for (Person p : people) {
                p.action();
            }
            City.dayTime++;
        }
    }

    @Override
    public void run() {
        timer.schedule(new MyTimerTask(), 4000, 100);
    }
}
