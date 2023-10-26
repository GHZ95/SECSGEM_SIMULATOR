package com.ran.river;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public interface EventPublisher extends AutoCloseable {

    /**
     * 广播发布事件
     * @param event
     */
    void publish(BootEvent event);

    /**
     * 单个发布
     * @param subscriber
     * @param event
     */
    void notifySubscriber(Subscriber subscriber, BootEvent event);

    /**
     * 添加订阅者
     * @param subscriber
     */
    void addSubscriber(Subscriber subscriber);

    /**
     * 删除订阅者
     * @param subscriber
     */
    void removeSubscriber(Subscriber subscriber);
}




