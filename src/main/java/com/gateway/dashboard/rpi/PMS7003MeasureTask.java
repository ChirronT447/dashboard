package com.gateway.dashboard.rpi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.*;

public class PMS7003MeasureTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PMS7003MeasureTask.class);

    private PMS7003Driver driver;
    private ScheduledExecutorService scheduler;

    public PMS7003MeasureTask(PMS7003Driver driver, ScheduledExecutorService scheduler) {
        this.driver = driver;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        log.debug("Running.");

        if (driver.activate()) {
            ScheduledFuture<PMS7003Measurement> future = scheduler.schedule(
                    () -> driver.measure(),
                    Duration.ofMinutes(1L).toMillis(),
                    TimeUnit.MILLISECONDS);

            PMS7003Measurement measurement = null;

            try {
                measurement = future.get(Duration.ofMinutes(1L).toMillis(), TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                log.error("Measurement interrupted. {}", e.getMessage());
                future.cancel(true);
            }
            catch (TimeoutException e) {
                log.error("Measurement timed out. {}", e.getMessage());
                future.cancel(true);
            }
            catch (ExecutionException e) {
                log.error("Measurement failed. {}", e.getMessage());
            }
            finally {
                driver.deactivate();
            }

            if (measurement != null) {
                log.debug("PM1.0: {} | PM2.5: {} | PM10.0: {}",
                        measurement.pm1_0_atmo(),
                        measurement.pm2_5_atmo(),
                        measurement.pm10_0_atmo());

                // TODO save into database
            }
        }
        else
            log.error("Failed to activate.");
    }

}
