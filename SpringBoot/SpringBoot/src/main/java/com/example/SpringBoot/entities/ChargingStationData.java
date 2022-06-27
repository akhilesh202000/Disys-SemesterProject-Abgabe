package com.example.SpringBoot.entities;

import java.util.Date;

public class ChargingStationData {

    public int stationId;
    public int customerId;
    public int kwh;
    public Date datetime;

    public ChargingStationData(int stationId, int customerId, int kwh, Date datetime) {
        this.stationId = stationId;
        this.customerId = customerId;
        this.kwh = kwh;
        this.datetime = datetime;
    }

    public ChargingStationData() {
    }

    @Override
    public String toString() {
        return  "Charging Station Nr.: " + stationId +
                ", kwh=" + kwh +
                ", Date: " + datetime;
    }
}
