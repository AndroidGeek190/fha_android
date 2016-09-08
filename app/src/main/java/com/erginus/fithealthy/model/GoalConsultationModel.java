package com.erginus.fithealthy.model;

/**
 * Created by paramjeet on 3/6/15.
 */
public class GoalConsultationModel {
    public String question_tpc, question_value, questionId, quesAnsCount, userId, quesTime, image;

    public String getTopic() {
        return question_tpc;
    }

    public void setTopic(String topic) {
        this.question_tpc = topic;
    }

    public String getQuestion() {
        return question_value;
    }

    public void setQuestion(String question) {
        this.question_value = question;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String id) {
        this.questionId = id;
    }

    public String getQuesAnsCount() {
        return quesAnsCount;
    }

    public void setQuesAnsCount(String count) {
        this.quesAnsCount = count;
    }

    public String getQuesTime() {
        return quesTime;
    }

    public void setQuesTime(String time) {
        this.quesTime = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return image;
    }

    public void setUserId(String uid) {
        this.userId = uid;
    }
}







