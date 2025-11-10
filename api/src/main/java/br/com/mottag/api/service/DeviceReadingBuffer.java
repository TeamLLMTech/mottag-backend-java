package br.com.mottag.api.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.mottag.api.dto.AntennaInfoDto;
import br.com.mottag.api.dto.DeviceReadingDto;

/**
 * Thread-safe buffer that stores device readings from multiple antennas
 * and provides aggregated data for position calculation.
 */
@Component
public class DeviceReadingBuffer {

    private static final Logger logger = LoggerFactory.getLogger(DeviceReadingBuffer.class);

    // Map: deviceAddress -> List of readings
    private final Map<String, List<DeviceReadingDto>> readingsBuffer = new ConcurrentHashMap<>();

    @Value("${positioning.time.window.ms:5000}")
    private long timeWindowMs;

    @Value("${positioning.min.readings:4}")
    private int minReadings;

    /**
     * Adds a new reading to the buffer for a device.
     *
     * @param reading the device reading to store
     */
    public void addReading(DeviceReadingDto reading) {
        String deviceAddress = reading.getDeviceAddress();

        readingsBuffer.compute(deviceAddress, (key, existingReadings) -> {
            if (existingReadings == null) {
                existingReadings = new ArrayList<>();
            }

            existingReadings.add(reading);
            logger.debug("Added reading for device {}: {} total readings",
                        deviceAddress, existingReadings.size());

            return existingReadings;
        });
    }

    /**
     * Gets aggregated antenna readings for a device within the time window.
     * Returns the most recent reading from each unique antenna.
     *
     * @param deviceAddress the MAC address of the device
     * @return list of antenna readings, or empty list if insufficient data
     */
    public List<AntennaInfoDto> getAggregatedReadings(String deviceAddress) {
        List<DeviceReadingDto> deviceReadings = readingsBuffer.get(deviceAddress);

        if (deviceReadings == null || deviceReadings.isEmpty()) {
            logger.debug("No readings found for device {}", deviceAddress);
            return new ArrayList<>();
        }

        long currentTime = System.currentTimeMillis();
        long cutoffTime = currentTime - timeWindowMs;

        // Filter readings within time window and group by antenna
        Map<String, DeviceReadingDto> latestByAntenna = deviceReadings.stream()
                .filter(reading -> reading.getTimestamp() > cutoffTime)
                .collect(Collectors.groupingBy(
                        DeviceReadingDto::getAntennaId,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingLong(DeviceReadingDto::getTimestamp)),
                                optional -> optional.orElse(null)
                        )
                ));

        // Remove null values (shouldn't happen, but defensive programming)
        latestByAntenna.values().removeIf(reading -> reading == null);

        logger.debug("Device {}: {} unique antennas in time window ({}ms)",
                    deviceAddress, latestByAntenna.size(), timeWindowMs);

        // Convert to AntennaInfoDto list
        List<AntennaInfoDto> antennaReadings = latestByAntenna.values().stream()
                .map(reading -> new AntennaInfoDto(
                        reading.getAntennaId(),
                        reading.getAntennaPosition(),
                        reading.getRssi()
                ))
                .collect(Collectors.toList());

        return antennaReadings;
    }

    /**
     * Checks if there are enough readings for a device to calculate position.
     *
     * @param deviceAddress the MAC address of the device
     * @return true if there are at least the minimum required readings
     */
    public boolean hasEnoughReadings(String deviceAddress) {
        List<AntennaInfoDto> readings = getAggregatedReadings(deviceAddress);
        return readings.size() >= minReadings;
    }

    /**
     * Removes old readings from the buffer that are outside the time window.
     * Should be called periodically to prevent memory leaks.
     *
     * @return number of devices cleaned up
     */
    public int cleanupOldReadings() {
        long currentTime = System.currentTimeMillis();
        long cutoffTime = currentTime - timeWindowMs;
        int devicesRemoved = 0;
        int readingsRemoved = 0;

        for (Map.Entry<String, List<DeviceReadingDto>> entry : readingsBuffer.entrySet()) {
            String deviceAddress = entry.getKey();
            List<DeviceReadingDto> readings = entry.getValue();

            int sizeBefore = readings.size();
            readings.removeIf(reading -> reading.getTimestamp() < cutoffTime);
            int sizeAfter = readings.size();

            readingsRemoved += (sizeBefore - sizeAfter);

            // Remove device entry if no readings left
            if (readings.isEmpty()) {
                readingsBuffer.remove(deviceAddress);
                devicesRemoved++;
            }
        }

        if (devicesRemoved > 0 || readingsRemoved > 0) {
            logger.info("Cleanup: removed {} readings from {} devices",
                       readingsRemoved, devicesRemoved);
        }

        return devicesRemoved;
    }

    /**
     * Clears all readings for a specific device.
     *
     * @param deviceAddress the MAC address of the device
     */
    public void clearDevice(String deviceAddress) {
        readingsBuffer.remove(deviceAddress);
        logger.debug("Cleared all readings for device {}", deviceAddress);
    }

    /**
     * Clears all readings from the buffer.
     */
    public void clearAll() {
        int deviceCount = readingsBuffer.size();
        readingsBuffer.clear();
        logger.info("Cleared buffer: {} devices removed", deviceCount);
    }

    /**
     * Gets the current size of the buffer.
     *
     * @return number of devices being tracked
     */
    public int getBufferSize() {
        return readingsBuffer.size();
    }

    /**
     * Gets statistics about the buffer.
     *
     * @return formatted statistics string
     */
    public String getStatistics() {
        int totalDevices = readingsBuffer.size();
        int totalReadings = readingsBuffer.values().stream()
                .mapToInt(List::size)
                .sum();

        return String.format("Buffer Statistics: %d devices, %d total readings, " +
                           "time window: %dms, min readings: %d",
                           totalDevices, totalReadings, timeWindowMs, minReadings);
    }

    public long getTimeWindowMs() {
        return timeWindowMs;
    }

    public void setTimeWindowMs(long timeWindowMs) {
        this.timeWindowMs = timeWindowMs;
    }

    public int getMinReadings() {
        return minReadings;
    }

    public void setMinReadings(int minReadings) {
        this.minReadings = minReadings;
    }
}
