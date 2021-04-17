package edu.neu.coe.info6205;

/**
 * @author Ethan Zhang
 * @description class for points (coordinates)
 * @createTime  13/04/2021
 */
public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @author Ethan Zhang
     * @description move from the former coordinate
     * @createTime  13/04/2021
     * @param x the movement on x-axis, y the movement on y-axis
     */
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
