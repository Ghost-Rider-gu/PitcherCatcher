package software.ping.util;

import java.util.Random;

/**
 * Message generator. Message will be bounded.
 */
public class MessageGenerator {

    /**
     * Return a random string.
     * FYI - for this we could use Apache Commons library, especially RandomStringUtils.
     *
     * @param length message length {@link int}
     * @return message {@link String}
     */
    public String generateMessage(int length) {
        int startCharCode = 97;
        int endCharCode = 122;

        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomLetter = startCharCode + (int)(random.nextFloat() * (endCharCode - startCharCode + 1));
            randomString.append((char)randomLetter);
        }

        return randomString.toString();
    }

}
