import com.max.kafka.KafkaProducer;
import com.max.model.Order;
import com.max.repository.OrderRepository;
import com.max.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @Test
    void send_shouldSaveOrderAndSendToKafka() {
        Order inputOrder = Order.builder()
                .customerName("Jane Doe")
                .amount(200.0)
                .build();

        Order savedOrder = Order.builder()
                .id("64f337673890116e66f05f7e")
                .customerName("Jane Doe")
                .amount(200.0)
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.send(inputOrder);

        assertNotNull(result);
        assertEquals(savedOrder.getId(), result.getId());
        assertEquals(savedOrder.getCustomerName(), result.getCustomerName());
        assertEquals(savedOrder.getAmount(), result.getAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(kafkaProducer, times(1)).send(savedOrder);
    }

    @Test
    void send_shouldGenerateIdWhenIdIsNull() {
        Order inputOrder = Order.builder()
                .customerName("Jane Doe")
                .amount(200.0)
                .build();

        Order savedOrder = Order.builder()
                .id("64f337673890116e66f05f7e")
                .customerName("Jane Doe")
                .amount(200.0)
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.send(inputOrder);

        assertNotNull(result.getId());
        assertEquals(savedOrder.getId(), result.getId());
    }

}