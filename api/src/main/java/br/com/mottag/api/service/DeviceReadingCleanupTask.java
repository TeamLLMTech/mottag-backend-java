package br.com.mottag.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled task to periodically clean up old device readings from the buffer.
 */
@Component
public class DeviceReadingCleanupTask {

    private static final Logger logger = LoggerFactory.getLogger(DeviceReadingCleanupTask.class);

    private final DeviceReadingBuffer readingBuffer;

    public DeviceReadingCleanupTask(DeviceReadingBuffer readingBuffer) {
        this.readingBuffer = readingBuffer;
    }

    /**
     * Periodically cleans up old readings from the buffer.
     * Runs at the interval specified in application.properties (positioning.cleanup.interval.ms).
     */
    @Scheduled(fixedDelayString = "${positioning.cleanup.interval.ms:10000}")
    public void cleanupOldReadings() {
        logger.debug("Running scheduled cleanup of old device readings");

        try {
            int devicesRemoved = readingBuffer.cleanupOldReadings();

            if (devicesRemoved > 0) {
                logger.info("Cleanup completed: {} devices removed from buffer", devicesRemoved);
            }

            // Log buffer statistics periodically
            logger.debug(readingBuffer.getStatistics());

        } catch (Exception e) {
            logger.error("Error during scheduled cleanup: {}", e.getMessage(), e);
        }
    }
}
