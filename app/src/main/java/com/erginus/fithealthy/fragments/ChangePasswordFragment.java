package com.erginus.fithealthy.fragments;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.erginus.fithealthy.R;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.GlobalPrefrences;
import com.erginus.fithealthy.helper.PrefsHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChangePasswordFragment extends android.support.v4.app.Fragment {
    String id="",usr_login= "", usr_email= "", usr_contc= "", hash = "", gender= "", desc="", rate="", grup_slug="" ;
    EditText edt_new_pwd, edt_confrm_pwd;
    Button btn_chng_pwd;
    PrefsHelper prefsHelper;
    String new_pswrd,cnfrm_pwd;
    GlobalPrefrences globalPrefrences;

    @Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        edt_new_pwd=(EditText)rootView.findViewById(R.id.editText_newPwd);
        edt_confrm_pwd=(EditText)rootView.findViewById(R.id.editText_confrmPwd);
        btn_chng_pwd=(Button)rootView.findViewById(R.id.button_chngPwd);
        prefsHelper=new PrefsHelper(getActivity());
    globalPrefrences=new GlobalPrefrences(getActivity());
        btn_chng_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_pswrd = edt_new_pwd.getText().toString();
                cnfrm_pwd = edt_confrm_pwd.getText().toString();
                boolean cancelLogin = false;
                View focusView = null;

                if (!TextUtils.isEmpty(new_pswrd) && !isValidPassword(new_pswrd)) {
                    edt_new_pwd.setError(getString(R.string.invalid_password));
                    focusView = edt_new_pwd;
                    cancelLogin = true;
                } else if (TextUtils.isEmpty(new_pswrd)) {
                    edt_new_pwd.setError(getString(R.string.pswrd_required));
                    focusView = edt_new_pwd;
                    cancelLogin = true;
                }
                else if(TextUtils.isEmpty(cnfrm_pwd) ) {
                    edt_confrm_pwd.setError(getString(R.string.invalid_password));
                    focusView = edt_confrm_pwd;
                    cancelLogin = true;
                }
                else if(!cnfrm_pwd.equals(new_pswrd) ) {
                    edt_confrm_pwd.setError("Password do not match");
                    focusView = edt_confrm_pwd;
                    cancelLogin = true;
                }
                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {
                    changePassword();
                }
            }
        });
        return rootView;
        }
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }

    public void changePassword()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Change password" + MapAppConstants.API +"/change_password");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/change_password", new Response.Listener<String>() {
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

                            Intent intent=new Intent(getActivity(), CoachHomeActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                            getActivity().finish();
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

                    params.put("user_login_password", edt_new_pwd.getText().toString());
                    params.put("confirm_login_password", edt_confrm_pwd.getText().toString());
                    params.put("user_id",prefsHelper.getUserIdFromPrefrence());
                    params.put("user_security_hash",prefsHelper.getSecHashFromPrefrence());
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


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