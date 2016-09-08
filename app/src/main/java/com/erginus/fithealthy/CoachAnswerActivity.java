package com.erginus.fithealthy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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


public class CoachAnswerActivity extends ActionBarActivity {

    List<AnswersModel> answerList;
    LinearLayout ll_back;
    EndlessListView answersListView;
    Button btn_answer;
    ListView answers;
    PrefsHelper prefsHelper;
    EditText edtxt_answer;
    TextView topic, desc, time,textError;
    String id;
    int page=0;
    private boolean mHaveMoreDataToLoad;
    AnswersAdapter answersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_coach_answer);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        answersListView = (EndlessListView) findViewById(R.id.answr_lv);
        ll_back = (LinearLayout) findViewById(R.id.linear_back);
        textError=(TextView)findViewById(R.id.textView5);
        prefsHelper=new PrefsHelper(this);
        answerList=new ArrayList<>();
        edtxt_answer=(EditText)findViewById(R.id.edt_answer);
        btn_answer=(Button)findViewById(R.id.btn_post);
        topic=(TextView)findViewById(R.id.txt_qstn);
        desc=(TextView)findViewById(R.id.txtvw_qstn);
        time=(TextView)findViewById(R.id.txtvw_time_no);
        topic.setText(getIntent().getStringExtra("topic"));
        desc.setText(getIntent().getStringExtra("question"));
        time.setText(getIntent().getStringExtra("count")+" "+"answers-"+" "+getIntent().getStringExtra("time"));
        id=getIntent().getStringExtra("id");
         Log.d("question id", id);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer();

            }
        });
        Log.d(prefsHelper.getUserIdFromPrefrence(), prefsHelper.getSecHashFromPrefrence());
        answerList(page);
        mHaveMoreDataToLoad = true;
        answersListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (true == mHaveMoreDataToLoad) {
                    loadMoreData();
                } else {
                    Toast.makeText(CoachAnswerActivity.this, "No more data to load",
                            Toast.LENGTH_SHORT).show();
                }

                return mHaveMoreDataToLoad;

            }
        });
        answersListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;

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
    public void answerList(final  int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CoachAnswerActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "CoachList " + MapAppConstants.API +"/answers_list");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstants.API +"/answers_list", new Response.Listener<String>() {
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
                            Toast.makeText(CoachAnswerActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();

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
                                        answersAdapter = new AnswersAdapter(CoachAnswerActivity.this, answerList);
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
                        Toast.makeText(CoachAnswerActivity.this, "Timeout Error",
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
                    params.put("page",""+page);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(CoachAnswerActivity.this).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void loadMoreData() {
        //  new LoadMore().execute((Void) null);
        page++;
        answerList(page);
    }
    public void answer()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CoachAnswerActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Answer " + MapAppConstants.API +"/answer");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/answer", new Response.Listener<String>() {
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
                        Toast.makeText(CoachAnswerActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject object1 = object.getJSONObject("data");

                                    String id="",answer= "", ansr_time="";
                                    id=object1.getString("questions_id");
                                    answer = object1.getString("answer_value");
                                    ansr_time=object1.getString("answer_created");
                                    Log.d(id, answer + ansr_time);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                           edtxt_answer.setText("");
                            Intent intent=new Intent(CoachAnswerActivity.this, CoachHomeActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                           finish();

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
                        Toast.makeText(CoachAnswerActivity.this, "Timeout Error",
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
                    params.put("user_id", prefsHelper.getUserIdFromPrefrence());
                    params.put("user_security_hash", prefsHelper.getSecHashFromPrefrence());
                    params.put("questions_id",id);
                    params.put("answer_value", edtxt_answer.getText().toString());
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
    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
