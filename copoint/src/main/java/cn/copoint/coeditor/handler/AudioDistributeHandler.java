package cn.copoint.coeditor.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

@Component
public class AudioDistributeHandler {
    @Autowired
    @Qualifier("threadPoolTaskScheduler")
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private PeriodicTrigger periodicTrigger;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(20);

    private int audioPeriod, desiredRate;
    private SimpMessagingTemplate messagingTemplate;
    private Map<String, ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();
    private ScheduledFuture<?> future;
    private List<AudioBlob> audioBlobList = new CopyOnWriteArrayList<>();

    public void setAudioPeriod(int audioPeriod) {
        this.audioPeriod = audioPeriod;
    }

    public void setDesiredRate(int desiredRate) {
        this.desiredRate = desiredRate;
    }

    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void writeBlob(String sessionId, int[] data) {
        audioBlobList.forEach(item -> {
            if (item.getSessionId().equals(sessionId)) {
                item.write(data);
            }
        });
    }

    public boolean newTask(String sessionId){
        if (scheduledFutureMap.get(sessionId) == null){
            audioBlobList.add(new AudioBlob(sessionId, desiredRate / (1000/audioPeriod)));
            scheduledFutureMap.put(
                    sessionId,
                    executor.scheduleAtFixedRate(
                            new AudioDistributeTask(sessionId, audioBlobList, messagingTemplate),
                            audioPeriod, audioPeriod, TimeUnit.MILLISECONDS));
        }
        else{
            return false;
        }
        return true;
    }

    public boolean cancelTask(String sessionId){
        if (scheduledFutureMap.get(sessionId) == null){
            return false;
        }
        else{
            scheduledFutureMap.get(sessionId).cancel(true);
            scheduledFutureMap.remove(sessionId);
        }
        int n = audioBlobList.size();
        for (int i = 0; i < n; i++) {
            if (audioBlobList.get(i).getSessionId().equals(sessionId)) {
                audioBlobList.remove(i);
                break;
            }
        }
        return true;
    }

    public int getSize() {
        return audioBlobList.size();
    }

}
