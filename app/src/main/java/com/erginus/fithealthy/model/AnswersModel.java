package com.erginus.fithealthy.model;

/**
 * Created by paramjeet on 3/6/15.
 */
public class AnswersModel {
    public String ansID, userId,ans_value,quesAnsCount, usrNm, description, image, fname, lname, ratingCount,rateAvg;

    public String getID() {
        return ansID;
    }

    public void setID(String id) {
        this.ansID = id;
    }
    public String getUserID() {
        return userId;
    }

    public void setUserID(String id) {
        this.userId = id;
    }

    public String getAnswer() {
        return ans_value;
    }

    public void setAnswer(String annswer) {
        this.ans_value = annswer;
    }


    public String getQuesAnsCount() {
        return quesAnsCount;
    }

    public void setQuesAnsCount(String count) {
        this.quesAnsCount = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description= description;
    }



    public String getUserName() {
        usrNm=fname+ " " +lname;
        return  usrNm;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }
    public String getRatingAverage() {
        return rateAvg;
    }

    public void setRatingAverage(String rateAvg) {
        this.rateAvg = rateAvg;
    }

    public void setUserName(String name) {
        this.usrNm = name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname= fname;
    }
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String img) {
        this.image=img;
    }
}







