package com.todocode.ultimequiz.Models;

public class Withdraw {
    public String playerId, playerEmail, amount, points, status, paymentMethod, paymentInfo, date;

    public Withdraw(String playerId, String playerEmail, String amount, String points, String status, String paymentMethod, String paymentInfo, String date) {
        this.playerId = playerId;
        this.playerEmail = playerEmail;
        this.amount = amount;
        this.points = points;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paymentInfo = paymentInfo;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerEmail() {
        return playerEmail;
    }

    public void setPlayerEmail(String playerEmail) {
        this.playerEmail = playerEmail;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }
}
