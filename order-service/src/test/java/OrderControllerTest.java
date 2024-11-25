import com.max.controller.OrderController;
import com.max.model.Order;
import com.max.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Test
    void send_ShouldReturnSavedOrder() {
        Order inputOrder = Order.builder()
                .customerName("John Doe")
                .amount(100.0)
                .build();

        Order savedOrder = Order.builder()
                .id("64f337673890116e66f05f7e")
                .customerName("John Doe")
                .amount(100.0)
                .build();

        when(orderService.send(inputOrder)).thenReturn(savedOrder);

        Order result = orderController.send(inputOrder);

        assertNotNull(result);
        assertEquals(savedOrder.getId(), result.getId());
        assertEquals(savedOrder.getCustomerName(), result.getCustomerName());
        assertEquals(savedOrder.getAmount(), result.getAmount());
        verify(orderService, times(1)).send(inputOrder);
    }

}