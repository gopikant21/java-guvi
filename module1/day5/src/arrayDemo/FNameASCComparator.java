package arrayDemo;

import java.util.Comparator;

public class FNameASCComparator implements Comparator<Person> {
    @Override
    public int compare(Person o1, Person o2) {
        return o1.getFname().compareTo(o2.getFname());
    }
}
