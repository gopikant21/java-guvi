package arrayDemo.product;

import java.util.Comparator;

public class DiscountComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return (int)(p1.getDiscount()- p2.getDiscount());
    }
}
