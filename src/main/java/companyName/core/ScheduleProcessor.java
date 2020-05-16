package companyName.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ScheduleProcessor {
    private List<Calendar> calendars;
    private Schedule schedule;

    public ScheduleProcessor(Schedule schedule) {
        this.schedule = schedule;
    }

    /**
     * 1. leave only intervals suitable for period - schedule.availabilityCalendars(...)
     * 2. buildIntervals() to find intersection between - buildIntervals()
     * 3. findIntersection() for current interval of each person and add to result list if exist
     * 4. if more intervals left GOTO 1st
     *
     * @param duration the duration of meeting in minutes
     * @param period to look for available time
     * @return
     */
    public List<Interval> findAvailableTime(List<String> calendarIds, int duration, Interval period) {
        this.calendars = schedule.availabilityCalendars(calendarIds, period);
        List<Interval> result = new ArrayList<>();

        while (hasIntervalsForProcessing()) {
            findIntersection(buildIntervals(), duration).ifPresent(result::add);
        }

        return result;
    }

    private Optional<Interval> findIntersection(List<Interval> peopleIntervals, int duration) {
        Interval result = new Interval(peopleIntervals.get(0));

        for (int i = 1; i < peopleIntervals.size(); i++) {
            Optional<Interval> interval = result.intersect(peopleIntervals.get(i), duration);
            if (interval.isEmpty()) {
                return Optional.empty();
            } else {
                result = interval.get();
            }
        }

        return Optional.of(result);
    }

    private List<Interval> buildIntervals() {
        // get next interval for smallest one and keep current for others
        calendars.sort(new AvailabilityEndTimeComparator());

        return Stream.concat(
                Stream.of(calendars.get(0).nextInterval()),
                calendars
                        .stream()
                        .skip(1)
                        .map(calendar -> calendar.currentInterval().orElseGet(calendar::nextInterval))
        ).collect(toList());
    }

    private boolean hasIntervalsForProcessing() {
        return calendars.stream().anyMatch(Calendar::hasNextInterval);
    }

    public void updateSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
