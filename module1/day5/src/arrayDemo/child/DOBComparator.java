package arrayDemo.child;

import java.util.Comparator;

public class DOBComparator implements Comparator<Child> {
    @Override
    public int compare(Child c1, Child c2) {
        String[] a =  c1.dob.split("-");
        String[] b = c2.dob.split("-");

        int day1 = Integer.parseInt(a[0]);
        int day2 = Integer.parseInt(b[0]);
        int month1 = Integer.parseInt(a[1]);
        int month2 = Integer.parseInt(b[1]);
        int year1 = Integer.parseInt(a[2]);
        int year2 = Integer.parseInt(b[2]);

        if(year1 != year2) return year1-year2;
        if(month1 != month2) return month1-month2;

        return day1-day2;
    }
}
