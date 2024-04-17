package algorithms;

import algorithms.cShake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class cShakeTest {
    String testN;
    String testS;
    cShake testShake;
    byte[] testData;
    @BeforeEach
    void setUp(){
        testShake = new cShake();
        testN = "";
        testS = "Email Signature";
    }
    @Test
    void testShake1() {
        testData = TestUtils.readHexString(
                "00010203");
        byte[] expected = TestUtils.readHexString("D008828E2B80AC9D2218FFEE1D070C48" +
                "B8E4C87BFF32C9699D5B6896EEE0EDD1" +
                "64020E2BE0560858D9C00C037E34A969" +
                "37C561A74C412BB4C746469527281C8C");
        testShakeGeneric(testData, expected);
    }
    @Test
    void testShake2(){
        testData = TestUtils.readHexString(
                "000102030405060708090A0B0C0D0E0F" +
                        "101112131415161718191A1B1C1D1E1F" +
                        "202122232425262728292A2B2C2D2E2F" +
                        "303132333435363738393A3B3C3D3E3F" +
                        "404142434445464748494A4B4C4D4E4F" +
                        "505152535455565758595A5B5C5D5E5F" +
                        "606162636465666768696A6B6C6D6E6F" +
                        "707172737475767778797A7B7C7D7E7F" +
                        "808182838485868788898A8B8C8D8E8F" +
                        "909192939495969798999A9B9C9D9E9F" +
                        "A0A1A2A3A4A5A6A7A8A9AAABACADAEAF" +
                        "B0B1B2B3B4B5B6B7B8B9BABBBCBDBEBF" +
                        "C0C1C2C3C4C5C6C7");
        byte[] expected = TestUtils.readHexString("07DC27B11E51FBAC75BC7B3C1D983E8B" +
                "4B85FB1DEFAF218912AC864302730917" +
                "27F42B17ED1DF63E8EC118F04B23633C" +
                "1DFB1574C8FB55CB45DA8E25AFB092BB");
        testShakeGeneric(testData, expected);
    }

    void testShakeGeneric(byte[] theInput, byte[] theExpectedOutput){
        byte[] result = testShake.compute(theInput, 64, testN, testS);
        assertArrayEquals(theExpectedOutput, result);
    }
}