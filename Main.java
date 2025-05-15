import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//public class WayCategory {
    String CategoryName;
    String CategoryParentName;

    public WayCategory(String CategoryName,String CategoryParentName){
        this.CategoryName=CategoryName;
        this.CategoryParentName=CategoryParentName;
    }
}
public class WayCoupon {
    String CategoryName;
    String CouponName;

    public WayCoupon(String CategoryName,String CouponName){
        this.CategoryName=CategoryName;
        this.CouponName=CouponName;

    }
}

public class Main {
    public static List<WayCoupon> couponsInput;
    public static List<WayCategory> categoriesInput;
    public static HashMap<String,String>categoryCouponMapping;
    public static HashMap<String,String> categoryParentMapping;
    public static HashMap<String,String> couponCache;

    public static void fillCategoryCouponMapping(){
        for (WayCoupon currentCoupon : couponsInput) {
            categoryCouponMapping.put(currentCoupon.CategoryName, currentCoupon.CouponName);
        }
    }
    public static void fillCategoryParentMapping(){
        for(WayCategory category:categoriesInput){
            categoryParentMapping.put(category.CategoryName,category.CategoryParentName);
        }
    }
    public static String parentCoupon(String currentCategory) {
        return parentCouponHelper(currentCategory, new HashSet<>());
    }
    private static String parentCouponHelper(String currentCategory, Set<String> visited) {
        if (visited.contains(currentCategory)) {
            throw new RuntimeException("Cycle detected involving category: " + currentCategory);
        }

        visited.add(currentCategory);

        if (categoryCouponMapping.containsKey(currentCategory)) {
            couponCache.put(currentCategory, categoryCouponMapping.get(currentCategory));
            return categoryCouponMapping.get(currentCategory);
        }

        String parentCategory = categoryParentMapping.get(currentCategory);
        if (parentCategory == null) {
            return null; // Or handle as "no coupon found"
        }

        String parentCatCoupon = parentCouponHelper(parentCategory, visited);
        couponCache.put(currentCategory, parentCatCoupon);
        return parentCatCoupon;
    }
    public static void main(String[] args) {
        categoryCouponMapping = new HashMap<>();
        categoryParentMapping = new HashMap<>();
        couponCache = new HashMap<>();

        couponsInput = Arrays.asList(
                new WayCoupon("Comforter Sets", "Comforters Sale"),
                new WayCoupon("Bedding", "Savings on Bedding"),
                new WayCoupon("Bed & Bath", "Low price for Bed & Bath")
        );
        categoriesInput = Arrays.asList(
                new WayCategory("Comforter Sets", "Bedding"),
                new WayCategory("Bedding", "Bed & Bath"),
                new WayCategory("Bed & Bath", null),
                new WayCategory("Soap Dispensers", "Bathroom Accessories"),
                new WayCategory("Bathroom Accessories", "Bed & Bath"),
                new WayCategory("Toy Organizers", "Baby And Kids"),
                new WayCategory("Baby And Kids", null)
        );

        fillCategoryCouponMapping();
        fillCategoryParentMapping();

        for (WayCategory currentCategory : categoriesInput) {
            if (!couponCache.containsKey(currentCategory.CategoryName)) {
                parentCoupon(currentCategory.CategoryName);
            }
        }
        System.out.println("Final category → coupon mapping:");
        for (Map.Entry<String, String> entry : couponCache.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }

}