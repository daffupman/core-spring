package io.daff.pattern.eventmode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daffupman
 * @since 2020/4/11
 */
public class EventSource {

    /**
     * 存储所有的监听器
     */
    private List<EventListener> listenerList = new ArrayList<>();

    public void registerEventListener(EventListener el) {
        listenerList.add(el);
    }

    public void publishEvent(Event event) {
        // 广播事件，只有对指定事件感兴趣的监听器才会给出响应
        for (EventListener eventListener : listenerList) {
            eventListener.processEvent(event);
        }
    }
}
