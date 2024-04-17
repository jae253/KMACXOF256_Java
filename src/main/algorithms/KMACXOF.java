package algorithms;

public class KMACXOF extends cShake {

    public byte[] compute(String key, byte[] data, int outputLength, String customString) {
        byte[] newX = Utils.concat(Utils.concat(Utils.bytePad(Utils.encodeString(key), 136), data),
                Utils.rightEncode(new byte[] {0}));
        cShake shake = new cShake();
        return shake.compute(newX, outputLength, "KMAC", customString);
    }

    public byte[] compute(byte[] key, byte[] data, int outputLength, String customString) {
        byte[] newX = Utils.concat(Utils.concat(Utils.bytePad(Utils.encodeString(key), 136), data),
                Utils.rightEncode(new byte[] {0}));
        cShake shake = new cShake();
        return shake.compute(newX, outputLength, "KMAC", customString);
    }
}
