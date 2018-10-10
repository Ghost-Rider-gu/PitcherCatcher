package software.ping.util;

import java.util.Random;

/**
 * Message generator. Message will be is bounded.
 */
public class MessageGenerator {

    /**
     * Return random string.
     * FYI - for this we could be use Apache Commons library, especially RandomStringUtils.
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

    /**
     * Check the length (minimum is 50) (maximum is 3000) (by default 300).
     * FYI - we will terminate the program, if length is inappropriate
     * but we could be setted it as default value
     *
     * @param length message length {@link int}
     */
    public void checkLengthMessage(int length) {
        if (length < 50 || length > 3000) {
            System.out.println("Inappropriate length for message. Message length might be from 50 to 3000.");
            System.exit(0);
        }
    }

}
