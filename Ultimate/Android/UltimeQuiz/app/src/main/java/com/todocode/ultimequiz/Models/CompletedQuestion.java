package com.todocode.ultimequiz.Models;

public class CompletedQuestion {
    public String question_id, player_id, quiz_id, subcategory_id, category_id, points, question_type, date, true_answer;

    public CompletedQuestion(String question_id, String player_id, String quiz_id, String subcategory_id, String category_id, String points, String question_type, String date, String true_answer) {
        this.question_id = question_id;
        this.player_id = player_id;
        this.quiz_id = quiz_id;
        this.subcategory_id = subcategory_id;
        this.category_id = category_id;
        this.points = points;
        this.question_type = question_type;
        this.date = date;
        this.true_answer = true_answer;
    }

    public String getTrue_answer() {
        return true_answer;
    }

    public void setTrue_answer(String true_answer) {
        this.true_answer = true_answer;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(String subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
