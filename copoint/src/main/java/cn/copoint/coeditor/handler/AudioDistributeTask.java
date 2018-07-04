package cn.copoint.coeditor.handler;

import cn.copoint.coeditor.utils.AudioMixer;
import cn.copoint.coeditor.utils.Base64;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class AudioDistributeTask implements Runnable {

    private String sessionId;
    private List<AudioBlob> audioBlobList;
    private SimpMessagingTemplate messagingTemplate;
    private AudioMixer audioMixer;

    private class AudioMessage {
        private String message;

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
    }

    public AudioDistributeTask(String sessionId, List<AudioBlob> audioBlobList, SimpMessagingTemplate messagingTemplate) {
        this.sessionId = sessionId;
        this.audioBlobList = audioBlobList;
        this.messagingTemplate = messagingTemplate;
        this.audioMixer = new AudioMixer(255,0,128);
    }

    @Override
    public void run() {
//        System.out.println(sessionId + " : " + audioBlobList.size());
        if (audioBlobList.size() > 1) {
            int[][] temp_blob = new int[audioBlobList.size()-1][audioBlobList.get(0).length()];
            int i = 0;
            audioBlobList.forEach(item -> {
                if (!item.getSessionId().equals(sessionId)) {
                    temp_blob[i] = item.read();
                }
            });
            audioBlobList.forEach(item -> {
                if (item.getSessionId().equals(sessionId)) {
                    AudioMessage message = new AudioMessage();
                    message.setMessage(Base64.encode(audioMixer.mix(temp_blob)));
                    messagingTemplate.convertAndSendToUser(sessionId,"/callback", message);
                }
            });
        }
    }
}
