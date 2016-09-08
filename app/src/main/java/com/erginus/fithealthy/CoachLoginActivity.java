package com.erginus.fithealthy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.GlobalPrefrences;
import com.erginus.fithealthy.helper.PrefsHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CoachLoginActivity extends ActionBarActivity implements View.OnClickListener {
    String id="",usr_login= "", usr_email= "", usr_contc= "", hash = "", gender= "", desc="", rate="", grup_slug="" ;

    EditText userId, password;
    Button login, signUp;
    TextView forgotPwd;
    LinearLayout ll_backArrow;
    PrefsHelper prefsHelper;
    String email,pswrd;
    GlobalPrefrences globalPrefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_coach_login);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
globalPrefrences=new GlobalPrefrences(this);
        userId=(EditText)findViewById(R.id.editText_mail);
        password=(EditText)findViewById(R.id.edit_psswrd);
        login=(Button)findViewById(R.id.button_login);
        signUp=(Button)findViewById(R.id.button_signup);
        forgotPwd=(TextView)findViewById(R.id.textView_frgtpsswrd);
        ll_backArrow=(LinearLayout)findViewById(R.id.linear_back);
        prefsHelper=new PrefsHelper(this);
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPwd.setOnClickListener(this);
        ll_backArrow.setOnClickListener(this);

    }
     private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == login) {
            email = userId.getText().toString();
            pswrd = password.getText().toString();
            boolean cancelLogin = false;
            View focusView = null;

            if (!TextUtils.isEmpty(pswrd) && !isValidPassword(pswrd)) {
                password.setError(getString(R.string.invalid_password));
                focusView = password;
                cancelLogin = true;
            }
            else if (TextUtils.isEmpty(pswrd)) {
                password.setError(getString(R.string.pswrd_required));
                focusView = password;
                cancelLogin = true;
            }

            if (TextUtils.isEmpty(email)) {
                userId.setError(getString(R.string.email_required));
                focusView = userId;
                cancelLogin = true;
            }
            if (cancelLogin) {
                // error in login
                focusView.requestFocus();
            } else {
              login();
            }

    }

        if(v==signUp)
        {
            Intent i1=new Intent(CoachLoginActivity.this, SignUpActivity.class);
            startActivity(i1);
            finish();

        }
        if(v==forgotPwd)
        {
            Intent i1=new Intent(CoachLoginActivity.this, ForgotPasswordActivity.class);
            startActivity(i1);

        }
        if(v==ll_backArrow)
        {
            finish();

        }
    }
    public void login()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CoachLoginActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "LOGIN " + MapAppConstants.API +"/login");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/login", new Response.Listener<String>() {
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
                        Toast.makeText(CoachLoginActivity.this,""+serverMessage,Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                       if (serverCode.equalsIgnoreCase("1")) {
                        try {

                            if ("1".equals(serverCode)) {
                                JSONObject object1 = object.getJSONObject("data");
                                String image="",fname, lname, trmsAccpt;
                                id = object1.getString("user_id");
                                hash = object1.getString("user_security_hash");
                                usr_email = object1.getString("user_email");
                                usr_contc = object1.getString("user_primary_contact");
                                usr_login = object1.getString("user_login");
                                gender = object1.getString("user_gender");
                                desc = object1.getString("user_description");
                                image=object1.getString("user_profile_image_url");
                                rate=object1.getString("user_rating_average");
                                fname=object1.getString("user_first_name");
                                lname=object1.getString("user_last_name");
                                grup_slug=object1.getString("group_slug");
                                trmsAccpt=object1.getString("terms_accepted");
                                Log.d(usr_email, usr_login);
                                prefsHelper.storeHashToPrefrences(hash);
                                prefsHelper.storeUserIdToPreference(id);
                                prefsHelper.storeUserImageBitmapToPreference(image);
                                prefsHelper.storeEmailToPreference(usr_email);
                                prefsHelper.storeRatingToPreference(rate);
                                prefsHelper.storeCoachDescToPrefrence(desc);
                                prefsHelper.storeUserFNameToPrefrence(fname);
                                prefsHelper.storeUserLNameToPrefrence(lname);
                                prefsHelper.storeUserNameToPreference(usr_login);
                                globalPrefrences.storeGroupSlug(grup_slug);
                                globalPrefrences.storeTermsAcceptedToPrefrence(trmsAccpt);


                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                           if(globalPrefrences.getGroupSlug().equals("coach"))
                           {
                               if(globalPrefrences.getTermsAcceptd().equals("1")) {
                                   Intent fb = new Intent(CoachLoginActivity.this, CoachHomeActivity.class);
                                   startActivity(fb);
                                   finish();
                               }
                               else
                               {
                                   Intent fb = new Intent(CoachLoginActivity.this, TermsAndConditionActivity.class);
                                   startActivity(fb);
                                   finish();
                               }
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
                        Toast.makeText(CoachLoginActivity.this, "Timeout Error",
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

                    params.put("user_login", email);
                    params.put("user_login_password", pswrd);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);


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
