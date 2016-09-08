package com.erginus.fithealthy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.GlobalPrefrences;
import com.erginus.fithealthy.helper.PrefsHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SurveyActivity extends Activity {

    TextView survey;
    Button submit;
    GlobalPrefrences globalPrefrences;
    LinearLayout linearLayout;
    EditText editText;
    PrefsHelper prefsHelper;
    String terms_accptd, survey_msg, text;
    String grup_slug = "";
    private SharedPreferences mPrefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        editText=(EditText)findViewById(R.id.editText);
        survey=(TextView)findViewById(R.id.textView6);
        globalPrefrences=new GlobalPrefrences(this);
        prefsHelper=new PrefsHelper(this);
        String surveyMessage= globalPrefrences.getSurveyMessage();
        linearLayout=(LinearLayout)findViewById(R.id.linear_back);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SurveyActivity.this, TermsAndConditionActivity.class);
                startActivity(intent);
                finish();
            }
        });
        survey.setText(surveyMessage);
        submit=(Button)findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View focusView = null;
                boolean cancelLogin = false;
                text= editText.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    editText.setError("Please fill the required details");
                    focusView = editText;
                    cancelLogin = true;
                }
                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                }
                else
            {
                survey();
            }

            }
        });

    }
    public void survey()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(SurveyActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "LOGIN " + MapAppConstants.API + "/survey");
            StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST,MapAppConstants.API+"/survey", new com.android.volley.Response.Listener<String>() {
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
                        Toast.makeText(SurveyActivity.this,""+serverMessage,Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

                                    JSONObject jsonObject = object.getJSONObject("data");
                                    terms_accptd = jsonObject.getString("terms_accepted");
                                    survey_msg = jsonObject.getString("survey_remarks");
                                    grup_slug=jsonObject.getString("group_slug");
                                    globalPrefrences.storeTermsAcceptedToPrefrence(terms_accptd);
                                    globalPrefrences.storeGroupSlug(grup_slug);
                                    Log.d("surveyyyyyyyyyyyyyy", survey_msg);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(grup_slug.equals("user")&& terms_accptd.equals("1")) {
                                Intent fb = new Intent(SurveyActivity.this, GoalConsultationRoomActivity.class);
                                startActivity(fb);
                                finish();
                            }
                            else if(grup_slug.equals("coach")&& terms_accptd.equals("1"))
                            {
                                Intent fb = new Intent(SurveyActivity.this, CoachHomeActivity.class);
                                startActivity(fb);
                                finish();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    ;
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(SurveyActivity.this, "Timeout Error",
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
                    params.put("survey_remarks",text);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(SurveyActivity.this.getApplicationContext()).addToRequestQueue(sr);


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
