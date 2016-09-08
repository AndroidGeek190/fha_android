package com.erginus.fithealthy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.erginus.fithealthy.commons.ConnectionDetector;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.GlobalPrefrences;
import com.erginus.fithealthy.helper.PrefsHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends Activity {
    GlobalPrefrences gPrefsHelper;
    PrefsHelper prefsHelper;
    private ConnectionDetector cd;
    public static String token, image,login,email,phone,gender, grup_slug, termsAccptd;
    String id = "",trmsAccpt, rate_msg = "",term_msg="", survey_msg = "", gmtMsg="", broadMsg="",chat="",grupcoaching="", oneToOnecoachng="",free_tokn="", token_msg="",stoken1="", stoken2="", stoken3="", stokn1price="", stokn2price="", stokn3price="", mtoken1="", mtoken2="", mtoken3="", mtokn1price="",mtoken2price="",mtoken3price="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        gPrefsHelper=new GlobalPrefrences(this);
        prefsHelper=new PrefsHelper(this);
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            AlertDialog alertDialog=new AlertDialog.Builder(StartActivity.this).create();
            alertDialog.setTitle("Alert!");
            alertDialog.setMessage("Internet Connection Error, Please connect to working Internet connection");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog,int which)
                {
                    Toast.makeText(getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });

            // Showing Alert Message
            alertDialog.show();

        }
        if(cd.isConnectingToInternet()) {
            loginToApp();
        }
    }

    public void loginToApp()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(StartActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            Log.e("", "LOGIN " + MapAppConstants.API + "/");
            StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST,MapAppConstants.API+"/", new com.android.volley.Response.Listener<String>() {
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
                            Toast.makeText(StartActivity.this,""+serverMessage,Toast.LENGTH_SHORT).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject jsonObject = object.getJSONObject("data");
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("configurations");
                                    free_tokn=jsonObject1.getString("free_tokens_per_month");
                                    token_msg=jsonObject1.getString("token_message");
                                    stoken1=jsonObject1.getString("single_token_one");
                                    stokn1price=jsonObject1.getString("single_token_one_price");
                                    stoken2=jsonObject1.getString("single_token_two");
                                    stokn2price=jsonObject1.getString("single_token_two_price");
                                    stoken3=jsonObject1.getString("single_token_three");
                                    stokn3price=jsonObject1.getString("single_token_three_price");
                                    mtoken1=jsonObject1.getString("monthly_token_one");
                                    mtokn1price=jsonObject1.getString("monthly_token_one_price");
                                    rate_msg=jsonObject1.getString("rate_message");
                                    mtoken2=jsonObject1.getString("monthly_token_two");
                                    mtoken2price=jsonObject1.getString("monthly_token_two_price");
                                    mtoken3=jsonObject1.getString("monthly_token_three");
                                    mtoken3price=jsonObject1.getString("monthly_token_three_price");
                                    gmtMsg=jsonObject1.getString("gmt_message");
                                    broadMsg=jsonObject1.getString("broadband_message");
                                    oneToOnecoachng=jsonObject1.getString("one_to_one_coaching");
                                    chat=jsonObject1.getString("one_to_one_chat");

                                    grupcoaching=jsonObject1.getString("group_coaching");
                                    term_msg=jsonObject1.getString("terms_message");
                                    survey_msg=jsonObject1.getString("survey_message");
                                    gPrefsHelper.storeFreeTokenPerMonth(free_tokn);
                                    gPrefsHelper.storeSingleTokenOne(stoken1);
                                    gPrefsHelper.storeSingleTokenOnePrice(stokn1price);
                                    gPrefsHelper.storeSingleTokenTwo(stoken2);
                                    gPrefsHelper.storeSingleTokenTwoPrice(stokn2price);
                                    gPrefsHelper.storeSingleTokenThree(stoken3);
                                    gPrefsHelper.storeSingleTokenThreePrice(stokn3price);
                                    gPrefsHelper.storeMonthlyTokenOne(mtoken1);
                                    gPrefsHelper.storeMonthlyTokenOnePrice(mtokn1price);
                                    gPrefsHelper.storeMonthlyTokenTwo(mtoken2);
                                    gPrefsHelper.storeMonthlyTokenTwoPrice(mtoken2price);
                                    gPrefsHelper.storeMonthlyTokenThree(mtoken3);
                                    gPrefsHelper.storeMonthlyTokenThreePrice(mtoken3price);
                                    gPrefsHelper.storeGMTMessageToPrefrence(gmtMsg);
                                    gPrefsHelper.storeBroadbndMsgToPref(broadMsg);
                                    gPrefsHelper.storeTokenMessageToPrefrence(token_msg);
                                    gPrefsHelper.storeOneToOneChat(chat);
                                    gPrefsHelper.storeOnetoOneCoaching(oneToOnecoachng);
                                    gPrefsHelper.storeGroupCoaching(grupcoaching);
                                    gPrefsHelper.storeTermMessageToPrefrence(term_msg);
                                    gPrefsHelper.storeSurveyMessageToPrefrence(survey_msg);


                                    Log.e(chat, grupcoaching);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(!(prefsHelper.getUserIdFromPrefrence().equals("")) ||!(prefsHelper.getSecHashFromPrefrence().equals(""))) {
                                sessionLogin();
                            }
                            else
                            {
                                Intent fb = new Intent(StartActivity.this, LoginActivity.class);
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
                        Toast.makeText(StartActivity.this, "Timeout Error",
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

                    params.put("current_timestamp", "1");
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES*2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(StartActivity.this).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void sessionLogin()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(StartActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "LOGIN " + MapAppConstants.API + "/session_login");
            StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST,MapAppConstants.API+"/session_login", new com.android.volley.Response.Listener<String>() {
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
                            Toast.makeText(StartActivity.this,""+serverMessage,Toast.LENGTH_SHORT).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    String id = "",free_tokn_avail="", hash = "",fname, lname;
                                    JSONObject jsonObject = object.getJSONObject("data");
                                    id = jsonObject.getString("user_id");
                                    hash = jsonObject.getString("user_security_hash");
                                    fname=jsonObject.getString("user_first_name");
                                    lname=jsonObject.getString("user_last_name");
                                    image=jsonObject.getString("user_profile_image_url");
                                    token=jsonObject.getString("user_token_count");
                                    login=jsonObject.getString("user_login");
                                    phone=jsonObject.getString("user_primary_contact");
                                    gender=jsonObject.getString("user_gender");
                                    email=jsonObject.getString("user_email");
                                    grup_slug=jsonObject.getString("group_slug");
                                    free_tokn_avail=jsonObject.getString("user_free_tokens_available");

                                    prefsHelper.storeUserIdToPreference(id);
                                    prefsHelper.storeHashToPrefrences(hash);
                                    prefsHelper.storeUserImageBitmapToPreference(image);
                                    prefsHelper.storeUserNameToPreference(login);
                                    prefsHelper.storeTokenToPreference(token);
                                    prefsHelper.storeRedeemToPreference(free_tokn_avail);
                                    prefsHelper.storeEmailToPreference(email);
                                    prefsHelper.storeGenderToPreference(gender);
                                    prefsHelper.storePhoneToPreference(phone);
                                    prefsHelper.storeUserFNameToPrefrence(fname);
                                    prefsHelper.storeUserLNameToPrefrence(lname);
                                    termsAccptd=jsonObject.getString("terms_accepted");
                                    gPrefsHelper.storeTermsAcceptedToPrefrence(termsAccptd);
                                    gPrefsHelper.storeGroupSlug(grup_slug);
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("configurations");
                                    free_tokn=jsonObject1.getString("free_tokens_per_month");
                                    token_msg=jsonObject1.getString("token_message");
                                    stoken1=jsonObject1.getString("single_token_one");
                                    stokn1price=jsonObject1.getString("single_token_one_price");
                                    stoken2=jsonObject1.getString("single_token_two");
                                    stokn2price=jsonObject1.getString("single_token_two_price");
                                    stoken3=jsonObject1.getString("single_token_three");
                                    stokn3price=jsonObject1.getString("single_token_three_price");
                                    mtoken1=jsonObject1.getString("monthly_token_one");
                                    mtokn1price=jsonObject1.getString("monthly_token_one_price");
                                    rate_msg=jsonObject1.getString("rate_message");
                                    mtoken2=jsonObject1.getString("monthly_token_two");
                                    mtoken2price=jsonObject1.getString("monthly_token_two_price");
                                    mtoken3=jsonObject1.getString("monthly_token_three");
                                    mtoken3price=jsonObject1.getString("monthly_token_three_price");
                                    gmtMsg=jsonObject1.getString("gmt_message");
                                    broadMsg=jsonObject1.getString("broadband_message");
                                    oneToOnecoachng=jsonObject1.getString("one_to_one_coaching");
                                    chat=jsonObject1.getString("one_to_one_chat");

                                    grupcoaching=jsonObject1.getString("group_coaching");
                                    term_msg=jsonObject1.getString("terms_message");
                                    survey_msg=jsonObject1.getString("survey_message");
                                    gPrefsHelper.storeFreeTokenPerMonth(free_tokn);
                                    gPrefsHelper.storeSingleTokenOne(stoken1);
                                    gPrefsHelper.storeSingleTokenOnePrice(stokn1price);
                                    gPrefsHelper.storeSingleTokenTwo(stoken2);
                                    gPrefsHelper.storeSingleTokenTwoPrice(stokn2price);
                                    gPrefsHelper.storeSingleTokenThree(stoken3);
                                    gPrefsHelper.storeSingleTokenThreePrice(stokn3price);
                                    gPrefsHelper.storeMonthlyTokenOne(mtoken1);
                                    gPrefsHelper.storeMonthlyTokenOnePrice(mtokn1price);
                                    gPrefsHelper.storeMonthlyTokenTwo(mtoken2);
                                    gPrefsHelper.storeMonthlyTokenTwoPrice(mtoken2price);
                                    gPrefsHelper.storeMonthlyTokenThree(mtoken3);
                                    gPrefsHelper.storeMonthlyTokenThreePrice(mtoken3price);
                                    gPrefsHelper.storeGMTMessageToPrefrence(gmtMsg);
                                    gPrefsHelper.storeBroadbndMsgToPref(broadMsg);
                                    gPrefsHelper.storeTokenMessageToPrefrence(token_msg);
                                    gPrefsHelper.storeOneToOneChat(chat);
                                    gPrefsHelper.storeOnetoOneCoaching(oneToOnecoachng);
                                    gPrefsHelper.storeGroupCoaching(grupcoaching);
                                    gPrefsHelper.storeTermMessageToPrefrence(term_msg);
                                    gPrefsHelper.storeSurveyMessageToPrefrence(survey_msg);



                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(gPrefsHelper.getGroupSlug().equals("user")) {
                                if(gPrefsHelper.getTermsAcceptd().equals("1")) {
                                    Intent fb = new Intent(StartActivity.this, GoalConsultationRoomActivity.class);
                                    startActivity(fb);
                                    finish();
                                }
                                else
                                {
                                    Intent fb = new Intent(StartActivity.this, TermsAndConditionActivity.class);
                                    startActivity(fb);
                                    finish();
                                }
                            }
                            else if(gPrefsHelper.getGroupSlug().equals("coach"))
                            {
                                if(gPrefsHelper.getTermsAcceptd().equals("1")) {
                                    Intent fb = new Intent(StartActivity.this, CoachHomeActivity.class);
                                    startActivity(fb);
                                    finish();
                                }
                                else
                                {
                                    Intent fb = new Intent(StartActivity.this, TermsAndConditionActivity.class);
                                    startActivity(fb);
                                    finish();
                                }
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
                        Toast.makeText(StartActivity.this, "Timeout Error",
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
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(StartActivity.this.getApplicationContext()).addToRequestQueue(sr);


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
