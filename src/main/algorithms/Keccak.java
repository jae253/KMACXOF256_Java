package algorithms;

/**
 * An implementation of the sha3 algorithm in java, ported from an implementation in c by Markku-Juhani Saarinen, sha3.c
 *
 * @author Markku-Juhani Saarinen, Rosemary
 * @see "https://github.com/mjosaarinen/tiny_sha3/blob/master/sha3.c"
 * */
public abstract class Keccak {

    static final int ROUNDS = 24;

    static final long[] roundConstants = new long[]{
            0x0000000000000001L, 0x0000000000008082L, 0x800000000000808aL,
            0x8000000080008000L, 0x000000000000808bL, 0x0000000080000001L,
            0x8000000080008081L, 0x8000000000008009L, 0x000000000000008aL,
            0x0000000000000088L, 0x0000000080008009L, 0x000000008000000aL,
            0x000000008000808bL, 0x800000000000008bL, 0x8000000000008089L,
            0x8000000000008003L, 0x8000000000008002L, 0x8000000000000080L,
            0x000000000000800aL, 0x800000008000000aL, 0x8000000080008081L,
            0x8000000000008080L, 0x0000000080000001L, 0x8000000080008008L
    };

    static final int[] rotationNumber = new int[]{
            1,  3,  6,  10, 15, 21, 28, 36, 45, 55, 2,  14,
            27, 41, 56, 8,  25, 43, 62, 18, 39, 61, 20, 44
    };

    static final int[] laneShiftingNumbers = new int[] {
            10, 7,  11, 17, 18, 3, 5,  16, 8,  21, 24, 4,
            15, 23, 19, 13, 12, 2, 20, 14, 22, 9,  6,  1
    };

    private long[] getStateAsLongs() {
        return Utils.byteArrayToLongArray(state);
    }

    private void setStateAsLongs(long[] stateLong) {
        state = Utils.longArrayToByteArray(stateLong);
    }

    protected byte[] state = new byte[200];
    private int rate;
    protected int pointer;

    protected int getRate() {
        return rate;
    }

    public void initialize(int messageLength) {
        rate = 200 - 2 * messageLength;
    }

    /**
     * Shakes up state
     * */
    protected void permute() {
        long[] bitwiseColumn = new long[5];
        long[] stateL = getStateAsLongs();

        for (int r = 0; r < ROUNDS; r++) {
            // Theta

            // Initialize with xor sum of each column
            for (int i = 0; i < 5; i++) {
                bitwiseColumn[i] = stateL[i] ^ stateL[i + 5] ^ stateL[i + 10] ^ stateL[i + 15] ^ stateL[i + 20];
            }

            for (int i = 0; i < 5; i++) {
                long temp = bitwiseColumn[(i + 4) % 5] ^ Utils.ROTL64(bitwiseColumn[(i + 1) % 5], 1);
                for (int j = 0; j < 25; j += 5) {
                    stateL[j + i] ^= temp;
                }

            }

            // Rho Pi
            // jump through array in order dictated by laneShiftingNumbers and
            // replace value with previous value rotated by an amount
            // dictated by rotationAmount.
            long lastValue = stateL[1];
            for (int i = 0; i < 24; i++) {
                int shiftingIndex = laneShiftingNumbers[i];
                int rotationAmount = rotationNumber[i];

                long holder = stateL[shiftingIndex];
                stateL[shiftingIndex] = Utils.ROTL64(lastValue, rotationAmount);
                lastValue = holder;
            }

            // Chi
            for (int j = 0; j < 25; j+= 5) {
                // Load array with sequential values from state
                System.arraycopy(stateL, j, bitwiseColumn, 0, 5);

                // apply loaded values back to state
                for (int i = 0; i < 5; i++) {
                    stateL[j + i] ^= (~bitwiseColumn[(i + 1) % 5]) & bitwiseColumn[(i + 2) % 5];
                }
            }

            // Iota
            // xor round constant so that there is a change to be distributed.
            stateL[0] ^= roundConstants[r];

        }
        setStateAsLongs(stateL);
    }

    protected void absorbData(byte[] data) {
        for (byte datum : data) {
            state[pointer] ^= datum;
            pointer++;

            if (pointer >= rate) {
                permute();
                pointer = 0;
            }
        }
    }

    protected byte[] squeezeData(int messageLength, int constant, boolean shake) {
        byte[] messageReturn = new byte[messageLength];

        state[pointer] ^= (byte) constant;
        state[rate - 1] ^= (byte) 0x80;
        permute();

        pointer = 0;

        for (int i = 0; i < messageLength; i++) {
            if (shake && pointer >= rate) {
                permute();
                pointer = 0;
            }
            messageReturn[i] = state[pointer];
            pointer++;
        }

        return messageReturn;
    }

    public void reset() {
        state = new byte[200];
        pointer = 0;
    }

    String getStateAsHexString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < state.length; i++) {
            if (i % 16 == 0) {
                sb.append('\n');
            }
            byte b = state[i];
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}


