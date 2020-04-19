package io.daff.pattern.eventmode;

/**
 * @author daffupman
 * @since 2020/4/11
 */
public class DoubleClickEventListener implements EventListener {

    @Override
    public void processEvent(Event event) {
        if ("doubleclick".equals(event.getType())) {
            System.out.println("触发双击");
        }
    }
}
