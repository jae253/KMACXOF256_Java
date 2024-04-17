package algorithms;

public class TestUtils {

    static byte[] readHexString(String s) {
        int len = s.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Hex string length should be even.");
        }

        byte[] out = new byte[len / 2];

        for (int i = 0; i < len; i+=2) {
            out[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }

        return out;
    }

}
