package companyname.core;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Calendar {
    private int index = -1;

    private final String id;
    private final List<Interval> intervals;


    public Calendar(String id, List<Interval> intervals) {
        this.id = id;
        this.intervals = intervals;
    }

    public Interval nextInterval() {
        return intervals.get(++index);
    }

    public boolean hasNextInterval() {
        return (index + 1) < intervals.size();
    }

    public Optional<Interval> currentInterval() {
        return index != -1 ? Optional.of(intervals.get(index)) : Optional.empty();
    }

    public Calendar forPeriod(Interval period) {
        List<Interval> intervals = this.intervals
                .stream()
                .filter(interval -> interval.intersect(period).isPresent())
                .map(interval -> interval.cutToPeriod(period))
                .collect(Collectors.toList());
        return new Calendar(this.id, intervals);
    }

    public Map<LocalDate, List<Interval>> dayIntervalsMap() {
        return intervals.stream().collect(
                Collectors.groupingBy(Interval::getLocalDate, LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calendar that = (Calendar) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public List<Interval> freeSlots(LocalTime openTime, LocalTime closedTime) {
        List<Interval> freeSlots = new ArrayList<>();
        dayIntervalsMap().forEach((day, intervals) -> freeSlots.addAll(findFreeIntervalsForADay(intervals, openTime, closedTime)));
        return freeSlots;
    }

    private List<Interval> findFreeIntervalsForADay(List<Interval> intervals, LocalTime openTime, LocalTime closedTime) {
        List<Interval> freeIntervals = new ArrayList<>();

        intervals.get(0).fromPointToStart(openTime).ifPresent(freeIntervals::add);
        for (int i = 0; i < intervals.size() - 1; i++) {
            intervals.get(i).between(intervals.get(i + 1)).ifPresent(freeIntervals::add);
        }
        intervals.get(intervals.size() - 1).fromFinishToPoint(closedTime).ifPresent(freeIntervals::add);

        return freeIntervals;
    }
}
