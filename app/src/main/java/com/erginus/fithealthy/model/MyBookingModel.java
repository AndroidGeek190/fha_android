package com.erginus.fithealthy.model;

/**
 * Created by paramjeet on 2/7/15.
 */
public class MyBookingModel {
    public  String id, fname,lname, length,ratingCount, rateAvg, image, avail_for, time, entime, conID, cochId;

    public  String getId()
    {
        return id;
    }
    public String setId(String id)
    {
       return this.id=id;
    }

    public  String getConId()
    {
        return conID;
    }
    public String setConId(String id)
    {
        return this.conID=id;
    }

    public  String getCoachId()
    {
        return cochId;
    }
    public String setCoachId(String id)
    {
        return this.cochId=id;
    }


    public String getFName() {
        return fname;
    }

    public void setFName(String fname) {
        this.fname = fname;
    }
    public String getLName() {
        return lname;
    }

    public void setLName(String lname) {
        this.lname = lname;
    }


    public String getLength() {
        return length;
    }

    public void setLength(String desc) {
        this.length = desc;
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


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image =image;
    }

    public String getAvail_for() {
        return avail_for;
    }

    public void setAvail_for(String avail_for) {
        this.avail_for =avail_for;
    }

    public String getStartDt() {
        return time;
    }

    public void setStartDt(String time) {
        this.time =time;
    }

    public String getEndDt() {
        return entime;
    }

    public void setEndDt(String time) {
        this.entime =time;
    }


}
