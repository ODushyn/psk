Example of how to run program is in the `main` method. The code should look readable and self explaining.

I've made the main focus on code readability, so it should be easy and straightforward to extend/modify code in the future.

# Short description


There are 4 core object that encapsulates all business logic. 
They all are "active" objects and not just data structures that keeps data that then processed by different objects.

**Objects expose no data to external world but just API**
That is a coding style I preferred for particular task.

1. Interval. Abstraction that represents interval of time with convenient API like:
  - intersect(Interval interval, long duration) return intersection of two intervals
  - between(Interval nextInterval)
  and so on.
   
2. Calendar. Abstraction that represents person calendar(basically list of intervals). 
This object provides API to iterate thorough intervals or group them according to business logic.
 
3. Schedule. Abstraction that represents set of calendars and people they belong to.
The only way to create Schedule is to load it from JSON files for now. That is what exactly needed for this task.
The only method it exposes `availabilityCalendars(List<String> calendarIds, Interval period)` returns list of people
calendars with only peoples free time intervals. 

4. ScheduleProcessor. It is responsible for manipulating the schedule and contains main algorithm. 
For now there is only one method as for the task `findAvailableTime(List<String> calendarIds, int duration, Interval period)`

## NOTE
I have used only `appointments` arrays from test JSON files to solve the task. 
I did not quite understand how TimeSlotTypes can affect the particular logic. 
However, I believe based on code structure that it should be quite easy to add any optional parameters to the program if needed. 