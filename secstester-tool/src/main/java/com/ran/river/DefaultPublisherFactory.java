package com.ran.river;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultPublisherFactory implements PublisherFactory {

    private Map<Class<? extends BootEvent>, DefaultEventPublisher> map;

    public static DefaultPublisherFactory INSTANCE = new DefaultPublisherFactory();

    private DefaultPublisherFactory() {
        map = new ConcurrentHashMap<>();
    }

    @Override
    public EventPublisher create(Class<? extends BootEvent> event, int queueSize) {
        map.computeIfAbsent(event, e -> {
            DefaultEventPublisher eventPublisher = new DefaultEventPublisher(queueSize);
            return eventPublisher;
        });
        return map.get(event);
    }

    public EventPublisher getPublisher(Class<? extends BootEvent> event) {
        return map.get(event);
    }

    public List<EventPublisher> getEventPublishers() {
        return new ArrayList<>(map.values());
    }


}