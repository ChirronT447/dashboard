package com.gateway.dashboard.rpi;

import java.time.Instant;

/**
 * PMS7003 gives values of “CF1” (standard particles or CF-1) and
 * “atmo” (atmospheric environment) for supported particle
 * sizes and also the counts. We are interested in “atmo” values.
 */
public record PMS7003Measurement(
        Instant time,
        int pm1_0_cf1, int pm2_5_cf1, int pm10_0_cf1,
        int pm1_0_atmo, int pm2_5_atmo, int pm10_0_atmo,
        int pm0_3_count, int pm0_5_count,
        int pm1_0_count, int pm2_5_count,
        int pm5_0_count, int pm10_0_count
) {}
