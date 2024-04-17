package algorithms;

public class cShake extends Shake {
    public cShake() { initialize(32); }

    public byte[] compute(byte[] data, int length, String functionName, String customString) {
        byte[] pad = Utils.bytePad(Utils.concat(Utils.encodeString(functionName),
                Utils.encodeString(customString)), 136);
        byte[] input = new byte[pad.length + data.length];
        System.arraycopy(pad, 0, input, 0, pad.length);
        System.arraycopy(data, 0, input, pad.length, data.length);
        absorb(input);
        xof();
        return squeeze(length);
    }
}
