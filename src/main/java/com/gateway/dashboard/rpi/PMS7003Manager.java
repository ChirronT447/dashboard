package com.gateway.dashboard.rpi;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PMS7003Manager {


    public static void main(String[] args) {
        PMS7003Driver driver = new PMS7003Driver();

        PMS7003MeasureTask task = new PMS7003MeasureTask(
                driver,
                Executors.newSingleThreadScheduledExecutor());

        ScheduledFuture<?> future = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                task, 0L, Duration.ofMinutes(1L).toMillis(), TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (future != null && !future.isDone())
                future.cancel(true);

            driver.disconnect();
        }));
    }


}
