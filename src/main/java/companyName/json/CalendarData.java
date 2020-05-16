package companyName.json;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class CalendarData {
    private List<Appointment> appointments;
}
