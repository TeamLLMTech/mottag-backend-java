package br.com.mottag.api.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.mottag.api.dto.AntennaInfoDto;
import br.com.mottag.api.dto.PositionDto;

/**
 * Service that calculates device position based on RSSI readings from multiple antennas.
 * Implements the center4 strategy: uses the 4 antennas with strongest RSSI to estimate position.
 */
@Service
public class AntennaPositioningService {

    private static final Logger logger = LoggerFactory.getLogger(AntennaPositioningService.class);

    @Value("${positioning.center4.rssi.threshold:2.0}")
    private double center4RssiThreshold;

    /**
     * Calculates the estimated position of a device using the center4 strategy.
     * This method finds the 4 antennas with the greatest RSSI values and calculates
     * a weighted position based on their locations and signal strengths.
     *
     * @param antennaReadings list of antenna information with positions and RSSI values
     * @return estimated position of the device, or null if insufficient data
     */
    public PositionDto calculatePositionCenter4(List<AntennaInfoDto> antennaReadings) {
        if (antennaReadings == null || antennaReadings.isEmpty()) {
            logger.warn("No antenna readings provided");
            return null;
        }

        if (antennaReadings.size() < 4) {
            logger.warn("center4 strategy requires at least 4 antennas, but only {} provided",
                       antennaReadings.size());
            return null;
        }

        // Sort antennas by RSSI in descending order and take top 4
        List<AntennaInfoDto> top4Antennas = antennaReadings.stream()
                .sorted(Comparator.comparingInt(AntennaInfoDto::getRssi).reversed())
                .limit(4)
                .collect(Collectors.toList());

        logger.debug("Top 4 antennas by RSSI: {}", top4Antennas);

        // Extract positions and RSSI values
        List<PositionDto> top4Positions = new ArrayList<>();
        List<Integer> top4Rssi = new ArrayList<>();

        for (AntennaInfoDto antenna : top4Antennas) {
            if (antenna.getPosition() == null) {
                logger.warn("Antenna {} has no position data", antenna.getAntennaId());
                return null;
            }
            top4Positions.add(antenna.getPosition());
            top4Rssi.add(antenna.getRssi());
        }

        // Calculate center (average) of the 4 positions
        double centerX = top4Positions.stream().mapToDouble(PositionDto::getX).average().orElse(0.0);
        double centerY = top4Positions.stream().mapToDouble(PositionDto::getY).average().orElse(0.0);

        // Check if RSSI values are close (max - min < threshold)
        int maxRssi = top4Rssi.stream().mapToInt(Integer::intValue).max().orElse(0);
        int minRssi = top4Rssi.stream().mapToInt(Integer::intValue).min().orElse(0);
        double rssiRange = maxRssi - minRssi;

        logger.debug("RSSI range: {}, threshold: {}", rssiRange, center4RssiThreshold);

        if (rssiRange <= center4RssiThreshold) {
            // All RSSI values are close, return the geometric center
            logger.info("RSSI values are close (range={}), returning center position: ({}, {})",
                       rssiRange, centerX, centerY);
            return new PositionDto(centerX, centerY);
        } else {
            // RSSI values differ significantly, calculate weighted position

            // Normalize RSSI values to positive weights (shift by min and add epsilon to avoid zero)
            List<Double> normalizedRssi = top4Rssi.stream()
                    .mapToDouble(rssi -> rssi - minRssi + 1e-6)
                    .boxed()
                    .collect(Collectors.toList());

            double totalWeight = normalizedRssi.stream().mapToDouble(Double::doubleValue).sum();

            // Calculate weighted position
            double weightedX = 0.0;
            double weightedY = 0.0;

            for (int i = 0; i < top4Positions.size(); i++) {
                PositionDto pos = top4Positions.get(i);
                double weight = normalizedRssi.get(i);
                weightedX += pos.getX() * weight;
                weightedY += pos.getY() * weight;
            }

            weightedX /= totalWeight;
            weightedY /= totalWeight;

            // Interpolate between center and weighted position
            // alpha ranges from 0 (use center) to 1 (use weighted position)
            double alpha = Math.min(rssiRange / (center4RssiThreshold * 2), 1.0);

            double finalX = centerX * (1 - alpha) + weightedX * alpha;
            double finalY = centerY * (1 - alpha) + weightedY * alpha;

            logger.info("Calculated weighted position with alpha={}: ({}, {})", alpha, finalX, finalY);

            return new PositionDto(finalX, finalY);
        }
    }
}
