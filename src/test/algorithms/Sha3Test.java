package algorithms;

import algorithms.Sha3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class Sha3Test {
    // Test cases copied from Markku-Juhani Saarinen's tiny_sha3
    static String[][] shaTestCases = {
            {   // SHA3-224, corner case with 0-length message
                    "",

                    "6B4E03423667DBB73B6E15454F0EB1ABD4597F9A1B078E3F5B5A6BC7"
            },
            {   // SHA3-256, short message
                    "9F2FCC7C90DE090D6B87CD7E9718C1EA6CB21118FC2D5DE9F97E5DB6AC1E9C10",

                    "2F1A5F7159E34EA19CDDC70EBF9B81F1A66DB40615D7EAD3CC1F1B954D82A3AF"
            },
            {   // SHA3-384, exact block size
                    "E35780EB9799AD4C77535D4DDB683CF33EF367715327CF4C4A58ED9CBDCDD486" +
                            "F669F80189D549A9364FA82A51A52654EC721BB3AAB95DCEB4A86A6AFA93826D" +
                            "B923517E928F33E3FBA850D45660EF83B9876ACCAFA2A9987A254B137C6E140A" +
                            "21691E1069413848",

                    "D1C0FA85C8D183BEFF99AD9D752B263E286B477F79F0710B0103170173978133" +
                            "44B99DAF3BB7B1BC5E8D722BAC85943A"
            },
            {   // SHA3-512, multiblock message
                    "3A3A819C48EFDE2AD914FBF00E18AB6BC4F14513AB27D0C178A188B61431E7F5" +
                            "623CB66B23346775D386B50E982C493ADBBFC54B9A3CD383382336A1A0B2150A" +
                            "15358F336D03AE18F666C7573D55C4FD181C29E6CCFDE63EA35F0ADF5885CFC0" +
                            "A3D84A2B2E4DD24496DB789E663170CEF74798AA1BBCD4574EA0BBA40489D764" +
                            "B2F83AADC66B148B4A0CD95246C127D5871C4F11418690A5DDF01246A0C80A43" +
                            "C70088B6183639DCFDA4125BD113A8F49EE23ED306FAAC576C3FB0C1E256671D" +
                            "817FC2534A52F5B439F72E424DE376F4C565CCA82307DD9EF76DA5B7C4EB7E08" +
                            "5172E328807C02D011FFBF33785378D79DC266F6A5BE6BB0E4A92ECEEBAEB1",

                    "6E8B8BD195BDD560689AF2348BDC74AB7CD05ED8B9A57711E9BE71E9726FDA45" +
                            "91FEE12205EDACAF82FFBBAF16DFF9E702A708862080166C2FF6BA379BC7FFC2"
            }
    };

    Sha3 sha3;

    @BeforeEach
    void setUp() {
        sha3 = new Sha3();
    }

    @Test
    void testSha3Case1() {
        testSha3(0);
    }
    @Test
    void testSha3Case2() {
        testSha3(1);
    }
    @Test
    void testSha3Case3() {
        testSha3(2);
    }
    @Test
    void testSha3Case4() {
        testSha3(3);
    }



    void testSha3(int testCase) {
        byte[] message = TestUtils.readHexString(shaTestCases[testCase][0]);
        byte[] expectedOut = TestUtils.readHexString(shaTestCases[testCase][1]);

        byte[] actualOut = sha3.compute(message, expectedOut.length);

        assertArrayEquals(expectedOut, actualOut);
    }

}