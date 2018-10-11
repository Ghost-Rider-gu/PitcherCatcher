package software.ping.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Tests for MessageGenerator class.
 */
public class MessageGeneratorTest {

    private static MessageGenerator messageGenerator;
    private static final Integer MESSAGE_SIZE = 200;

    @Before
    public void setup() {
        messageGenerator = new MessageGenerator();
    }

    @Test
    public void shouldGetNewMessage() throws Exception {
        String newString = messageGenerator.generateMessage(MESSAGE_SIZE);

        assertNotNull(newString);
        assertEquals((long)newString.length(), (long)MESSAGE_SIZE);
    }

}