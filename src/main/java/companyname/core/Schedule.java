package companyname.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import companyname.json.Appointment;
import companyname.json.CalendarData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Schedule {
    private final List<String> jsonFiles;
    private final Map<String, Calendar> calendarMap = new HashMap<>();
    private LocalTime openTime = LocalTime.of(8, 0, 0);
    private LocalTime closedTime = LocalTime.of(21, 0, 0);


    public Schedule(List<String> jsonFiles) {
        this.jsonFiles = jsonFiles;
        loadCalendarData();
    }

    public List<Calendar> availabilityCalendars(List<String> calendarIds, Interval period) {
        return calendarIds.stream()
                .map(id -> new Calendar(id, freeTimeSlots(id, period)).forPeriod(period))
                .collect(toList());
    }

    private void loadCalendarData() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (
                        JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                        LocalDateTime.parse(json.getAsJsonPrimitive().getAsString()))
                .create();
        List<CalendarData> calendarDataList = new ArrayList<>();
        for (String path : jsonFiles) {
            File file = new File(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(path)).getFile());
            try {
                calendarDataList.add(gson.fromJson(new FileReader(file), CalendarData.class));
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException(path + " not found");
            }
        }

        for (CalendarData calendarData : calendarDataList) {
            Map<String, List<Appointment>> calendarAppointment =
                    calendarData.getAppointments().stream()
                            .collect(Collectors.groupingBy(Appointment::getCalendar_id, Collectors.toCollection(ArrayList::new)));
            calendarMap.putAll(appointmentsToCalendar(calendarAppointment));
        }
    }

    private Map<String, Calendar> appointmentsToCalendar(Map<String, List<Appointment>> calendarAppointment) {
        return calendarAppointment.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new Calendar(
                                entry.getKey(),
                                entry.getValue()
                                        .stream()
                                        .map(appointment -> new Interval(appointment.getStart(), appointment.getEnd()))
                                        .collect(Collectors.toList()))));
    }

    private List<Interval> freeTimeSlots(String calendarId, Interval period) {
        Calendar calendar = calendarMap.get(calendarId);
        return calendar.forPeriod(period).freeSlots(openTime, closedTime);
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public void setClosedTime(LocalTime closedTime) {
        this.closedTime = closedTime;
    }
}
