import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    Restaurant restaurant;
    OrderService orderService;


    @BeforeEach
    public void setup() {
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant = new Restaurant("Amelie's cafe", "Chennai", openingTime, closingTime);
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);
        orderService = new OrderService();
    }

    public class OrderService {
        public int calculateOrderValue(Restaurant restaurant, String... itemNames) {
            int totalOrderValue = 0;
            for (String itemName : itemNames) {
                Item item = restaurant.findItemByName(itemName);
                if (item != null) {
                    totalOrderValue += item.getPrice();
                }
            }
            return totalOrderValue;
        }
    }

    @Test
    public void calculate_order_value_for_multiple_items() {
        int orderValue = orderService.calculateOrderValue(restaurant, "Sweet corn soup", "Vegetable lasagne");
        assertEquals(388, orderValue);
    }

    @Test
    public void calculate_order_value_for_single_item() {
        int orderValue = orderService.calculateOrderValue(restaurant, "Vegetable lasagne");
        assertEquals(269, orderValue);
    }

    @Test
    public void calculate_order_value_for_no_items() {
        int orderValue = orderService.calculateOrderValue(restaurant);
        assertEquals(0, orderValue);
    }

    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time(){
        try (MockedStatic<LocalTime> mockedTime = Mockito.mockStatic(LocalTime.class)) {
            mockedTime.when(LocalTime::now).thenReturn(LocalTime.parse("12:00:00"));

            assertTrue(restaurant.isRestaurantOpen());
        }
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time(){
        //WRITE UNIT TEST CASE HERE
        try (MockedStatic<LocalTime> mockedTime = Mockito.mockStatic(LocalTime.class)) {
            mockedTime.when(LocalTime::now).thenReturn(LocalTime.parse("09:00:00"));

            assertFalse(restaurant.isRestaurantOpen());
        }

    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1() {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie", 319);
        assertEquals(initialMenuSize + 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize - 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(itemNotFoundException.class, () -> restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}