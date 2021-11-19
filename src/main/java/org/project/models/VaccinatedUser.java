package org.project.models;

import java.io.Serializable;
import java.sql.Date;

public class VaccinatedUser implements Serializable {

    private String name;
    private String surname;
    private String nickname;
    private String event;
    private String id;
    private String hubName;
    private String fiscalCode;
    private Date vaccineDate;
    private String vaccineType;

    public VaccinatedUser(String name, String surname, String nickname, String event, String id, String hubName, String fiscalCode, Date vaccineDate, String vaccineType) {
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.event = event;
        this.id = id;
        this.hubName = hubName;
        this.fiscalCode = fiscalCode;
        this.vaccineDate = vaccineDate;
        this.vaccineType = vaccineType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHubName() {
        return hubName;
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public Date getVaccineDate() {
        return vaccineDate;
    }

    public void setVaccineDate(Date vaccineDate) {
        this.vaccineDate = vaccineDate;
    }

    public String getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(String vaccineType) {
        this.vaccineType = vaccineType;
    }

    @Override
    public String toString() {
        return "VaccinatedUser{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nickname='" + nickname + '\'' +
                ", event='" + event + '\'' +
                ", id='" + id + '\'' +
                ", hubName='" + hubName + '\'' +
                ", fiscalCode='" + fiscalCode + '\'' +
                ", vaccineDate=" + vaccineDate +
                ", vaccineType='" + vaccineType + '\'' +
                '}';
    }
}
