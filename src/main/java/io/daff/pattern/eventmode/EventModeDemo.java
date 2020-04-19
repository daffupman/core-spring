package io.daff.pattern.eventmode;

/**
 * @author daffupman
 * @since 2020/4/11
 */
public class EventModeDemo {

    public static void main(String[] args) {
        EventSource eventSource = new EventSource();
        SingleClickEventListener scel = new SingleClickEventListener();
        DoubleClickEventListener dcel = new DoubleClickEventListener();
        Event event = new Event();
        event.setType("doubleclick");
        eventSource.registerEventListener(dcel);
        eventSource.registerEventListener(scel);
        eventSource.publishEvent(event);
    }
}
