package br.com.mottag.api.dto;

/**
 * Represents a single reading of a device from an antenna.
 */
public class DeviceReadingDto {
    private String deviceAddress;
    private String antennaId;
    private PositionDto antennaPosition;
    private int rssi;
    private long timestamp;

    public DeviceReadingDto() {
    }

    public DeviceReadingDto(String deviceAddress, String antennaId, PositionDto antennaPosition,
                           int rssi, long timestamp) {
        this.deviceAddress = deviceAddress;
        this.antennaId = antennaId;
        this.antennaPosition = antennaPosition;
        this.rssi = rssi;
        this.timestamp = timestamp;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getAntennaId() {
        return antennaId;
    }

    public void setAntennaId(String antennaId) {
        this.antennaId = antennaId;
    }

    public PositionDto getAntennaPosition() {
        return antennaPosition;
    }

    public void setAntennaPosition(PositionDto antennaPosition) {
        this.antennaPosition = antennaPosition;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DeviceReadingDto{" +
                "deviceAddress='" + deviceAddress + '\'' +
                ", antennaId='" + antennaId + '\'' +
                ", antennaPosition=" + antennaPosition +
                ", rssi=" + rssi +
                ", timestamp=" + timestamp +
                '}';
    }
}
