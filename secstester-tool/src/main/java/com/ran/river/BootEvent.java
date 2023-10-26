package com.ran.river;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public abstract class BootEvent implements Serializable {

    private static AtomicLong SEQ = new AtomicLong(0);

    private Object source;
    private Long timeStamp;
    private Long id;

    public BootEvent(Object source) {
        this.source = source;
        this.timeStamp = System.currentTimeMillis();
        this.id = getSequence();
    }

    private Long getSequence() {
        return SEQ.getAndIncrement();
    }

}

