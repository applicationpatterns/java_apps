import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.ap.compare.app.model.Customer;
import org.ap.compare.app.model.Order;
import org.ap.compare.app.model.OrderItem;
import org.ap.compare.app.utils.JsonHelper;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestJsonHelper {

    // ProTip: use this utility to generate a pilot JSON file
    @Test
    void testWriteCustomer() throws IOException {
        Customer customer = Customer.builder()
                .customerId(4)
                .customerName("Around the Horn")
                .address("120 Hanover Sq.")
                .city("London")
                .country("UK")
                .build();

        Order order1 = Order.builder()
                .orderId(10355)
                .customerId(customer.getCustomerId())
                .date(Date.valueOf("1996-11-15"))
                .build();
        {
            List<OrderItem> orderItemList = new ArrayList<>();
            OrderItem oi = null;
            oi = OrderItem.builder()
                    .orderItemId(285).productId(24).productQty(25)
                    .orderId(order1.getOrderId())
                    .customerId(customer.getCustomerId())
                    .build();
            orderItemList.add(oi);
            oi = OrderItem.builder()
                    .orderItemId(286).productId(57).productQty(25)
                    .orderId(order1.getOrderId())
                    .customerId(customer.getCustomerId())
                    .build();
            orderItemList.add(oi);
            order1.setOrderItemList(orderItemList);
        }


        Order order2 = Order.builder()
                .orderId(10383)
                .customerId(customer.getCustomerId())
                .date(Date.valueOf("1996-12-16"))
                .build();
        {
            List<OrderItem> orderItemList = new ArrayList<>();
            OrderItem oi = null;
            oi = OrderItem.builder()
                    .orderItemId(358).productId(13).productQty(20)
                    .orderId(order1.getOrderId())
                    .customerId(customer.getCustomerId())
                    .build();
            orderItemList.add(oi);
            oi = OrderItem.builder()
                    .orderItemId(359).productId(50).productQty(15)
                    .orderId(order1.getOrderId())
                    .customerId(customer.getCustomerId())
                    .build();
            orderItemList.add(oi);
            oi = OrderItem.builder()
                    .orderItemId(360).productId(56).productQty(20)
                    .orderId(order1.getOrderId())
                    .customerId(customer.getCustomerId())
                    .build();
            orderItemList.add(oi);
            order2.setOrderItemList(orderItemList);
        }

        List<Order> orders = Arrays.asList(order1, order2);
        customer.setOrderList(orders);


        String jsonString = JsonHelper.writeCustomer(customer);
        Customer resurrectedCustomer = JsonHelper.readCustomer(jsonString);
        assertThat(resurrectedCustomer).isEqualTo(customer);

        // created this file by copying jsonString returned by writeCustomer
        String filename = "src/test/resources/TestJsonHelper_CustomerList1.json";
        resurrectedCustomer = JsonHelper.readCustomerFromFile(filename);
        assertThat(resurrectedCustomer).isEqualTo(customer);
    }

    @SneakyThrows
    @Test
    void readCustomerFromFileWithMultipleItems() {
        String filename = "src/test/resources/TestJsonHelper_CustomerList2.json";
        List<Customer> c = JsonHelper.readCustomerListFromFile(filename);
        System.out.println("got back this:\n"+c);
    }
}
