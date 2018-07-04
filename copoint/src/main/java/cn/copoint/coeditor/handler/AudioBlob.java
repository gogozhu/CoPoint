package cn.copoint.coeditor.handler;

import java.util.Arrays;

public class AudioBlob {
    private String sessionId;
    private int[] blob;

    public AudioBlob(String sessionId, int length) {
        this.sessionId = sessionId;
        this.blob = new int[length];
        Arrays.fill(this.blob,128);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void write(int[] data) {
        synchronized (this) {
            System.arraycopy(data, 0, blob, 0, data.length);
        }
    }

    public int[] read() {
        synchronized (this) {
            int[] data = new int[blob.length];
            System.arraycopy(blob, 0, data, 0, blob.length);
            return data;
        }
    }

    public int length() {
        return blob.length;
    }
}
