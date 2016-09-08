package com.erginus.fithealthy.fragments;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.erginus.fithealthy.InputWeightDetailViewActivity;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.R;
import com.erginus.fithealthy.adapter.TrackingProgressAdapter;
import com.erginus.fithealthy.model.TrackingProgressModel;
import com.jafarkhq.views.EndlessListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TrackingProgressFragment extends android.support.v4.app.Fragment {
    List<TrackingProgressModel> trackList;
    LinearLayout ll_back;
    TrackingProgressAdapter adapter;
    EndlessListView trackingListView;
    PrefsHelper prefsHelper;
    int page=0;
    TextView textError;
    private boolean mHaveMoreDataToLoad=true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tracking_progress, container, false);

        trackingListView = (EndlessListView) rootView.findViewById(R.id.trckprgrss_lv);
        trackList = new ArrayList<TrackingProgressModel>();
        prefsHelper=new PrefsHelper(getActivity());
        textError=(TextView)rootView.findViewById(R.id.textView5);
        trackingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent trck = new Intent(getActivity(), InputWeightDetailViewActivity.class);
                trck.putExtra("image", trackList.get(position).getImage());
                trck.putExtra("weight",trackList.get(position).getWeight());
                trck.putExtra("bmi", trackList.get(position).getBmi());
                trck.putExtra("bdyFat",trackList.get(position).getBodyFat());
                trck.putExtra("calCon", trackList.get(position).getCalConsumed());
                trck.putExtra("calBurn",trackList.get(position).getCalBurned());
                trck.putExtra("fatCon", trackList.get(position).getFatConsumed());
                trck.putExtra("protCon",trackList.get(position).getProteinCons());
                trck.putExtra("bdyArea", trackList.get(position).getBodyArea());
                trck.putExtra("measDiff",trackList.get(position).getMeasureDiff());
                trck.putExtra("sports", trackList.get(position).getSportsPerf());
                trck.putExtra("distanc",trackList.get(position).getDistancePerf());
                trck.putExtra("time", trackList.get(position).getTimePerf());
                trck.putExtra("postion",trackList.get(position).getPositionPerf());
                trck.putExtra("winLose", trackList.get(position).getWinLoosePerf());
                trck.putExtra("training",trackList.get(position).getTrainingSession());
                trck.putExtra("exercise", trackList.get(position).getExercisePerf());
                trck.putExtra("load",trackList.get(position).getLoadPerformance());
                trck.putExtra("repetition", trackList.get(position).getAvgReptition());
                trck.putExtra("sets",trackList.get(position).getAvgSets());
                trck.putExtra("pace", trackList.get(position).getAvgPace());
                trck.putExtra("heartRt",trackList.get(position).getAvgHeartRate());
                trck.putExtra("watt", trackList.get(position).getAvgWatt());
                trck.putExtra("cadence",trackList.get(position).getAvgCadence());
                trck.putExtra("recvry", trackList.get(position).getRecoverySess());
                trck.putExtra("flexblty",trackList.get(position).getFlexibilitySess());
                trck.putExtra("date",trackList.get(position).getTrackingDate());


                startActivity(trck);
             //  getActivity().overridePendingTransition(R.anim.slide_in_right_t, R.anim.slide_out_left_t);


            }
        });
        trackingProgressList(page);
        mHaveMoreDataToLoad = true;
        trackingListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (true == mHaveMoreDataToLoad) {
                    loadMoreData();
                } else {
                    Toast.makeText(getActivity(), "No more data to load",
                            Toast.LENGTH_SHORT).show();
                }

                return mHaveMoreDataToLoad;

            }
        });

        return rootView;

    }
    private void loadMoreData() {
        //  new LoadMore().execute((Void) null);
        page++;
        trackingProgressList(page);
    }

    public void trackingProgressList(final int page)
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "CoachList " + MapAppConstants.API + "/tracking_progress");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/tracking_progress",new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    ;
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage=object.getString("message");
                       if (serverCode.equalsIgnoreCase("0")) {
                           Toast.makeText(getActivity(),""+serverMessage,Toast.LENGTH_SHORT).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {

                            try {
                              if ("1".equals(serverCode)) {
                                    JSONArray array = object.getJSONArray("data");

                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject objet = array.getJSONObject(i);

                                            String thmbImg,date, weight, bmi,bdyFat,calCons,caburn,fatCons,proteinC,bdyArea,measDiff,sports, distance, time, position, winloose,traning,exercise,load, avgRep,avgSet, avgPace,avgHeartRt,avgwatt,avgCad,recovry,flexiblty, image;
                                            date=objet.getString("tracking_created_date");
                                            weight=objet.getString("weight");
                                            bmi=objet.getString("bmi");
                                            bdyFat=objet.getString("body_fat");
                                            calCons=objet.getString("calories_consumed");
                                            caburn=objet.getString("calories_burned");
                                            fatCons=objet.getString("fat_consumed");
                                            proteinC=objet.getString("protein_consumed");
                                            bdyArea=objet.getString("body_area");
                                            measDiff=objet.getString("measurement_difference");
                                            sports=objet.getString("sports_performance");
                                            distance=objet.getString("distance_performance");
                                            time=objet.getString("time_performance");
                                            position=objet.getString("position_performance");
                                            winloose=objet.getString("win_lose_performance");
                                            traning=objet.getString("training_sessions_performance");
                                            exercise=objet.getString("exercise_performance");
                                            load=objet.getString("load_performance");
                                            avgRep=objet.getString("average_repetitions_performance");
                                            avgSet=objet.getString("average_sets_performance");
                                            avgPace=objet.getString("average_pace_performance");
                                            avgHeartRt=objet.getString("average_heart_rate_performance");
                                            avgwatt=objet.getString("average_watts_performance");
                                            avgCad=objet.getString("average_cadence");
                                            recovry=objet.getString("recovery_sessions");
                                            flexiblty=objet.getString("flexibility_sessions");
                                            image=objet.getString("tracking_image_url");
                                            thmbImg=objet.getString("tracking_thumb_url");
                                            trackList.add(trackModel(date,weight, bmi,bdyFat,calCons,caburn,fatCons,proteinC,bdyArea,measDiff,sports, distance, time, position, winloose,traning,exercise,load, avgRep,avgSet, avgPace,avgHeartRt,avgwatt,avgCad,recovry,flexiblty, image,thmbImg));
                                           }
                                    }
                                    else{
                                        mHaveMoreDataToLoad=false;
                                    }
                                    setTrackList(trackList);
                                 /* if(page==0 && trackList.isEmpty())
                                  {
                                      trackingListView.setVisibility(View.GONE);
                                      textError.setVisibility(View.VISIBLE);
                                  }*/
                                }
                                if(trackingListView.getAdapter()==null){
                                    adapter = new TrackingProgressAdapter(getActivity(), trackList);
                                    trackingListView.setAdapter(adapter);
                                    Log.d("i am here at adapter", "jhjgxbexey");

                                }
                                else
                                {
                                    trackingListView.loadMoreCompleat();
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
    public void setTrackList(List<TrackingProgressModel> list) {
        this.trackList= list;
    }

    private TrackingProgressModel trackModel(String date,String weight,String bmi, String bdyFat, String calCons,String caburn, String fatCons, String proteinC,String bdyArea
            ,String measDiff,String sports, String distance, String time,String position, String winloose, String traning,String exercise,
            String load, String avgRep, String avgSet,String avgPace, String avgHeartRt, String avgwatt,String avgCad,
            String recovry, String flexiblty,String image, String thumb)
    {
        TrackingProgressModel model = new TrackingProgressModel();
        model.setTrackingDate(date);
        model.setWeight(weight);
        model.setBmi(bmi);
        model.setBodyFat(bdyFat);
        model.setCalConsumed(calCons);
        model.setCalBurned(caburn);
        model.setFatConsumed(fatCons);
        model.setProteinCons(proteinC);
        model.setBodyArea(bdyArea);
        model.setMeasureDiff(measDiff);
        model.setSportsPerf(sports);
        model.setDistancePerf(distance);
        model.setPositionPerf(position);
        model.setTimePerf(time);
        model.setWinLoosePerf(winloose);
        model.setTraingSession(traning);
        model.setExercisePerf(exercise);
        model.setLoadPerformance(load);
        model.setAvgRepition(avgRep);
        model.setAvgSets(avgSet);
        model.setAvgPace(avgPace);
        model.setAvgHeartRate(avgHeartRt);
        model.setAvgWatt(avgwatt);
        model.setAvgCadence(avgCad);
        model.setRecoverySess(recovry);
        model.setFlexibilitySess(flexiblty);
        model.setImage(image);
        model.setThumbImage(thumb);
        return model;
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