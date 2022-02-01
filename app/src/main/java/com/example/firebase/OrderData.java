package com.example.firebase;

import java.io.Serializable;

public class OrderData implements Serializable {

    private String orderId;
    private String userId;
    private String requestDescription;
    private String phoneNumber ;
    private String firstLocation;
    private String lastLocation ;
    private String Date ;
    private String Time ;
    private boolean isAccept;
    private boolean  isFinished;
    private String State;
    private String providerId;

    public OrderData() {
    }

    public OrderData(String orderId, String userId, String requestDescription, String phoneNumber, String firstLocation, String lastLocation, String date, String time, boolean isAccept, boolean isFinished, String state, String providerId) {
        this.orderId = orderId;
        this.userId = userId;
        this.requestDescription = requestDescription;
        this.phoneNumber = phoneNumber;
        this.firstLocation = firstLocation;
        this.lastLocation = lastLocation;
        Date = date;
        Time = time;
        this.isAccept = isAccept;
        this.isFinished = isFinished;
        State = state;
        this.providerId = providerId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean accept) {
        isAccept = accept;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(String firstLocation) {
        this.firstLocation = firstLocation;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }


    @Override
    public String toString() {
        return "OrderData{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", requestDescription='" + requestDescription + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstLocation='" + firstLocation + '\'' +
                ", lastLocation='" + lastLocation + '\'' +
                ", Date='" + Date + '\'' +
                ", Time='" + Time + '\'' +
                ", isAccept=" + isAccept +
                ", isFinished=" + isFinished +
                ", State='" + State + '\'' +
                '}';
    }
}
