package com.ran.river;

import java.util.concurrent.ThreadPoolExecutor;

public abstract class Subscriber<E extends BootEvent> {

    public abstract void onEvent(E event);

    public abstract Class<? extends BootEvent> supportedType();

    /**
    * 如果指定了纯种池，则会异步执行
    */
    public ThreadPoolExecutor executor(){
        return null;
    }

}