package com.erginus.fithealthy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.GlobalPrefrences;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.model.BookingModel;
import com.erginus.fithealthy.model.TopicModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CoachDetailActivity extends ActionBarActivity implements  Serializable{
    LinearLayout ll_back;
    RelativeLayout vdoCall, chat, groupVdoCall;
    ImageView profilePic,img_video_call;
    RatingBar ratingBar;
    String rating, id, usrNm, desc, img;
    String avail_id,conf_id, avail_for, avai_tpic, avail_from, avail_to, topic_id,topic_nm;
    int avai_for;
    List<BookingModel> one_to_one_chat_list, one_to_one_video_list,group_video_list;
    List<String> topic_nm1_list,topic_nm2_list, topic_nm3_list, topicNm1List, topicNm2List,topicNm3List;
    PrefsHelper prefsHelper;
    GlobalPrefrences globalPrefrences;
    RelativeLayout rl_rating;
    TextView txt_name,txt_desc,txt_rating, title_name,txt_purple_tokn,txt_orng_tokn,txt_blue_tokn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_coach_detail);
        ll_back=(LinearLayout)findViewById(R.id.linear_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        prefsHelper=new PrefsHelper(this);
        globalPrefrences=new GlobalPrefrences(this);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar2);
        profilePic=(ImageView)findViewById(R.id.imageViewch);
        txt_name=(TextView)findViewById(R.id.coache_name2);
        title_name=(TextView)findViewById(R.id.name);
        txt_desc=(TextView)findViewById(R.id.dscrptn);
        txt_rating=(TextView)findViewById(R.id.bar_count);
        txt_purple_tokn=(TextView)findViewById(R.id.textView_tokens1);
       // txt_blue_tokn=(TextView)findViewById(R.id.textView_token);
        txt_orng_tokn=(TextView)findViewById(R.id.textView_tokenprice);
        txt_purple_tokn.setText(globalPrefrences.getOneToOneCoaching());
       // txt_blue_tokn.setText(prefsHelper.getOneToOneChat());
        txt_orng_tokn.setText(globalPrefrences.getGroupCoaching());
        one_to_one_chat_list=new ArrayList<>();
        one_to_one_video_list=new ArrayList<>();
        group_video_list=new ArrayList<>();
       // topic_nm1_list=new ArrayList<>();
        topic_nm2_list=new ArrayList<>();
        topic_nm3_list=new ArrayList<>();
        rl_rating=(RelativeLayout)findViewById(R.id.ll_name_rate);
        img=getIntent().getStringExtra("imagePath");
        usrNm=getIntent().getStringExtra("firstName") + " " + getIntent().getStringExtra("lastName");
        desc=getIntent().getStringExtra("description");
        Picasso.with(this).load(img).into(profilePic);
        txt_name.setText(usrNm);
        title_name.setText(getIntent().getStringExtra("firstName") + " " + getIntent().getStringExtra("lastName"));
        txt_desc.setText(desc);
        txt_rating.setText("("+getIntent().getStringExtra("ratingCount")+")");
        ratingBar.setRating(Float.parseFloat(getIntent().getStringExtra("ratingAvg")));
        id=getIntent().getStringExtra("id");
        Log.d(prefsHelper.getUserIdFromPrefrence(), prefsHelper.getSecHashFromPrefrence());
        vdoCall=(RelativeLayout)findViewById(R.id.rl1_videocall);
        chat=(RelativeLayout)findViewById(R.id.rl1_121chat);
        groupVdoCall=(RelativeLayout)findViewById(R.id.rl1_grp_chat);
        vdoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avai_for=2;
               getCoachAvailabiity();
            }
        });
       /* chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avai_for=1;
             getCoachAvailabiity();
            }
        });*/

        groupVdoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avai_for=3;
             getCoachAvailabiity();
            }
        });

    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public void getCoachAvailabiity()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CoachDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Answer " + MapAppConstants.API +"/get_coach_availabilities");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/get_coach_availabilities", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    ;
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(CoachDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONArray jsonArray = object.getJSONArray("data");
                                    if(jsonArray.length()>0){

                                        for(int i=0; i<jsonArray.length();i++){
                                            JSONObject object1=jsonArray.getJSONObject(i);
                                            avail_id=object1.getString("availability_id");
                                            conf_id = object1.getString("conference_id");
                                            avail_for=object1.getString("availability_for");
                                            avai_tpic=object1.getString("topic_name");
                                            avail_from= object1.getString("availability_from");
                                            avail_to=object1.getString("availability_to");
                                            topic_id=object1.getString("topics_id");
                                            topic_nm=object1.getString("topic_name");
                                            SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            Date startDate=null;
                                            Date endDate=null;
                                            try {
                                                startDate=dt1.parse(avail_from);
                                                endDate=dt1.parse(avail_to);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            String stDate=dt.format(startDate);

                                            String enDate=dt.format(endDate);
                                            String AvailableStartDateArr[]=stDate.split(" ");
                                            String AvailableEndDateArr[]=enDate.split(" ");

                                            String startTime=AvailableStartDateArr[1]+" "+AvailableStartDateArr[2];

                                            String endTime=AvailableEndDateArr[1]+" "+AvailableEndDateArr[2];
                                           /* if(avail_for.equals("1"))
                                            {
                                                one_to_one_chat_list.add(bookingModel(avail_id,conf_id,avail_from,avail_to, topic_id,topic_nm));
                                                topic_nm1_list.add(topic_nm);
                                                topicNm1List=sortList(topic_nm1_list);

                                            }*/
                                            if (avail_for.equals("2")){
                                                one_to_one_video_list.add(bookingModel(avail_id,conf_id,avail_from,avail_to, topic_id, topic_nm));
                                                topic_nm2_list.add(topic_nm);
                                                topicNm2List=sortList(topic_nm2_list);

                                            }
                                            if(avail_for.equals("3"))
                                            {
                                                group_video_list.add(bookingModel(avail_id,conf_id,avail_from,avail_to, topic_id, topic_nm));
                                                topic_nm3_list.add(topic_nm);
                                                topicNm3List=sortList(topic_nm3_list);

                                            }
                                        }

                                    }

                                   // setChatList(one_to_one_chat_list);
                                    setVideoList(one_to_one_video_list);
                                    setGroupVideoList(group_video_list);
                                   // setTopicList(topic_id_list);
                                  //  topicList=  sortList(topic_id_list);
                                      //  Log.d("kcekurycweyrvieyr", String.valueOf(one_to_one_chat_list));
                                    Log.d("xsdudqss97393", String.valueOf(one_to_one_video_list));
                                    Log.d("xssgdhsgdy", String.valueOf(group_video_list));

                                }
                          /*      if(avail_for.equals("1")){
                                    Intent bkng=new Intent(CoachDetailActivity.this, CoachChatBookingFormActivity.class);
                                    bkng.putExtra("usrNm", usrNm);
                                    bkng.putExtra("desc",desc);
                                    bkng.putExtra("img",img);
                                    bkng.putExtra("c_list", (Serializable) one_to_one_chat_list);
                                    bkng.putExtra("t1_list", (Serializable) topicNm1List);
                                    startActivity(bkng);
                                }*/
                                if(avail_for.equals("2")){
                                    Intent bkng=new Intent(CoachDetailActivity.this, CoachVideoCallBookingFormActivity.class);
                                    bkng.putExtra("usrNm", usrNm);
                                    bkng.putExtra("desc",desc);
                                    bkng.putExtra("img",img);
                                    bkng.putExtra("v_list", (Serializable) one_to_one_video_list);
                                    bkng.putExtra("t2_list", (Serializable) topicNm2List);
                                    startActivity(bkng);
                                    finish();
                                }
                                if(avail_for.equals("3")){
                                    Intent bkng=new Intent(CoachDetailActivity.this, CoachGroupVideoCallBookingFormActivity.class);
                                    bkng.putExtra("usrNm", usrNm);
                                    bkng.putExtra("desc",desc);
                                    bkng.putExtra("img",img);
                                    bkng.putExtra("g_list", (Serializable) group_video_list);
                                    bkng.putExtra("t3_list", (Serializable) topicNm3List);
                                    startActivity(bkng);
                                    finish();
                                }

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }}

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    ;
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(CoachDetailActivity.this, "Timeout Error",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ServerError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof NetworkError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ParseError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    }
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("users_id", id);
                    params.put("availability_for", avai_for+"");
                    params.put("user_id", prefsHelper.getUserIdFromPrefrence());
                    params.put("user_security_hash", prefsHelper.getSecHashFromPrefrence());
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(500000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getApplicationContext()

            ).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String> sortList(List<String> myList){
        Set<String> hashsetList = new HashSet<String>(myList);
        List<String> sortlist=new ArrayList<>(hashsetList);
        System.out.printf("\nUnique values using HashSet: %s%n", hashsetList);
        return sortlist;
    }

    private BookingModel bookingModel(String aid,String cid, String availFro, String availTo, String tid, String tname )
    {
        BookingModel b = new BookingModel();
        b.setAvailabilityId(aid);
        b.setAvailabiltyTo(cid);
        b.setAvailabiltyFrom(availFro);
        b.setAvailabiltyTo(availTo);
        b.setTopicsId(tid);
        b.setTopicName(tname);
        return b;
    }

    private TopicModel topicModel(String tid,String topicNm)
    {
        TopicModel b = new TopicModel();
        b.setId(tid);
        b.setName(topicNm);
        return b;
    }
    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onPause() {
        super.onPause();


    }
    public List<BookingModel> getchatList() {
        return one_to_one_chat_list;
    }

    public void setChatList(List<BookingModel> list) {
        this.one_to_one_chat_list = list;
    }
    public List<BookingModel> getVideoList() {
        return one_to_one_video_list;
    }

    public void setVideoList(List<BookingModel> list) {
        this.one_to_one_video_list = list;
    }
    public List<BookingModel> getGroupVideoList() {
        return group_video_list;
    }

    public void setGroupVideoList(List<BookingModel> list) {
        this.group_video_list = list;
    }

   /* public List<TopicModel> getTopicList() {
        return topic_id_list;
    }

    public void setTopicList(List<TopicModel> list) {
        this.topic_id_list = list;
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
    }
   }
