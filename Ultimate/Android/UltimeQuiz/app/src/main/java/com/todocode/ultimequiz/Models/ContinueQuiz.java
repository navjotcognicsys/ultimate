package com.todocode.ultimequiz.Models;

public class ContinueQuiz {
    public String id, quizId, quizName, quizImageUrl, subcategoryId, subcategoryName, categoryId, categoryName, playerId;
    public Integer quizNumberOfQuestions, quizNumberOfRemainingQuestions, textQuestions, imageQuestions, audioQuestions;

    public ContinueQuiz(String id, String quizId, String quizName, String quizImageUrl, String subcategoryId, String subcategoryName, String categoryId, String categoryName, String playerId, Integer quizNumberOfQuestions, Integer quizNumberOfRemainingQuestions, Integer textQuestions, Integer imageQuestions, Integer audioQuestions) {
        this.id = id;
        this.quizId = quizId;
        this.quizName = quizName;
        this.quizImageUrl = quizImageUrl;
        this.subcategoryId = subcategoryId;
        this.subcategoryName = subcategoryName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.playerId = playerId;
        this.quizNumberOfQuestions = quizNumberOfQuestions;
        this.quizNumberOfRemainingQuestions = quizNumberOfRemainingQuestions;
        this.textQuestions = textQuestions;
        this.imageQuestions = imageQuestions;
        this.audioQuestions = audioQuestions;
    }

    public Integer getTextQuestions() {
        return textQuestions;
    }

    public void setTextQuestions(Integer textQuestions) {
        this.textQuestions = textQuestions;
    }

    public Integer getImageQuestions() {
        return imageQuestions;
    }

    public void setImageQuestions(Integer imageQuestions) {
        this.imageQuestions = imageQuestions;
    }

    public Integer getAudioQuestions() {
        return audioQuestions;
    }

    public void setAudioQuestions(Integer audioQuestions) {
        this.audioQuestions = audioQuestions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getQuizImageUrl() {
        return quizImageUrl;
    }

    public void setQuizImageUrl(String quizImageUrl) {
        this.quizImageUrl = quizImageUrl;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Integer getQuizNumberOfQuestions() {
        return quizNumberOfQuestions;
    }

    public void setQuizNumberOfQuestions(Integer quizNumberOfQuestions) {
        this.quizNumberOfQuestions = quizNumberOfQuestions;
    }

    public Integer getQuizNumberOfRemainingQuestions() {
        return quizNumberOfRemainingQuestions;
    }

    public void setQuizNumberOfRemainingQuestions(Integer quizNumberOfRemainingQuestions) {
        this.quizNumberOfRemainingQuestions = quizNumberOfRemainingQuestions;
    }
}
