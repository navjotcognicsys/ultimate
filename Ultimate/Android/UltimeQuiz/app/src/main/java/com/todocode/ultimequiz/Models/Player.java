package com.todocode.ultimequiz.Models;

public class Player {
    public String id, username, email_or_phone, image_url, referral_code, last_claim, login_method, device_id, facebook, twitter, instagram, blocked, member_since;
    public Integer actual_score, total_score, coins, earnings_withdrawed, earnings_actual, referrals;

    public Player(String id, String username, String email_or_phone, String image_url, String referral_code, String last_claim, String login_method, String device_id, String facebook, String twitter, String instagram, String blocked, String member_since, Integer actual_score, Integer total_score, Integer coins, Integer earnings_withdrawed, Integer earnings_actual, Integer referrals) {
        this.id = id;
        this.username = username;
        this.email_or_phone = email_or_phone;
        this.image_url = image_url;
        this.referral_code = referral_code;
        this.last_claim = last_claim;
        this.login_method = login_method;
        this.device_id = device_id;
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.blocked = blocked;
        this.member_since = member_since;
        this.actual_score = actual_score;
        this.total_score = total_score;
        this.coins = coins;
        this.earnings_withdrawed = earnings_withdrawed;
        this.earnings_actual = earnings_actual;
        this.referrals = referrals;
    }

    public Integer getReferrals() {
        return referrals;
    }

    public void setReferrals(Integer referrals) {
        this.referrals = referrals;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail_or_phone() {
        return email_or_phone;
    }

    public void setEmail_or_phone(String email_or_phone) {
        this.email_or_phone = email_or_phone;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    public String getLast_claim() {
        return last_claim;
    }

    public void setLast_claim(String last_claim) {
        this.last_claim = last_claim;
    }

    public String getLogin_method() {
        return login_method;
    }

    public void setLogin_method(String login_method) {
        this.login_method = login_method;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public String getMember_since() {
        return member_since;
    }

    public void setMember_since(String member_since) {
        this.member_since = member_since;
    }

    public Integer getActual_score() {
        return actual_score;
    }

    public void setActual_score(Integer actual_score) {
        this.actual_score = actual_score;
    }

    public Integer getTotal_score() {
        return total_score;
    }

    public void setTotal_score(Integer total_score) {
        this.total_score = total_score;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getEarnings_withdrawed() {
        return earnings_withdrawed;
    }

    public void setEarnings_withdrawed(Integer earnings_withdrawed) {
        this.earnings_withdrawed = earnings_withdrawed;
    }

    public Integer getEarnings_actual() {
        return earnings_actual;
    }

    public void setEarnings_actual(Integer earnings_actual) {
        this.earnings_actual = earnings_actual;
    }
}
