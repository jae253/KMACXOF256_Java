import algorithms.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void testROTL64() {
        long testShift = 0x1000000000000001L;
        long expectedResult = 0x0000000110000000L;

        Assertions.assertEquals(Utils.ROTL64(testShift, 32), expectedResult);
    }

    @Test
    void testlongArrayToByteArray() {
        long[] longArray = new long[] {123L, 321L, 444L};
        byte[] byteArray = new byte[24];
        byteArray[0] = 123;

        byteArray[8] = 65;
        byteArray[9] = 1;

        byteArray[16] = -68;
        byteArray[17] = 1;

        Assertions.assertArrayEquals(byteArray, Utils.longArrayToByteArray(longArray));
    }

    @Test
    void testbyteArrayToLongArray() {
        long[] longArray = new long[] {123L, 321L, 444L};
        byte[] byteArray = new byte[24];
        byteArray[0] = 123;

        byteArray[8] = 65;
        byteArray[9] = 1;

        byteArray[16] = -68;
        byteArray[17] = 1;

        Assertions.assertArrayEquals(longArray, Utils.byteArrayToLongArray(byteArray));
    }
}