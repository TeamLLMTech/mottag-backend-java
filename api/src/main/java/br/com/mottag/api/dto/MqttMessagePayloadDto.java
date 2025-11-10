package br.com.mottag.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MqttMessagePayloadDto {

    @JsonProperty("aid")
    private String antennaId;

    @JsonProperty("time")
    private long boardTimestamp;

    @JsonProperty("events")
    private List<MqttMessagePayloadEventDto> events;

    public String getAntennaId() {
        return antennaId;
    }

    public void setAntennaId(String antennaId) {
        this.antennaId = antennaId;
    }

    public long getBoardTimestamp() {
        return boardTimestamp;
    }

    public void setBoardTimestamp(long boardTimestamp) {
        this.boardTimestamp = boardTimestamp;
    }

    public List<MqttMessagePayloadEventDto> getEvents() {
        return events;
    }

    public void setEvents(List<MqttMessagePayloadEventDto> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "MqttMessagePayloadDto{" +
                "antennaId='" + antennaId + '\'' +
                ", boardTimestamp=" + boardTimestamp +
                ", events=" + events +
                '}';
    }
}
