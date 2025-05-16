
import java.time.LocalDate;
import java.util.*;

public class WayCategory {
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
    LocalDate DateModified;


    public WayCoupon(String CategoryName,String CouponName,LocalDate DateModified){
        this.CategoryName=CategoryName;
        this.CouponName=CouponName;
        this.DateModified=DateModified;
    }
}

public class Main {
    public static List<WayCoupon> couponsInput;
    public static List<WayCategory> categoriesInput;
    public static HashMap<String,List<WayCoupon>>categoryCouponMapping;
    public static HashMap<String,String> categoryParentMapping;
    public static HashMap<String,List<WayCoupon>> couponCache;
    public static HashMap<String,List<LocalDate>>couponDateMapping;
    public static HashMap<String,LocalDate>currentLatestCouponDate;

    public static void fillCategoryCouponMapping(){
        for (WayCoupon currentCoupon : couponsInput) {
            if(!categoryCouponMapping.containsKey(currentCoupon.CategoryName)){
                List<WayCoupon>couponList = new ArrayList<>();
                categoryCouponMapping.put(currentCoupon.CategoryName,couponList);
            }else {
                categoryCouponMapping.get(currentCoupon.CategoryName).add(currentCoupon);
            }
        }
    }

    public static void fillCategoryParentMapping(){
        for(WayCategory category:categoriesInput){
            categoryParentMapping.put(category.CategoryName,category.CategoryParentName);
        }
    }
    public static void fillCouponDateMapping(){
        for(WayCoupon currentCoupon:couponsInput){
            if(!couponDateMapping.containsKey(currentCoupon.CouponName)){
                List<LocalDate>couponDates=new ArrayList<>();
                couponDates.add(currentCoupon.DateModified);
                couponDateMapping.put(currentCoupon.CouponName,couponDates);
            }else{
                couponDateMapping.get(currentCoupon.CouponName).add(currentCoupon.DateModified);
            }
        }
    }


    public static void fillCurrentLatestCouponDate(LocalDate currentDate){
        for(Map.Entry<String,List<WayCoupon>>entry:couponCache.entrySet()){
            List<WayCoupon> couponList = entry.getValue();
            Collections.sort(couponList, new Comparator<WayCoupon>() {
                @Override
                public int compare(WayCoupon c1, WayCoupon c2) {
                    return c1.DateModified.compareTo(c2.DateModified);
                }
            });
            int low=0,high=dates.size();
            while (low < high) {
                int mid = low + (high - low) / 2;
                if (couponList.get(mid).DateModified.isAfter(currentDate)) {
                    high = mid;
                } else {
                    low = mid + 1;
                }
            }
            if(low==0){
                currentLatestCouponDate.put(entry.getKey(), LocalDate.MIN);
            }else{
                currentLatestCouponDate.put(entry.getKey(), dates.get(low-1));
            }
        }
    }

    public static List<WayCoupon> parentCoupon(String currentCategory) {
        return parentCouponHelper(currentCategory, new HashSet<>());
    }
    private static List<WayCoupon> parentCouponHelper(String currentCategory, Set<String> visited) {
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
        List<WayCoupon> parentCatCoupon = parentCouponHelper(parentCategory, visited);
        couponCache.put(currentCategory, parentCatCoupon);
        return parentCatCoupon;
    }

    public static void main(String[] args) {
        categoryCouponMapping = new HashMap<>();
        categoryParentMapping = new HashMap<>();
        couponCache = new HashMap<>();
        couponDateMapping = new HashMap<>();
        currentLatestCouponDate = new HashMap<>();
        couponsInput = Arrays.asList(
                new WayCoupon("Comforter Sets", "Comforters Sale", LocalDate.parse("2021-01-01")),
                new WayCoupon("Comforter Sets", "Cozy Comforter Coupon", LocalDate.parse("2020-01-01")),
                new WayCoupon("Bedding", "Best Bedding Bargains",LocalDate.parse("2019-01-01")),
                new WayCoupon("Bedding", "Savings on Bedding",LocalDate.parse( "2019-01-01")),
                new WayCoupon("Bed & Bath", "Low price for Bed & Bath",LocalDate.parse("2018-01-01")),
                new WayCoupon("Bed & Bath", "Bed & Bath extravaganza",LocalDate.parse("2019-01-01")),
                new WayCoupon("Bed & Bath", "Big Savings for Bed & Bath",LocalDate.parse("2030-01-01"))
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
        fillCouponDateMapping();
        fillCurrentLatestCouponDate(LocalDate.now());

        for (WayCategory currentCategory : categoriesInput) {
            if (!couponCache.containsKey(currentCategory.CategoryName)) {
                parentCoupon(currentCategory.CategoryName);
            }
        }
        System.out.println("Final category → coupon mapping:");
        for (Map.Entry<String, String> entry : couponCache.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        for(Map.Entry<String,List<LocalDate>> entry:couponDateMapping.entrySet()){
            List<LocalDate> dates = entry.getValue();
            System.out.println("The key is "+entry.getKey());
            for(LocalDate localDate:dates){
                System.out.print("d"+localDate);
            }
        }
        for(Map.Entry<String,LocalDate> entry:currentLatestCouponDate.entrySet()){
            System.out.println(entry.getKey()+entry.getValue());
        }

    }

}
