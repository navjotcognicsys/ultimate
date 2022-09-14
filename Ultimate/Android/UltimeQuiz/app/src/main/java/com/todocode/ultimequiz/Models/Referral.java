package com.todocode.ultimequiz.Models;

public class Referral {
    public String id, playerId, playerEmail, playerUsername, playerImageUrl, referredSourceId;

    public Referral(String id, String playerId, String playerEmail, String playerUsername, String playerImageUrl, String referredSourceId) {
        this.id = id;
        this.playerId = playerId;
        this.playerEmail = playerEmail;
        this.playerUsername = playerUsername;
        this.playerImageUrl = playerImageUrl;
        this.referredSourceId = referredSourceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public String getReferredSourceId() {
        return referredSourceId;
    }

    public void setReferredSourceId(String referredSourceId) {
        this.referredSourceId = referredSourceId;
    }

    public String getPlayerImageUrl() {
        return playerImageUrl;
    }

    public void setPlayerImageUrl(String playerImageUrl) {
        this.playerImageUrl = playerImageUrl;
    }
}
