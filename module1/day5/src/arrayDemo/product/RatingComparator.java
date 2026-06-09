package arrayDemo.product;

import java.util.Comparator;

public class RatingComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p1.getRating()- p2.getRating();
    }
}
