// OrderSystem.java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderSystem implements IOrderSystem {
    private List<IOrder> cart;

    public OrderSystem() {
        this.cart = new ArrayList<>();
    }

    @Override
    public void addItemToCart(IOrder order) {
        if (order != null) {
            this.cart.add(order);
        }
    }

    @Override
    public void removeItemFromCart(IOrder order) {
        if (order != null) {
            this.cart.remove(order); // Assumes Order has a proper equals/hashCode implementation
        }
    }

    @Override
    public double calculateTotalDiscountedPrice() {
        double totalDiscountedPrice = 0;
        for (IOrder order : cart) {
            double originalPrice = order.getPrice();
            double discountRate = order.getDiscountRate();
            double discountAmount = (originalPrice * discountRate) / 100.0;
            totalDiscountedPrice += (originalPrice - discountAmount);
        }
        return totalDiscountedPrice;
    }

    @Override
    public Map<String, Double> calculateCategoryDiscounts() {
        Map<String, Double> categoryDiscounts = new HashMap<>();
        for (IOrder order : cart) {
            String category = order.getCategory();
            double originalPrice = order.getPrice();
            double discountRate = order.getDiscountRate();
            double discountAmount = (originalPrice * discountRate) / 100.0;

            categoryDiscounts.put(category, categoryDiscounts.getOrDefault(category, 0.0) + discountAmount);
        }
        return categoryDiscounts;
    }

    @Override
    public Map<String, Integer> getCartItemsWithQuantities() {
        Map<String, Integer> itemQuantities = new HashMap<>();
        for (IOrder order : cart) {
            itemQuantities.put(order.getName(), itemQuantities.getOrDefault(order.getName(), 0) + 1);
        }
        return itemQuantities;
    }

    // Helper method to display cart items as per the example output
    public void displayCartItems() {
        Map<String, Integer> itemQuantities = getCartItemsWithQuantities();
        if (itemQuantities.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            System.out.println(entry.getKey() + " (" + entry.getValue() + " item" + (entry.getValue() > 1 ? "s" : "") + ")");
        }
    }

    public static void main(String[] args) {
        OrderSystem orderSystem = new OrderSystem();

        // Create order objects
        IOrder pizza = new Order("Pizza", 40);
        IOrder sandwich = new Order("Sandwich", 30);
        // IOrder coke = new Order("Coke", 5); // Example for "Cheap"
        // IOrder salad = new Order("Salad", 15); // Example for "Moderate"


        // Add items to cart
        orderSystem.addItemToCart(pizza);
        orderSystem.addItemToCart(sandwich);
        // orderSystem.addItemToCart(coke);
        // orderSystem.addItemToCart(salad);
        // orderSystem.addItemToCart(new Order("Pizza", 40)); // Adding another pizza


        // Calculate total discounted amount
        double totalAmount = orderSystem.calculateTotalDiscountedPrice();
        System.out.println("Total Amount: " + totalAmount);

        // Calculate category discounts
        Map<String, Double> categoryDiscounts = orderSystem.calculateCategoryDiscounts();
        for (Map.Entry<String, Double> entry : categoryDiscounts.entrySet()) {
            System.out.println(entry.getKey() + " Category Discount: " + entry.getValue());
        }

        // Retrieve and display cart items with quantities
        orderSystem.displayCartItems();

        System.out.println("\n--- Removing Sandwich ---");
        orderSystem.removeItemFromCart(sandwich);

        totalAmount = orderSystem.calculateTotalDiscountedPrice();
        System.out.println("Total Amount after removal: " + totalAmount);

        categoryDiscounts = orderSystem.calculateCategoryDiscounts();
        for (Map.Entry<String, Double> entry : categoryDiscounts.entrySet()) {
            System.out.println(entry.getKey() + " Category Discount: " + entry.getValue());
        }
        orderSystem.displayCartItems();
    }
}