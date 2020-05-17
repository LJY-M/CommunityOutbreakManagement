package com.example.communityoutbreakmanagement;

import java.io.Serializable;

class Resident implements Serializable {

    public String identityAuthentication = "identityAuthentication";

    private String houseNumber;
    private String residentName;
    private String residentPassword;
    private String residentPhone;

    public String getResidentPhone() {
        return residentPhone;
    }

    public void setResidentPhone(String residentPhone) {
        this.residentPhone = residentPhone;
    }

    public String getResidentPassword() {
        return residentPassword;
    }

    public void setResidentPassword(String residentPassword) {
        this.residentPassword = residentPassword;
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

    public Resident(String houseNumber, String residentName, String residentPassword, String residentPhone) {
        this.houseNumber = houseNumber;
        this.residentName = residentName;
        this.residentPassword = residentPassword;
        this.residentPhone = residentPhone;
    }

    @Override
    public String toString() {
        return "Resident{" +
                "houseNumber='" + houseNumber + '\'' +
                ", residentName='" + residentName + '\'' +
                ", residentPassword='" + residentPassword + '\'' +
                ", residentPhone='" + residentPhone + '\'' +
                '}';
    }
}
