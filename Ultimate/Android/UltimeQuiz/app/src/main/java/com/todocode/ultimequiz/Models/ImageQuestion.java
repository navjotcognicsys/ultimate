package com.todocode.ultimequiz.Models;

public class ImageQuestion {
    public String questionType, hint, question_image_url, true_answer, false1,
            false2, false3,premium_or_not;
    public Integer id, category_id, subcategory_id, quiz_id, points, seconds, rank;

    public ImageQuestion(String questionType, String hint, String question_image_url, String true_answer, String false1, String false2, String false3, String premium_or_not, Integer id, Integer category_id, Integer subcategory_id, Integer quiz_id, Integer points, Integer seconds, Integer rank) {
        this.questionType = questionType;
        this.hint = hint;
        this.question_image_url = question_image_url;
        this.true_answer = true_answer;
        this.false1 = false1;
        this.false2 = false2;
        this.false3 = false3;
        this.premium_or_not = premium_or_not;
        this.id = id;
        this.category_id = category_id;
        this.subcategory_id = subcategory_id;
        this.quiz_id = quiz_id;
        this.points = points;
        this.seconds = seconds;
        this.rank = rank;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getQuestion_image_url() {
        return question_image_url;
    }

    public void setQuestion_image_url(String question_image_url) {
        this.question_image_url = question_image_url;
    }

    public String getTrue_answer() {
        return true_answer;
    }

    public void setTrue_answer(String true_answer) {
        this.true_answer = true_answer;
    }

    public String getFalse1() {
        return false1;
    }

    public void setFalse1(String false1) {
        this.false1 = false1;
    }

    public String getFalse2() {
        return false2;
    }

    public void setFalse2(String false2) {
        this.false2 = false2;
    }

    public String getFalse3() {
        return false3;
    }

    public void setFalse3(String false3) {
        this.false3 = false3;
    }

    public String getPremium_or_not() {
        return premium_or_not;
    }

    public void setPremium_or_not(String premium_or_not) {
        this.premium_or_not = premium_or_not;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Integer getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(Integer subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public Integer getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }
}
