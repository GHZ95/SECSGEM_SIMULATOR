package com.ran.river;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultEventPublisher extends Thread implements EventPublisher {

    private final BlockingQueue<BootEvent> queue;
    private List<Subscriber> list = new ArrayList<>();
    private Map<Class<? extends BootEvent>, List<Subscriber>> map = new ConcurrentHashMap<>();

    private volatile boolean running;

    public DefaultEventPublisher(int queueSize) {
        queue = new ArrayBlockingQueue<>(queueSize);
        setDaemon(true);
        setName("Default-Publisher-"+Thread.currentThread().getName() );
        running = true;
        System.out.println(Thread.currentThread().getName() + " is run..");
        start();
    }

    @Override
    public void publish(BootEvent event) {
        if (!queue.offer(event)) {
            //如果队列已满，则使用调用者线程通知
            System.out.println("publish event failed, queue size is {}, caller thread runs"+ queue.size());
            notifyAllSubscribers(event);
        }
    }

    @Override
    public void notifySubscriber(Subscriber subscriber, BootEvent event) {
        if (subscriber.executor() != null) {
            subscriber.executor().submit(() -> subscriber.onEvent(event));
        } else {
            subscriber.onEvent(event);
        }
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        list.add(subscriber);
        map.computeIfAbsent(subscriber.supportedType(), clz -> new ArrayList<>());
        map.get(subscriber.supportedType()).add(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        list.remove(subscriber);
        if (map.containsKey(subscriber.supportedType())) {
            map.remove(subscriber.supportedType());
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                BootEvent event = queue.take();
                notifyAllSubscribers(event);
            } catch (InterruptedException e) {
            	System.out.println("poll event error: ");
                Thread.currentThread().interrupt();
            }
        }
    }

    private void notifyAllSubscribers(BootEvent event) {
        for (Subscriber subscriber : list) {
            notifySubscriber(subscriber, event);
        }
    }


    @Override
    public void close() throws Exception {
        running = false;
        queue.clear();
        System.out.println("publisher close..");
    }
    
}