package com.gateway.dashboard.rpi;

import com.pi4j.io.serial.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * You may need to change the serial port address (by default /dev/serial0
 *  is a symlink for a primary serial port on a Raspberry Pi).
 */
public class PMS7003Driver {

    private static final Logger log = LoggerFactory.getLogger(PMS7003Driver.class);

    private static final int FRAME_SIZE = 32;
    private static final byte START_BYTE_1 = 0x42;
    private static final byte START_BYTE_2 = 0x4D;
    private static final byte[] SLEEP_CMD_BYTES = { START_BYTE_1, START_BYTE_2, (byte) 0xE4, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x73 };
    private static final byte[] WAKEUP_CMD_BYTES = { START_BYTE_1, START_BYTE_2, (byte) 0xE4, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x74 };

    private Serial serial;
    private SerialDataEventListener listener;
    private ConcurrentLinkedDeque<byte[]> measurementBytesQueue;

    public boolean connect() {
        if (isConnected())
            return true;

        measurementBytesQueue = new ConcurrentLinkedDeque<>();

        serial = SerialFactory.createInstance();

        serial.setBufferingDataReceived(false);

        SerialConfig config = new SerialConfig();

        config.device("/dev/serial0")
                .baud(Baud._9600)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);

        listener = event -> {
            try {
                if (event.length() > 0) {
                    byte[] bytes = event.getBytes();

                    if (bytes.length == FRAME_SIZE)
                        measurementBytesQueue.add(bytes);
                    else
                        log.debug("Bytes received: {}", convertToHexString(bytes));
                }
            }
            catch (IOException e) {
                log.error("Failed to read bytes from event. {}", e.getMessage());
            }
        };

        serial.addListener(listener);

        try {
            serial.open(config);

            log.debug("Opened port.");
        }
        catch (IOException e) {
            log.error("Failed to open port. {}", e.getMessage());
        }

        return isConnected();
    }

    public boolean disconnect() {
        if (!isConnected())
            return true;

        try {
            serial.removeListener(listener);

            serial.close();

            SerialFactory.shutdown();

            measurementBytesQueue.clear();

            log.debug("Closed port.");
        }
        catch (IOException e) {
            log.error("Failed to close port. {}", e.getMessage());
        }

        return !isConnected();
    }

    public boolean activate() {
        if (!connect()) {
            log.error("Can't activate, port not open.");
            return false;
        }

        if (!write(WAKEUP_CMD_BYTES)) {
            log.error("Failed to wake up.");
            return false;
        }

        log.debug("Activated.");

        return true;
    }

    public boolean deactivate() {
        if (!connect()) {
            log.error("Can't deactivate, port not open.");
            return false;
        }

        if (!write(SLEEP_CMD_BYTES)) {
            log.error("Failed to send to sleep.");
            return false;
        }

        log.debug("Deactivated.");

        measurementBytesQueue.clear();

        return true;
    }

    public PMS7003Measurement measure() {
        if (!connect()) {
            log.error("Can't measure, port not open.");
            return null;
        }

        log.debug("Measuring.");

        if (measurementBytesQueue.isEmpty()) {
            log.warn("No measurements available.");
            return null;
        }

        byte[] bytes = measurementBytesQueue.pollLast();

        return new PMS7003Measurement(
                Instant.now(),
                convertBytesToValue(bytes, 4),
                convertBytesToValue(bytes, 6),
                convertBytesToValue(bytes, 8),
                convertBytesToValue(bytes, 10),
                convertBytesToValue(bytes, 12),
                convertBytesToValue(bytes, 14),
                convertBytesToValue(bytes, 16),
                convertBytesToValue(bytes, 18),
                convertBytesToValue(bytes, 20),
                convertBytesToValue(bytes, 22),
                convertBytesToValue(bytes, 24),
                convertBytesToValue(bytes, 26)
        );
    }

    public boolean isConnected() {
        return (serial != null && serial.isOpen());
    }

    private int convertBytesToValue(byte[] bytes, int index) {
        return (Byte.toUnsignedInt(bytes[index]) << 8) + Byte.toUnsignedInt(bytes[index + 1]);
    }

    private boolean write(byte[] bytes) {
        try {
            serial.write(bytes);

            return true;
        }
        catch (IOException e) {
            log.error("Failed to write bytes. {}", e.getMessage());
        }

        return false;
    }

    private String convertToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);

        for (byte b : bytes)
            builder.append(String.format("%02x", b));

        return builder.toString();
    }


}
