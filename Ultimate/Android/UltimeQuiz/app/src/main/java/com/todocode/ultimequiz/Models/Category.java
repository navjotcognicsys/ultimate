package com.todocode.ultimequiz.Models;

public class Category {
    public String id, name, img, number_of_subcategories, number_of_quizzes;

    public Category(String id, String name, String img, String number_of_subcategories, String number_of_quizzes) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.number_of_subcategories = number_of_subcategories;
        this.number_of_quizzes = number_of_quizzes;
    }

    public String getNumber_of_subcategories() {
        return number_of_subcategories;
    }

    public void setNumber_of_subcategories(String number_of_subcategories) {
        this.number_of_subcategories = number_of_subcategories;
    }

    public String getNumber_of_quizzes() {
        return number_of_quizzes;
    }

    public void setNumber_of_quizzes(String number_of_quizzes) {
        this.number_of_quizzes = number_of_quizzes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
