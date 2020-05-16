package companyName.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.MINUTES;

public class Interval {
    private final LocalDateTime start;
    private final LocalDateTime finish;

    public Interval(LocalDateTime start, LocalDateTime finish) {
        this.start = start;
        this.finish = finish;
    }

    public Interval(String start, String finish) {
        this.start = LocalDateTime.parse(start);
        this.finish = LocalDateTime.parse(finish);
    }

    public Interval(Interval interval) {
        this.start = interval.start;
        this.finish = interval.finish;
    }

    public Optional<Interval> intersect(Interval interval, long duration) {
        if (!longerThanOrEqual(duration)) {
            return Optional.empty();
        }

        if (!interval.longerThanOrEqual(duration) || !hasIntersection(interval)) {
            return Optional.empty();
        }

        return Optional.of(new Interval(
                maxDate(start, interval.start),
                minDate(finish, interval.finish)));
    }

    public Optional<Interval> intersect(Interval interval) {
        return intersect(interval, duration());
    }

    public Interval cutToPeriod(Interval period) {
        if (start.compareTo(period.start) < 0) {
            return new Interval(period.start, finish);
        } else if (finish.compareTo(period.finish) > 0) {
            return new Interval(start, period.finish);
        }

        return this;
    }

    public LocalDate getLocalDate() {
        return start.toLocalDate();
    }

    public Optional<Interval> between(Interval nextInterval) {
        if (nextInterval.start.compareTo(finish) <= 0 && nextInterval.finish.compareTo(start) >= 0) {
            return Optional.empty();
        }
        LocalDateTime currentEnd = finish;
        LocalDateTime nextStart = nextInterval.start;
        if (currentEnd.compareTo(nextStart) != 0) {
            return Optional.of(new Interval(currentEnd, nextStart));
        }

        return Optional.empty();
    }

    public Optional<Interval> fromPointToStart(LocalTime point) {
        if (start.toLocalTime().compareTo(point) > 0) {
            return Optional.of(new Interval(start.with(point), start));
        }

        return Optional.empty();
    }

    public Optional<Interval> fromFinishToPoint(LocalTime point) {
        if (finish.toLocalTime().compareTo(point) < 0) {
            return Optional.of(new Interval(finish, finish.with(point)));
        }

        return Optional.empty();
    }

    private LocalDateTime maxDate(LocalDateTime ldt1, LocalDateTime ldt2) {
        return ldt1.compareTo(ldt2) > 0 ? ldt1 : ldt2;
    }

    private LocalDateTime minDate(LocalDateTime ldt1, LocalDateTime ldt2) {
        return ldt1.compareTo(ldt2) > 0 ? ldt2 : ldt1;
    }

    private long duration() {
        return MINUTES.between(this.start, this.finish);
    }

    private boolean longerThanOrEqual(long duration) {
        return duration <= duration();
    }

    private boolean hasIntersection(Interval interval) {
        return interval.start.compareTo(finish) < 0 && interval.finish.compareTo(start) > 0;
    }

    @Override
    public String toString() {
        return "{" +
                "start=" + start +
                ", finish=" + finish +
                '}';
    }

    public int compareEnd(Interval interval) {
        return interval.finish.compareTo(finish);
    }
}
