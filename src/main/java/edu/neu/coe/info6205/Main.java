package edu.neu.coe.info6205;

public class Main {
    public static void main(String[] args) {
        City city = new City(Virus.Viruses.COVID19);
        while (city.dayTime < 100) {
            city.printTheStateOfCity();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            city.dayPass();
        }
    }
}
