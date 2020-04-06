package com.emptyirony.networkmanager.bungee.util;

import lombok.Data;
import me.allen.chen.util.time.TimeUtil;

import java.util.concurrent.TimeUnit;

@Data
public class Cooldown {
    private long start = System.currentTimeMillis();
    private long expire;
    private boolean notified;

    private long duration;

    public Cooldown(long duration) {
        this.duration = duration;
        this.expire = this.start + duration;
        if (duration == 0L) {
            this.notified = true;
        }
    }

    public Cooldown(long duration, TimeUnit timeUnit) {
        this(timeUnit.toMillis(duration));
    }

    public long getPassed() {
        return System.currentTimeMillis() - this.start;
    }

    public long getRemaining() {
        return this.expire - System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() - this.expire >= 0L;
    }

    public String getTimeLeft() {
        return this.getRemaining() >= 60000L ? TimeUtil.millisToRoundedTime(this.getRemaining()) : TimeUtil.millisToSeconds(this.getRemaining());
    }

    public void reset() {
        this.start = System.currentTimeMillis();
        this.expire = this.start + this.duration;
        if (duration == 0L) {
            this.notified = true;
        }
    }
}
