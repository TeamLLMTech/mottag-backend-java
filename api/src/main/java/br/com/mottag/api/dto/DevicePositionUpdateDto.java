package br.com.mottag.api.dto;

import java.time.LocalDateTime;

/**
 * DTO for broadcasting device position updates via WebSocket.
 */
public class DevicePositionUpdateDto {
    private String deviceAddress;
    private double x;
    private double y;
    private LocalDateTime timestamp;
    private int antennaCount;
    private String status;

    public DevicePositionUpdateDto() {
    }

    public DevicePositionUpdateDto(String deviceAddress, double x, double y, int antennaCount) {
        this.deviceAddress = deviceAddress;
        this.x = x;
        this.y = y;
        this.timestamp = LocalDateTime.now();
        this.antennaCount = antennaCount;
        this.status = "active";
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getAntennaCount() {
        return antennaCount;
    }

    public void setAntennaCount(int antennaCount) {
        this.antennaCount = antennaCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DevicePositionUpdateDto{" +
                "deviceAddress='" + deviceAddress + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", timestamp=" + timestamp +
                ", antennaCount=" + antennaCount +
                ", status='" + status + '\'' +
                '}';
    }
}
