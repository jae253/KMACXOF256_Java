package algorithms;

public class Sha3 extends Keccak {
    byte[] compute(byte[] data, int length) {
        initialize(length);
        absorbData(data);
        return squeeze(length);
    }



    protected byte[] squeeze(int messageLength) {
        byte[] messageReturn = new byte[messageLength];

        state[pointer] ^= (byte) 0x06;
        state[getRate() - 1] ^= (byte) 0x80;
        permute();

        System.arraycopy(state, 0, messageReturn, 0, messageLength);

        return messageReturn;
    }
}
