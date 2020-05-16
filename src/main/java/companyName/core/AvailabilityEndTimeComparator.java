package companyName.core;

import java.util.Comparator;

public class AvailabilityEndTimeComparator implements Comparator<Calendar> {

    @Override
    public int compare(Calendar o1, Calendar o2) {
        if (o1.currentInterval().isEmpty() || o2.currentInterval().isEmpty()) {
            return 0;
        }

        int res = (o2.hasNextInterval() ? 1 : 0) - (o1.hasNextInterval() ? 1 : 0);
        if (res != 0) {
            return res;
        }

        return o2.currentInterval().get().compareEnd(o1.currentInterval().get());
    }
}
