package com.erginus.fithealthy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.erginus.fithealthy.helper.PrefsHelper;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivity extends Activity {
EditText edt_usrNm, edt_email, edt_mobile, edt_spclty;
    RadioGroup gender;
    RadioButton radioButton;
    Button registr;
    LinearLayout ll_backArrow;
    String genderValue="";
    int selectedId;
    PrefsHelper prefsHelper;
   String userName, user_email,user_phn,user_spclty, gendr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//used for hiding the soft keyboard on starting the activity.
//getting ids of every component of layout file
        edt_usrNm=(EditText)findViewById(R.id.editText_name);
        edt_email=(EditText)findViewById(R.id.editText_mail);
        edt_mobile=(EditText)findViewById(R.id.editText_phnno);
        edt_spclty=(EditText)findViewById(R.id.edt_desc);
        gender=(RadioGroup)findViewById(R.id.radio_group);
        ll_backArrow=(LinearLayout)findViewById(R.id.linear_back);
        registr=(Button)findViewById(R.id.button_rgstn);

        prefsHelper=new PrefsHelper(this);
//sign up method is calling on register button click with proper validations.
        registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View focusView = null;
                boolean cancelLogin = false;
                userName=edt_usrNm.getText().toString(); //getting value from edittext into string variable
                user_email=edt_email.getText().toString();//getting value from edittext into string variable
                user_phn=edt_mobile.getText().toString();//getting value from edittext into string variable
                user_spclty=edt_spclty.getText().toString();//getting value from edittext into string variable
                selectedId = gender.getCheckedRadioButtonId();//getting id of selected radio button into int variable
                radioButton=(RadioButton)findViewById(selectedId);//getting id from radiobutton
                gendr= radioButton.getText().toString();//getting value from radio button into string variable
                if(gendr.equals("Male"))
                {
                    genderValue="1";
                }
                else
                {
                    genderValue="0";
                }
                Log.d("enterd valuesssss......",userName+" "+ user_email+" "+user_phn+" "+user_spclty+" "+gendr);
                /*
                   Validating fields for not to be null or improper
                 */
                if (TextUtils.isEmpty(userName)) {
                    edt_usrNm.setError(getString(R.string.userName_required));
                    focusView = edt_usrNm;
                    cancelLogin = true;
                }else if( userName.length()<6)
                {
                    edt_usrNm.setError("Username is too short");
                    focusView = edt_usrNm;
                    cancelLogin = true;
                }

                if (TextUtils.isEmpty(user_email)) {
                    edt_email.setError(getString(R.string.email_required));
                    focusView = edt_email;
                    cancelLogin = true;
                } else if (!isValidEmail(user_email)) {
                    edt_email.setError(getString(R.string.invalid_email));
                    focusView = edt_email;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(user_phn) & !isValidPhone((user_phn))) {
                    edt_mobile.setError(getString(R.string.phone_required));
                    focusView = edt_mobile;
                    cancelLogin = true;
                }
                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {
                    //calling method
                    signUp();
                }

            }
        });

        ll_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
    }
        });


    }
    /*
    sign up method
     */
    public void signUp()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
           //calling webservice
            Log.e("", "SIGNUP " + MapAppConstants.API +"/signup");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/signup", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    ;
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        //response from webservice
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(SignUpActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                 JSONObject object1=object.getJSONObject("data");
                                    String  usr_login = "", usr_email = "", usr_contc = "", hash = "", gender = "", desc = "";
                                     hash = object1.getString("user_security_hash");
                                    usr_email = object1.getString("user_email");
                                    usr_contc = object1.getString("user_primary_contact");
                                    usr_login = object1.getString("user_login");
                                    gender = object1.getString("user_gender");
                                    desc = object1.getString("user_description");
                                    //showing registerd values
                                    Log.d(usr_email, usr_login);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                         //returning to login screen after sign up
                            Intent i1 = new Intent(SignUpActivity.this, CoachLoginActivity.class);
                            startActivity(i1);
                            finish();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    ;
                  //  Volley error messages
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(SignUpActivity.this, "Timeout Error",
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
                /*
                posting data to web service
                 */
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    Log.d("enterd valuesssss......",userName+" "+ user_email+" "+user_phn+" "+user_spclty+" "+gendr);

                    params.put("user_login", userName);
                    params.put("user_email", user_email);
                    params.put("user_gender", genderValue);
                    params.put("user_description", user_spclty);
                    params.put("user_primary_contact", user_phn);
                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    //method for validating email
        private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    //method for validating phone
    private boolean isValidPhone(String pass) {
        if (pass != null && pass.length()==10) {
            return true;
        }
        return false;
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
