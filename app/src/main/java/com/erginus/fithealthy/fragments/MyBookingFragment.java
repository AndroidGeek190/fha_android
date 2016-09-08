package com.erginus.fithealthy.fragments;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.erginus.fithealthy.CoachHomeActivity;
import com.erginus.fithealthy.GoalConsultationRoomActivity;
import com.erginus.fithealthy.adapter.MyBookingAdapter;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.R;
import com.erginus.fithealthy.model.MyBookingModel;

import com.erginus.fithealthy.ui.SampleActivity;
import com.jafarkhq.views.EndlessListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyBookingFragment extends android.support.v4.app.Fragment {
    EndlessListView booking;
    List<MyBookingModel> myBookingList;
    String  ratedValue;
    PrefsHelper prefsHelper;
    MyBookingAdapter adapter;
    int page=0;
    private boolean mHaveMoreDataToLoad=true;
    LinearLayout ll1, ll2;
    String confrncId, bookingId;
    String seconds_remn, start_ssn, show_rating_dilog;
    ImageView imageView;

    public MyBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_my_booking, container, false);
        booking=(EndlessListView)rootView.findViewById(R.id.mybooking_lv);
        myBookingList=new ArrayList<MyBookingModel>();
        prefsHelper=new PrefsHelper(getActivity());
        ll1=(LinearLayout)rootView.findViewById(R.id.list);
        ll2=(LinearLayout)rootView.findViewById(R.id.image);
        imageView=(ImageView)rootView.findViewById(R.id.imageView3);
        booking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (myBookingList.get(position).getAvail_for().equals("2") || myBookingList.get(position).getAvail_for().equals("3")) {
                    confrncId = myBookingList.get(position).getConId();
                    bookingId=myBookingList.get(position).getId();
                    prefsHelper.storeBookingIdToPrefrences(bookingId);
                    prefsHelper.storeConferenceIdToPrefrences(confrncId);
                    startCall();
                }


               /* else
                {
                     *//*confrncId=myBookingList.get(position).getConId();
                    startCall(myBookingList.get(position).getId());*//*
                    Intent grup = new Intent(getActivity(), ChatActivity.class);
                    grup.putExtra("confrncId", myBookingList.get(position).getConId());
                    startActivity(grup);
                }*/
            }
        });
        myBookingList(page);
        mHaveMoreDataToLoad = true;
        booking.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (true == mHaveMoreDataToLoad) {
                    loadMoreData();
                } else {
                    Toast.makeText(getActivity(), "No more data to load", Toast.LENGTH_SHORT).show();
                }

                return mHaveMoreDataToLoad;

            }
        });

        return rootView;
    }

    private void loadMoreData() {
        //  new LoadMore().execute((Void) null);
        page++;
        myBookingList(page);
    }
    public void myBookingList(final int page)
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "CoachList " + MapAppConstants.API +"/bookings");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/bookings",new Response.Listener<String>() {
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

                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                           if ("1".equals(serverCode)) {
                                    JSONArray array = object.getJSONArray("data");

                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject objet = array.getJSONObject(i);

                                         String id, avail_for, fnm, lnm,lngth, img, dt,edt,rate, conf_id, coch_id;
                                         id=objet.getString("booking_id");
                                         avail_for=objet.getString("availability_for");
                                         fnm=objet.getString("user_first_name");
                                            lnm=objet.getString("user_last_name");
                                            img=objet.getString("user_profile_image_url");
                                            rate=objet.getString("user_rating_average");
                                            dt=objet.getString("booking_start_time");
                                            edt=objet.getString("booking_end_time");
                                            lngth=objet.getString("booking_length");
                                            conf_id=objet.getString("conference_id");
                                            coch_id=objet.getString("user_id");
                                            myBookingList.add(myBookingModel(id,avail_for,fnm,lnm,img,rate,dt,edt,lngth, conf_id,coch_id));
                                        }
                                    }
                                    else{
                                        mHaveMoreDataToLoad=false;
                                    }
                                    setTrackList(myBookingList);
                               if(page==0 && myBookingList.isEmpty())
                               {
                                   booking.setVisibility(View.GONE);
                                   ll2.setVisibility(View.VISIBLE);
                               }
                                }
                                if(booking.getAdapter()==null){
                                    adapter = new MyBookingAdapter(getActivity(), myBookingList);
                                    booking.setAdapter(adapter);
                                    Log.d("i am here at adapter", "jhjgxbexey");

                                }
                                else
                                {
                                    booking.loadMoreCompleat();
                                    adapter.notifyDataSetChanged();
                                }

                            }

                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
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
                        Toast.makeText(getActivity(), "Timeout Error",
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
                    params.put("user_id",prefsHelper.getUserIdFromPrefrence());
                    params.put("user_security_hash", prefsHelper.getSecHashFromPrefrence());
                    params.put("page",page+"");

                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setTrackList(List<MyBookingModel> list) {
        this.myBookingList= list;
    }

    private MyBookingModel myBookingModel(String id,String avail_for,String fname,String lnm,String image,String rate , String date,String edate, String length, String conId, String coachid)
    {
        MyBookingModel model = new MyBookingModel();
        model.setId(id);
        model.setAvail_for(avail_for);
        model.setFName(fname);
        model.setLName(lnm);
        model.setImage(image);
        model.setRatingAverage(rate);
        model.setStartDt(date);
        model.setEndDt(edate);
        model.setLength(length);
        model.setConId(conId);
        model.setCoachId(coachid);
        return model;
    }

    public void startCall()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "CoachList " + MapAppConstants.API + "/start");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/start",new Response.Listener<String>() {
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
                        Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                              if ("1".equals(serverCode)) {


                                    JSONObject object1=object.getJSONObject("data");
                                    seconds_remn=object1.getString("seconds_remaining");
                                    show_rating_dilog=object1.getString("show_rating_dialog");
                                    prefsHelper.storeSecondsToPrefrences(seconds_remn);
                                    prefsHelper.storeShowRatingToPrefrences(show_rating_dilog);

                                  Log.d("kscutkeutrtrvi", show_rating_dilog);

                                }
                            }

                            catch (Exception e) {
                                e.printStackTrace();
                            }

                                Intent grup = new Intent(getActivity(), SampleActivity.class);
                                startActivity(grup);

                        }
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
                        Toast.makeText(getActivity(), "Timeout Error",
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
                    params.put("user_id",prefsHelper.getUserIdFromPrefrence());
                    params.put("user_security_hash", prefsHelper.getSecHashFromPrefrence());
                    params.put("booking_id",bookingId);

                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onPause() {
        super.onPause();


    }
}
