package cn.copoint.coeditor.utils;

public class AudioMixer {
    private double f = 1;
    private int MAX, MIN, AVG;

    public AudioMixer(int MAX, int MIN, int AVG) {
        this.MAX = MAX - AVG;
        this.MIN = MIN - AVG;
        this.AVG = AVG;
    }

    public int[] mix(int[][] sourceAudio) {
        int output[] = new int[sourceAudio[0].length];
        int number = sourceAudio.length;
        int length = sourceAudio[0].length;
        int i, j;
        for (i = 0; i < length; i++) {
            int temp = 0;
            for (j = 0; j < number; j++) {
                temp += (sourceAudio[j][i] - AVG);
            }
            output[i] = (int) (temp * f);
            if (output[i] > MAX) {
                f = (double) MAX / (double) (output[i]);
                output[i] = MAX;
            }
            if (output[i] < MIN) {
                f = (double) MIN / (double) (output[i]);
                output[i] = MIN;
            }
            if (f < 1) {
                f += ((double) 1 - f) / (double) 32;
            }
            output[i] += AVG;
        }
        return output;
    }
}
