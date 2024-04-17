package algorithms;

import algorithms.KMACXOF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class KMACXOFTest {
    byte[] testK;
    String testS;
    KMACXOF testKMACXOF;
    byte[] testData;
    @BeforeEach
    void setUp(){
        testKMACXOF = new KMACXOF();
        testK = TestUtils.readHexString("404142434445464748494A4B4C4D4E4F" +
                "505152535455565758595A5B5C5D5E5F");
        testS = "";
    }
    @Test
    void testKMACXOF1() {
        testS = "My Tagged Application";
        testData = TestUtils.readHexString(
                "00010203");
        byte[] expected = TestUtils.readHexString("1755133F1534752AAD0748F2C706FB5C" +
                "784512CAB835CD15676B16C0C6647FA9" +
                "6FAA7AF634A0BF8FF6DF39374FA00FAD" +
                "9A39E322A7C92065A64EB1FB0801EB2B");
        testKMACXOFGeneric(testData, expected);
    }
    @Test
    void testKMACXOF2(){
        testData = TestUtils.readHexString("000102030405060708090A0B0C0D0E0F" +
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
        byte[] expected = TestUtils.readHexString("FF7B171F1E8A2B24683EED37830EE797" +
                "538BA8DC563F6DA1E667391A75EDC02C" +
                "A633079F81CE12A25F45615EC8997203" +
                "1D18337331D24CEB8F8CA8E6A19FD98B");
        testKMACXOFGeneric(testData, expected);
    }

    @Test
    void testKMACXOF3(){
        testS = "My Tagged Application";
        testData = TestUtils.readHexString("000102030405060708090A0B0C0D0E0F" +
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
        byte[] expected = TestUtils.readHexString("D5BE731C954ED7732846BB59DBE3A8E3" +
                "0F83E77A4BFF4459F2F1C2B4ECEBB8CE" +
                "67BA01C62E8AB8578D2D499BD1BB2767" +
                "68781190020A306A97DE281DCC30305D");
        testKMACXOFGeneric(testData, expected);
    }
    void testKMACXOFGeneric(byte[] theInput, byte[] theExpectedOutput){
        byte[] result = testKMACXOF.compute(testK, theInput, 64, testS);
        assertArrayEquals(theExpectedOutput, result);
    }
}
