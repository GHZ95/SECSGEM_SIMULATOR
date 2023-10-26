package com.ran.river;

import java.util.concurrent.atomic.AtomicBoolean;

public class NotifyBus {

    public static NotifyBus INSTANCE = new NotifyBus();
    private static Integer QUEUE_SIZE = 100;
    private AtomicBoolean CLOSED = new AtomicBoolean(false);
    private DefaultPublisherFactory factory = DefaultPublisherFactory.INSTANCE;

    private NotifyBus() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> NotifyBus.INSTANCE.shutDown()));
    }

    public void setFactory(DefaultPublisherFactory factory) {
        this.factory = factory;
    }

    public void publish(BootEvent event) {
        factory.getPublisher(event.getClass()).publish(event);
    }

    public void addSubscriber(Subscriber subscriber) {
        addSubscriber(subscriber, factory);
    }

    private void addSubscriber(Subscriber subscriber, PublisherFactory factory) {
        EventPublisher eventPublisher = factory.create(subscriber.supportedType(), QUEUE_SIZE);
        eventPublisher.addSubscriber(subscriber);
    }

    public void shutDown() {
        if (!CLOSED.compareAndSet(false, true)) {
        	System.out.println(">>>>> notifyBus already close..");
            return;
        }
        System.out.println(">>>>> notifyBus close...");
        for (EventPublisher eventPublisher : DefaultPublisherFactory.INSTANCE.getEventPublishers()) {
            try {
                eventPublisher.close();
            } catch (Exception e) {
            	System.out.println("####### notifyBus close error"+ e);
            }
        }
    }


}