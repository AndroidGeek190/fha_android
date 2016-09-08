package com.erginus.fithealthy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
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
import com.erginus.fithealthy.adapter.AnswersAdapter;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.model.AnswersModel;
import com.jafarkhq.views.EndlessListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnswersListActivity extends ActionBarActivity {
    List<AnswersModel> answerList;
    LinearLayout ll_back;
    EndlessListView answersListView;
    PrefsHelper prefsHelper;
    TextView topic, desc, time, textError;
    String id;
    int page=0;
    private boolean mHaveMoreDataToLoad;
    AnswersAdapter answersAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_answers);
        answersListView = (EndlessListView) findViewById(R.id.answr_lv);
        ll_back = (LinearLayout) findViewById(R.id.linear_back);
        prefsHelper=new PrefsHelper(this);
        answerList=new ArrayList<>();
        topic=(TextView)findViewById(R.id.txt_qstn);
        desc=(TextView)findViewById(R.id.txtvw_qstn);
        time=(TextView)findViewById(R.id.txtvw_time_no);
        textError=(TextView)findViewById(R.id.textView5);
        topic.setText(getIntent().getStringExtra("topic"));
        desc.setText(getIntent().getStringExtra("question"));
        time.setText(getIntent().getStringExtra("count")+" "+"answers-"+" "+getIntent().getStringExtra("time"));
        id=getIntent().getStringExtra("id");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        answerList(page);
        mHaveMoreDataToLoad = true;
        answersListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (true == mHaveMoreDataToLoad) {
                    loadMoreData();
                } else {
                    Toast.makeText(AnswersListActivity.this, "No more data to load",
                            Toast.LENGTH_SHORT).show();
                }

                return mHaveMoreDataToLoad;

            }
        });
        answersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent coach = new Intent(AnswersListActivity.this, CoachDetailActivity.class);
                coach.putExtra("id", answerList.get(position).getUserID());
                coach.putExtra("firstName", answerList.get(position).getFname());
                coach.putExtra("lastName", answerList.get(position).getLname());
                coach.putExtra("description", answerList.get(position).getDescription());
                coach.putExtra("imagePath", answerList.get(position).getImage());
                coach.putExtra("ratingCount", answerList.get(position).getRatingCount());
                coach.putExtra("ratingAvg", answerList.get(position).getRatingAverage());
                startActivity(coach);
            }
        });


    }
    public List<AnswersModel> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<AnswersModel> answerList) {
        this.answerList = answerList;
    }
    private AnswersModel answersModel(String ansID,String usrId, String fname, String lname, String desc,  String queAnsCount, String answer, String image, String rate)
    {
        AnswersModel answersModel = new AnswersModel();
        answersModel.setID(ansID);
        answersModel.setUserID(usrId);
        answersModel.setFname(fname);
        answersModel.setLname(lname);
        answersModel.setDescription(desc);
        answersModel.setRatingAverage(queAnsCount);
        answersModel.setAnswer(answer);
        answersModel.setImage(image);
        answersModel.setRatingCount(rate);
        return answersModel;
    }
    private void loadMoreData() {
        //  new LoadMore().execute((Void) null);
        page++;
        answerList(page);
    }
    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onPause() {
        super.onPause();


    }
    public void answerList(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(AnswersListActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "CoachList " + MapAppConstants.API +"/answers_list");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstants.API + "/answers_list", new Response.Listener<String>() {
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
                            Toast.makeText(AnswersListActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();

                        }
                        if (serverCode.equalsIgnoreCase("1")) {

                            try {
                              if ("1".equals(serverCode)) {
                                    JSONArray array = object.getJSONArray("data");

                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject objet = array.getJSONObject(i);
                                            String id = "",fname = "",usrId, lname="" ,desc = "",rate, count = "", answer = "", image="";
                                            id = objet.getString("answer_id");
                                            usrId=objet.getString("user_id");
                                            fname = objet.getString("user_first_name");
                                            lname = objet.getString("user_last_name");
                                            desc= objet.getString("user_description");
                                            count = objet.getString("user_rating_average");
                                            answer = objet.getString("answer_value");
                                            image=objet.getString("user_profile_image_url");
                                             rate=objet.getString("user_rating_count");
                                            answerList.add(answersModel(id,usrId, fname,lname,desc, count,answer, image, rate));

                                        }
                                    }   else{
                                        mHaveMoreDataToLoad=false;
                                    }

                                    setAnswerList(answerList);
                                 /* if(page==0 && answerList.isEmpty())
                                  {
                                      answersListView.setVisibility(View.GONE);
                                      textError.setVisibility(View.VISIBLE);
                                  }*/
                                }
                                    if(answersListView.getAdapter()==null){
                                        answersAdapter = new AnswersAdapter(AnswersListActivity.this, answerList);
                                        answersListView.setAdapter(answersAdapter);

                                    }
                                    else{
                                        answersListView.loadMoreCompleat();
                                        answersAdapter.notifyDataSetChanged();
                                    }
                                    Log.d("i am here at adapter", "jhjgxbexey");

                               } catch (Exception e) {
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
                        Toast.makeText(AnswersListActivity.this, "Timeout Error",
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
                    params.put("questions_id",id );
                    params.put("page", ""+page);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(AnswersListActivity.this).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
