package io.daff.pattern.eventmode;

/**
 * @author daffupman
 * @since 2020/4/11
 */
public class SingleClickEventListener implements EventListener {

    @Override
    public void processEvent(Event event) {
        if ("singleclick".equals(event.getType())) {
            System.out.println("触发单击");
        }
    }
}
