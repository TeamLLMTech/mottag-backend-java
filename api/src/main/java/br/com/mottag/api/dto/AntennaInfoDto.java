package br.com.mottag.api.dto;

/**
 * Contains information about an antenna including its ID, position, and RSSI reading.
 */
public class AntennaInfoDto {
    private String antennaId;
    private PositionDto position;
    private int rssi;

    public AntennaInfoDto() {
    }

    public AntennaInfoDto(String antennaId, PositionDto position, int rssi) {
        this.antennaId = antennaId;
        this.position = position;
        this.rssi = rssi;
    }

    public String getAntennaId() {
        return antennaId;
    }

    public void setAntennaId(String antennaId) {
        this.antennaId = antennaId;
    }

    public PositionDto getPosition() {
        return position;
    }

    public void setPosition(PositionDto position) {
        this.position = position;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public String toString() {
        return "AntennaInfoDto{" +
                "antennaId='" + antennaId + '\'' +
                ", position=" + position +
                ", rssi=" + rssi +
                '}';
    }
}
