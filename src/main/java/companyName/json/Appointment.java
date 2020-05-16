package companyName.json;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Appointment {
    private String id;
    private String calendar_id;
    private LocalDateTime start;
    private LocalDateTime end;
}
