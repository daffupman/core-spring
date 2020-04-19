package io.daff.pattern.eventmode;

/**
 * @author daffupman
 * @since 2020/4/11
 */
public interface EventListener {

    void processEvent(Event event);
}
