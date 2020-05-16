import companyname.core.Interval;
import companyname.core.Schedule;
import companyname.core.ScheduleProcessor;

import java.util.List;

public class AssignmentBootstrap {

    public static void main(String[] args) {

        final List<String> CALENDARS = List.of(
                "48644c7a-975e-11e5-a090-c8e0eb18c1e9",
                "48cadf26-975e-11e5-b9c2-c8e0eb18c1e9",
                "452dccfc-975e-11e5-bfa5-c8e0eb18c1e9");
        final int DURATION = 30;
        final Interval PERIOD = new Interval("2019-03-01T13:00:00", "2019-05-11T15:30:00");


        Schedule schedule = new Schedule(List.of("DannyBoy.json", "EmmaWin.json", "JoannaHef.json"));
        ScheduleProcessor scheduleProcessor = new ScheduleProcessor(schedule);

        scheduleProcessor
                .findAvailableTime(CALENDARS, DURATION, PERIOD)
                .forEach(System.out::println);
    }
}
