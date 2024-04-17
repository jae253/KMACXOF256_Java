package algorithms;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Utils {
    public static long ROTL64(long x, int shift) {
        return (x << shift) | (x >>> (64 - shift));
    }

    public static byte[] rightEncode(byte[] data) {
        assert data.length <= 255;

        byte[] returnBytes = Arrays.copyOf(data, data.length + 1);
        returnBytes[data.length] = (byte) data.length;
        return returnBytes;
    }

    static byte[] leftEncode (byte[] data) {
        assert data.length <= 255;

        byte[] returnBytes = new byte[data.length + 1];
        System.arraycopy(data, 0, returnBytes, 1, data.length);

        returnBytes[0] = (byte) data.length;
        return returnBytes;
    }

    public static byte[] bytePad (byte[] x, int w){
        assert w > 0;

        byte[] wenc = leftEncode(intToBytes(w));
        byte[] z = new byte[w*((wenc.length + x.length + w - 1)/w)];

        System.arraycopy(wenc, 0, z, 0, wenc.length);
        System.arraycopy(x, 0, z, wenc.length, x.length);

        for (int i = wenc.length + x.length; i < z.length; i++){
            z[i] = (byte)0;
        }

        return z;
    }

    public static byte[] encodeString (String s) {
        byte[] chars = s.getBytes();
        // Fixed not calling in terms of bits instead of bytes
        return concat(leftEncode(intToBytes(s.length() * 8)), chars);
    }

    public static byte[] encodeString (byte[] s) {
        // Fixed not calling in terms of bits instead of bytes
        return concat(leftEncode(intToBytes(s.length * 8)), s);
    }
    
    public static byte[] concat(byte[] x, byte[] y) {
        byte[] newArray = new byte[x.length + y.length];
        System.arraycopy(x, 0, newArray, 0, x.length);
        System.arraycopy(y, 0, newArray, x.length, y.length);
        return newArray;
    }

    // Test method for debugging purposes
    public static void readBytesToHex(byte[] bytes, String caption){
        System.out.println(caption);
        for (int i = 0; i < bytes.length; i++){
            if (i > 0 && (i + 1) % 16 != 1){
                System.out.print(" ");
            }
            int a = Byte.toUnsignedInt(bytes[i]);
            if (a < 16){
                System.out.print("0");
            }
            System.out.print(Integer.toHexString(a).toUpperCase());
            if ((i + 1) % 16 == 0 && i != 0){
                System.out.println();
            }
        }
        System.out.println();
    }

    static byte[] intToBytes(int input) {
        StringBuilder s1 = new StringBuilder();
        /*
        I found it's easier to add a single leading zero at the beginning of a hex string,
        than worry about how many individual bits we are working with in binary, since
        hex mostly takes care of that problem by nature (a43 -> 0a43)
        */
        s1.append(Integer.toHexString(input));
        if (s1.length() % 2 != 0){
            s1.insert(0, "0");
        }

        // Preparing byte array for hex characters: "0a43" -> [x0a, x43]
        byte[] byteArray = new byte[s1.length() / 2];

        // Parsing each pair of charaters
        for (int i = 0; i < byteArray.length; i++) {
            int val = Integer.parseInt(s1.substring(i * 2, (i * 2) + 2), 16);
            byteArray[i] = (byte) val;
        }
        return byteArray;
    }

    public static byte[] longArrayToByteArray(long[] longArray) {
        byte[] byteArray = new byte[longArray.length * 8];  // 8 bytes in a long
        for (int i = 0; i < longArray.length; i++) {
            long value = longArray[i];
            for (int j = 0; j < 8; j++) {
                byteArray[i * 8 + j] = (byte) (value & 0xFF);
                value >>= 8;
            }
        }
        return byteArray;
    }

    public static long[] byteArrayToLongArray(byte[] byteArray) {
        if (byteArray.length % 8 != 0) {
            throw new IllegalArgumentException("Byte array length must be a multiple of 8");
        }

        long[] longArray = new long[byteArray.length / 8];
        for (int i = 0; i < longArray.length; i++) {
            long value = 0;
            for (int j = 0; j < 8; j++) {
                value |= ((long) byteArray[i * 8 + j] & 0xFF) << (j * 8);
            }
            longArray[i] = value;
        }
        return longArray;
    }
}
