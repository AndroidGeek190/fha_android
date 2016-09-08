package com.erginus.fithealthy.model;

/**
 * Created by paramjeet on 2/7/15.
 */
public class CoachesModel {
    public  String id, fname,lname, desc,ratingCount, rateAvg, image;

    public  String getId()
    {
        return id;
    }
    public String setId(String id)
    {
       return this.id=id;
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


    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
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

}
