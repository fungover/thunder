package org.fungover.thunder;

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
        MockitoAnnotations.openMocks(this);
        Main.createAndStartBroker(brokerMock);
        verify(brokerMock, times(1)).start();
    }
}
