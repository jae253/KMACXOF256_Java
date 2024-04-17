package algorithms;

public abstract class Shake extends Keccak {

    public void absorb(byte[] data) {
        this.absorbData(data);
    }

    public void xof() {
        state[pointer] ^= (byte) 0x04;
        state[getRate() - 1] ^= (byte) 0x80;
        permute();

        pointer = 0;
    }

    public byte[] squeeze(int messageLength) {

        byte[] messageReturn = new byte[messageLength];
        for (int i = 0; i < messageLength; i++) {

            if (pointer >= getRate()) {
                permute();
                pointer = 0;
            }

            messageReturn[i] = state[pointer];
            pointer++;
        }

        return messageReturn;
    }
}
