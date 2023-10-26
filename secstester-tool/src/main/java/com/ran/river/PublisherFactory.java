package com.ran.river;

public interface PublisherFactory {

    EventPublisher create(Class<? extends BootEvent> event, int queueSize);
}
