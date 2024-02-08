import org.fungover.thunder.Broker;
import org.fungover.thunder.Main;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

class MainTest {

    @Mock
    private Broker brokerMock;

    @InjectMocks
    private Main main;

    @Test
    void givenCreateAndStartBrokerIsCalledThenBrokerStartShouldBeInvoked() throws IOException {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Call the method to create and start the Broker
        main.createAndStartBroker(brokerMock);

        // Verify that the start method of the broker is invoked
        verify(brokerMock, times(1)).start();
    }
}
