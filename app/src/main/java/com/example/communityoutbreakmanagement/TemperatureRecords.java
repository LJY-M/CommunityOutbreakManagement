package com.example.communityoutbreakmanagement;

public class TemperatureRecords {

    private String houseNumber;
    private String residentName;
    private String residentTemperature;
    private String recordsTime;

    public TemperatureRecords(String houseNumber, String residentName, String residentTemperature, String recordsTime) {
        this.houseNumber = houseNumber;
        this.residentName = residentName;
        this.residentTemperature = residentTemperature;
        this.recordsTime = recordsTime;
    }

    @Override
    public String toString() {
        return "TemperatureRecords{" +
                "houseNumber='" + houseNumber + '\'' +
                ", residentName='" + residentName + '\'' +
                ", residentTemperature='" + residentTemperature + '\'' +
                ", recordsTime='" + recordsTime + '\'' +
                '}';
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getResidentTemperature() {
        return residentTemperature;
    }

    public void setResidentTemperature(String residentTemperature) {
        this.residentTemperature = residentTemperature;
    }

    public String getRecordsTime() {
        return recordsTime;
    }

    public void setRecordsTime(String recordsTime) {
        this.recordsTime = recordsTime;
    }
}
