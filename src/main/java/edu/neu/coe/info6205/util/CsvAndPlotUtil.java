package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.City;
import edu.neu.coe.info6205.PersonPool;
import edu.neu.coe.info6205.State;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.Destination;
import tech.tablesaw.io.csv.CsvWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class CsvAndPlotUtil {

    private static final List<Integer> Day = new ArrayList<>();
    private static final List<Integer> Normal = new ArrayList<>();
    private static final List<Integer> Shadow = new ArrayList<>();
    private static final List<Integer> Symptomatic = new ArrayList<>();
    private static final List<Integer> Confirmed = new ArrayList<>();
    private static final List<Integer> Quarantined = new ArrayList<>();
    private static final List<Integer> Death = new ArrayList<>();
    private static final List<Integer> Recovered = new ArrayList<>();
    private static final List<Integer> Destroyed = new ArrayList<>();

    /**
     * @author Gan Li
     * @description record the state of city
     * @createTime  19/04/2021
     */
    public static void record() throws IOException {
        Day.add(City.dayTime);
        Normal.add(PersonPool.getInstance().getPeopleSize(State.NORMAL));
        Shadow.add(PersonPool.getInstance().getPeopleSize(State.SHADOW));
        Symptomatic.add(PersonPool.getInstance().getPeopleSize(State.SYMPTOMATIC));
        Confirmed.add(PersonPool.getInstance().getPeopleSize(State.CONFIRMED));
        Quarantined.add(PersonPool.getInstance().getPeopleSize(State.QUARANTINED));
        Death.add(PersonPool.getInstance().getPeopleSize(State.DEATH));
        Recovered.add(PersonPool.getInstance().getPeopleSize(State.RECOVERED));
        Destroyed.add(PersonPool.getInstance().getPeopleSize(State.DESTROYED));

        Table table = createTable();
        write(table, "Results.csv");
        System.out.println(table.print());
    }

    /**
     * @author Gan Li
     * @description create table for recoding
     * @createTime  19/04/2021
     */
    private static Table createTable(){
        Integer [] day = new Integer[Day.size()];
        IntColumn c1 = IntColumn.create("Day", Day.toArray(day));
        Integer [] normal = new Integer[Normal.size()];
        IntColumn c2 = IntColumn.create("Normal", Normal.toArray(normal));
        Integer [] shadow = new Integer[Shadow.size()];
        IntColumn c3 = IntColumn.create("Shadow", Shadow.toArray(shadow));
        Integer [] symptomatic = new Integer[Symptomatic.size()];
        IntColumn c4 = IntColumn.create("Symptomatic", Symptomatic.toArray(symptomatic));
        Integer [] confirmed = new Integer[Confirmed.size()];
        IntColumn c5 = IntColumn.create("Confirmed", Confirmed.toArray(confirmed));
        Integer [] quarantined = new Integer[Quarantined.size()];
        IntColumn c6 = IntColumn.create("Quarantined", Quarantined.toArray(quarantined));
        Integer [] death = new Integer[Death.size()];
        IntColumn c7 = IntColumn.create("Death", Death.toArray(death));
        Integer [] recovered = new Integer[Recovered.size()];
        IntColumn c8 = IntColumn.create("Recovered", Recovered.toArray(recovered));
        Integer [] destroyed = new Integer[Destroyed.size()];
        IntColumn c9 = IntColumn.create("Destroyed", Destroyed.toArray(destroyed));
        return Table.create(c1, c2, c3, c4, c5, c6, c7, c8, c9);
    }

    /**
     * @author Gan Li
     * @description write result into csv
     * @createTime  19/04/2021
     */
    private static void write(Table table, String path) throws IOException {
        CsvWriter writer = new CsvWriter();
        File file = new File(path);
        Destination destination = new Destination(file);
        writer.write(table, destination);
    }
}
