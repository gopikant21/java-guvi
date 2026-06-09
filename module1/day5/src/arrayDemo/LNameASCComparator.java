package arrayDemo;

import java.util.Comparator;

public class LNameASCComparator implements Comparator<Person> {
    @Override
    public int compare(Person o1, Person o2) {
        return o1.getLname().compareTo(o2.getLname());
    }
}
