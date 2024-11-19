import com.fasterxml.jackson.databind.ObjectMapper;
import com.max.OrderServiceApp;
import com.max.controller.OrderController;
import com.max.kafka.KafkaProducer;
import com.max.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OrderServiceApp.class)
@AutoConfigureMockMvc
public class OrderControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private OrderController orderController;

    private Order order;

    @BeforeEach
    public void setup() {
        order = new Order();
        order.setId("12345");
        order.setCustomerName("John");
        order.setAmount(100.0);
    }

    @Test
    public void testSendOrderWithIdSuccess() throws Exception {
        Order order = new Order("12345", "John", 100.0);

        String orderJson = objectMapper.writeValueAsString(order);

        ResultActions result = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.customerName").value(order.getCustomerName()));

        verify(kafkaProducer).send(argThat(orderArgument ->
                orderArgument.getId().equals("12345") &&
                        orderArgument.getCustomerName().equals("John") &&
                        orderArgument.getAmount() == 100.0
        ));
    }

    @Test
    public void testSendOrderWithDuplicateId_ShouldThrowBadRequestException() throws Exception {
        orderController.getOrderCache().put(order.getId(), true);

        ResultActions result = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Order with the same id already exists"));
    }

}