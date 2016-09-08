package com.erginus.fithealthy.model;

import java.io.Serializable;

/**
 * Created by paramjeet on 10/7/15.
 */
public class BookingModel implements Serializable {

    String avail_id, conf_id, availFrom, availTo, topicsId, topicName;

    public  String getAvailabilityId()
    {
        return  avail_id;
    }

    public void setAvailabilityId(String id)
    {
         this.avail_id=id;
    }

    public  String getConferenceId()
    {
        return  conf_id;
    }

    public  void setConferenceId(String conf_id)
    {
        this.conf_id=conf_id;
    }

    public String getAvaialbilityFrom(){
        return availFrom;
    }

    public  void setAvailabiltyFrom(String time)
    {
        this.availFrom=time;
    }


    public String getAvaialbilityTo(){
        return availTo;
    }

    public  void setAvailabiltyTo(String time)
    {
        this.availTo=time;
    }

    public String getTopicsId(){
        return topicsId;
    }

    public  void setTopicsId(String tpcId)
    {
        this.topicsId=tpcId;
    }

    public String getTopicName(){
        return topicName;
    }

    public  void setTopicName(String tpcNm)
    {
        this.topicName=tpcNm;
    }

}
