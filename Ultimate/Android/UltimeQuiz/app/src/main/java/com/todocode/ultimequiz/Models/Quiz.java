package com.todocode.ultimequiz.Models;

public class Quiz {
    public String id, quizName, quizImageUrl, subcategoryId, subcategoryName, categoryId, categoryName;
    public Integer quizNumberOfQuestions, textquestionNumber, imagequestionNumber, audioquestionNumber;

    public Quiz(String id, String quizName, String quizImageUrl, String subcategoryId, String subcategoryName, String categoryId, String categoryName, Integer quizNumberOfQuestions, Integer textquestionNumber, Integer imagequestionNumber, Integer audioquestionNumber) {
        this.id = id;
        this.quizName = quizName;
        this.quizImageUrl = quizImageUrl;
        this.subcategoryId = subcategoryId;
        this.subcategoryName = subcategoryName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.quizNumberOfQuestions = quizNumberOfQuestions;
        this.textquestionNumber = textquestionNumber;
        this.imagequestionNumber = imagequestionNumber;
        this.audioquestionNumber = audioquestionNumber;
    }

    public Integer getTextquestionNumber() {
        return textquestionNumber;
    }

    public void setTextquestionNumber(Integer textquestionNumber) {
        this.textquestionNumber = textquestionNumber;
    }

    public Integer getImagequestionNumber() {
        return imagequestionNumber;
    }

    public void setImagequestionNumber(Integer imagequestionNumber) {
        this.imagequestionNumber = imagequestionNumber;
    }

    public Integer getAudioquestionNumber() {
        return audioquestionNumber;
    }

    public void setAudioquestionNumber(Integer audioquestionNumber) {
        this.audioquestionNumber = audioquestionNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getQuizNumberOfQuestions() {
        return quizNumberOfQuestions;
    }

    public void setQuizNumberOfQuestions(Integer quizNumberOfQuestions) {
        this.quizNumberOfQuestions = quizNumberOfQuestions;
    }
}
