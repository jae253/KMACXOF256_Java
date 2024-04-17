package algorithms;

import algorithms.Shake;
import algorithms.Shake128;
import algorithms.Shake256;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ShakeTest {
    // Test vectors have bytes 480..511 of XOF output for given inputs.
    // From http://csrc.nist.gov/groups/ST/toolkit/examples.html#aHashing
    static String[] shakeTestCases = new String[]{
            // SHAKE128, message of length 0
            "43E41B45A653F2A5C4492C1ADD544512DDA2529833462B71A41A45BE97290B6F",
            // SHAKE256, message of length 0
            "AB0BAE316339894304E35877B0C28A9B1FD166C796B9CC258A064A8F57E27F2A",
            // SHAKE128, 1600-bit test pattern
            "44C9FB359FD56AC0A9A75A743CFF6862F17D7259AB075216C0699511643B6439",
            // SHAKE256, 1600-bit test pattern
            "6A1A9D7846436E4DCA5728B6F760EEF0CA92BF0BE5615E96959D767197A0BEEB"
    };

    Shake128 shake128;
    Shake256 shake256;

    static byte[] testPattern = new byte[200];

    @BeforeAll
    static void init() {
        Arrays.fill(testPattern, (byte) 0xA3);
    }

    @BeforeEach
    void setUp() {

        shake128 = new Shake128();
        shake256 = new Shake256();

    }

    @Test
    void testShakeCase1() {
        testShake(0, false, shake128);
    }
    @Test
    void testShakeCase2() {
        testShake(1, false, shake256);
    }
    @Test
    void testShakeCase3() {
        testShake(2, true, shake128);
    }
    @Test
    void testShakeCase4() {
        testShake(3, true, shake256);
    }

    void testShake(int testCase, boolean input, Shake shake) {
        if (input) {
            shake.absorb(testPattern);
        }

        shake.xof();

        shake.squeeze(512 - 32);

        byte[] lastBytes = shake.squeeze(32);

        byte[] expected = TestUtils.readHexString(shakeTestCases[testCase]);

        assertArrayEquals(expected, lastBytes);
    }
}
