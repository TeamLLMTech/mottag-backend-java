package br.com.mottag.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MqttMessagePayloadEventDto {

    @JsonProperty("addr")
    private String address;

    @JsonProperty("rssi")
    private int rssi;

    @JsonProperty("t")
    private long timestamp;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        return "MqttMessagePayloadEventDto{" +
                "address='" + address + '\'' +
                ", rssi=" + rssi +
                ", timestamp=" + timestamp +
                '}';
    }
}
