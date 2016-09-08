package com.erginus.fithealthy.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.erginus.fithealthy.CoachHomeActivity;
import com.erginus.fithealthy.CoachLoginActivity;
import com.erginus.fithealthy.GoalConsultationRoomActivity;
import com.erginus.fithealthy.R;
import com.erginus.fithealthy.TermsAndConditionActivity;
import com.erginus.fithealthy.commons.ConnectionDetector;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.GlobalPrefrences;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class MainFragment extends Fragment {
    private UiLifecycleHelper uiHelper;
    PrefsHelper prefsHelper;
    LinearLayout coachlogin, twitterlogin;
    private boolean isLoggedIn = false; // by default assume not logged in
    String phone = "";
    String dobrth = "";
    String username = "";
    String email = "";
    String fname = "";
    String lname = "";
    String gender = "";
    String image = "", token="", login="";
    String FB_id = "";
    static String TWITTER_CONSUMER_KEY = "GVgx8ZxMftq0aZOJqkMTFJtLV";
    static String TWITTER_CONSUMER_SECRET = "fcE51Bg9LhFHZAzDsaF1j987WHY8sHv2NMDTFuAa1LDIIcOgZA";
    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

    // Twitter oauth urls
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    private static final String TAG = "MainFragment";
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;
    private static Twitter twitter;
    private static RequestToken requestToken;
    String userName, img, termsAccptd;
    long userID;
    User user;
    GlobalPrefrences globalPrefrences;

    public MainFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        LoginButton authButton = (LoginButton) view.findViewById(R.id.ImageView_fb);
        authButton.setBackgroundResource(R.drawable.login_facebook_icon);
        authButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        authButton.setFragment(this);
        coachlogin=(LinearLayout)view.findViewById(R.id.ll_caoch_login);
        twitterlogin=(LinearLayout)view.findViewById(R.id.ll_tw);
        prefsHelper=new PrefsHelper(getActivity());
        globalPrefrences=new GlobalPrefrences(getActivity());
        termsAccptd=globalPrefrences.getTermsAcceptd();
        authButton.setReadPermissions(Arrays.asList("public_profile", "user_likes","email", "user_status", "user_friends", "user_location", "user_birthday"));
        coachlogin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

                  Intent fb = new Intent(getActivity(), CoachLoginActivity.class);
                  startActivity(fb);


          }

        });
        twitterlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToTwitter();
            }
        });


        // Check if twitter keys are set
        if(TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0){

            AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Alert!");
            alertDialog.setMessage("Twitter oAuth tokens, Please set your twitter oauth tokens first!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog,int which)
                {
                    // Write your code here to execute after dialog closed
                    Toast.makeText(getActivity().getApplicationContext(),"You clicked on OK", Toast.LENGTH_SHORT).show();

                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
        if (!isTwitterLoggedInAlready()) {
            Uri uri =getActivity().getIntent().getData();
            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                String verifier = uri
                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

                try {
                    // Get the access token
                    AccessToken accessToken = twitter.getOAuthAccessToken(
                            requestToken, verifier);
                   prefsHelper.storeKey0authTokenToPreference(accessToken.getToken());
                    prefsHelper.storeKey0authSecretToPreference(accessToken.getTokenSecret());
                    prefsHelper.storeTwitterLoginoPreference(true);
                    Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
                    userID = accessToken.getUserId();
                    user = twitter.showUser(userID);
                    userName = user.getName();

                    int firstSpace = userName.indexOf(" ");
                    final String firstName = userName.substring(0, firstSpace);
                    final String lastName = userName.substring(firstSpace).trim();
                    prefsHelper.storeUserNameToPreference(userName);
                    try {
                        final ProgressDialog pDialog = new ProgressDialog(getActivity());
                        pDialog.setMessage("Loading...");
                        pDialog.show();

                        Log.e("", "LOGIN " + MapAppConstants.API + "/social_media_login");
                        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST,MapAppConstants.API+"/social_media_login", new com.android.volley.Response.Listener<String>() {
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
                                    Toast.makeText(getActivity(),""+serverMessage,Toast.LENGTH_SHORT).show();
                                    if (serverCode.equalsIgnoreCase("0")) {

                                    }
                                    if (serverCode.equalsIgnoreCase("1")) {
                                        try {

                                            if ("1".equals(serverCode)) {
                                                String id = "",grup_slug,term_msg,survey_msg,trmsAccpt, rate_msg = "", free_tokn_avail = "", hash = "", gmtMsg = "", broadMsg = "", chat = "", grupcoaching = "", oneToOnecoachng = "", free_tokn = "", token_msg = "", stoken1 = "", stoken2 = "", stoken3 = "", stokn1price = "", stokn2price = "", stokn3price = "", mtoken1 = "", mtoken2 = "", mtoken3 = "", mtokn1price = "", mtoken2price = "", mtoken3price = "";
                                                JSONObject jsonObject=object.getJSONObject("data");
                                                    id=jsonObject.getString("user_id");
                                                    image=jsonObject.getString("user_profile_image_url");
                                                    hash=jsonObject.getString("user_security_hash");
                                                   fname=jsonObject.getString("user_first_name");
                                                   lname=jsonObject.getString("user_last_name");
                                                   token=jsonObject.getString("user_token_count");
                                                login = jsonObject.getString("user_login");
                                                phone = jsonObject.getString("user_primary_contact");
                                                gender = jsonObject.getString("user_gender");
                                                email = jsonObject.getString("user_email");
                                                grup_slug=jsonObject.getString("group_slug");
                                                globalPrefrences.storeGroupSlug(grup_slug);
                                                free_tokn_avail = jsonObject.getString("user_free_tokens_available");
                                                trmsAccpt=jsonObject.getString("terms_accepted");
                                                globalPrefrences.storeTermsAcceptedToPrefrence(trmsAccpt);
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
                                                globalPrefrences.storeFreeTokenPerMonth(free_tokn);
                                                globalPrefrences.storeSingleTokenOne(stoken1);
                                                globalPrefrences.storeSingleTokenOnePrice(stokn1price);
                                                globalPrefrences.storeSingleTokenTwo(stoken2);
                                                globalPrefrences.storeSingleTokenTwoPrice(stokn2price);
                                                globalPrefrences.storeSingleTokenThree(stoken3);
                                                globalPrefrences.storeSingleTokenThreePrice(stokn3price);
                                                globalPrefrences.storeMonthlyTokenOne(mtoken1);
                                                globalPrefrences.storeMonthlyTokenOnePrice(mtokn1price);
                                                globalPrefrences.storeMonthlyTokenTwo(mtoken2);
                                                globalPrefrences.storeMonthlyTokenTwoPrice(mtoken2price);
                                                globalPrefrences.storeMonthlyTokenThree(mtoken3);
                                                globalPrefrences.storeMonthlyTokenThreePrice(mtoken3price);
                                                globalPrefrences.storeGMTMessageToPrefrence(gmtMsg);
                                                globalPrefrences.storeBroadbndMsgToPref(broadMsg);
                                                globalPrefrences.storeTokenMessageToPrefrence(token_msg);
                                                globalPrefrences.storeOneToOneChat(chat);
                                                globalPrefrences.storeOnetoOneCoaching(oneToOnecoachng);
                                                globalPrefrences.storeGroupCoaching(grupcoaching);
                                                globalPrefrences.storeTermMessageToPrefrence(term_msg);
                                                globalPrefrences.storeSurveyMessageToPrefrence(survey_msg);
                                                globalPrefrences.storeRateMessage(rate_msg);
                                                globalPrefrences.storeSocialMediaPlatform("twitter");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        if(globalPrefrences.getGroupSlug().equals("user")) {

                                            if(globalPrefrences.getTermsAcceptd().equals("1")) {
                                                Intent fb = new Intent(getActivity(), GoalConsultationRoomActivity.class);
                                                startActivity(fb);
                                                getActivity().finish();

                                            }
                                            else
                                            {
                                                Intent fb = new Intent(getActivity(), TermsAndConditionActivity.class);
                                                startActivity(fb);
                                                getActivity().finish();

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

                                params.put("social_media_platform", "twitter");
                                params.put("social_media_id", String.valueOf(userID));
                                params.put("user_email","");
                                params.put("user_first_name",firstName);
                                params.put("user_last_name",lastName);
                                params.put("user_gender","");


                                return params;
                            }
                        };
                        sr.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        sr.setShouldCache(true);
                        VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
               }
        }
    return view;
    }
    /**
     * Function to login twitter
     * */
    private void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        requestToken = twitter
                                .getOAuthRequestToken(TWITTER_CALLBACK_URL);
                        getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse(requestToken.getAuthenticationURL())));
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            } );   thread.start();
        }      else{

            if(globalPrefrences.getGroupSlug().equals("user")) {

                if(globalPrefrences.getTermsAcceptd().equals("1")) {
                    Intent fb = new Intent(getActivity(), GoalConsultationRoomActivity.class);
                    startActivity(fb);
                    getActivity().finish();

                }
                else
                {
                    Intent fb = new Intent(getActivity(), TermsAndConditionActivity.class);
                    startActivity(fb);
                    getActivity().finish();

                }

            }}
        }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     * */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return prefsHelper.getTwitterLoginFromPreference();
    }
    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            // Get the user's data.
            Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    // If the response is successful
                    if (session == Session.getActiveSession()) {
                        if (user != null) {

                            fname = user.getFirstName();

                            lname = user.getLastName();
                            username = fname + " " + lname;
                            prefsHelper.storeUserNameToPreference(username);

                            if(user.getProperty("email")!=null) {
                                email = user.getProperty("email") + "";
                            }
                            else
                            {
                                email="";
                            }
                            prefsHelper.storeEmailToPreference(email);
                            if (user.getProperty("gender") != null) {
                                gender = ""+user.getProperty("gender");
                                if (gender.equalsIgnoreCase("male")) {
                                    prefsHelper.storeGenderToPreference(gender);
                                }
                                if (gender.equalsIgnoreCase("female")) {
                                    prefsHelper.storeGenderToPreference(gender);
                                }
                            }
                            else {
                                gender = "";
                                prefsHelper.storeGenderToPreference(gender);
                            }
                            if (user.getProperty("phone") != null) {
                                phone = user.getProperty("phone")+"";
                            }
                            else{
                                phone="";
                            }
                            Log.d(username, email);

                            FB_id = user.getId();
                            Log.d("id...", FB_id);
                            /*try {
                                img_value = new URL("https://graph.facebook.com/" + FB_id + "/picture?type=large");

                                mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                mIcon1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();

                                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                                prefsHelper.storeImageBitmapToPreference(encodedImage);
                                //  Log.d("iudicyyeweyv", ""+mIcon1);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
*/
                            loginToFacebook();
                        }}
                    if (response.getError() != null) {

                    }
                }
            });
            request.executeAsync();

        }
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");


        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");

        }

         }
    public void loginToFacebook()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "LOGIN " + MapAppConstants.API +"/social_media_login");
            StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST,MapAppConstants.API+"/social_media_login", new com.android.volley.Response.Listener<String>() {
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
                                    String id = "",grup_slug,term_msg,survey_msg,trmsAccpt, rate_msg = "", free_tokn_avail = "", hash = "", gmtMsg = "", broadMsg = "", chat = "", grupcoaching = "", oneToOnecoachng = "", free_tokn = "", token_msg = "", stoken1 = "", stoken2 = "", stoken3 = "", stokn1price = "", stokn2price = "", stokn3price = "", mtoken1 = "", mtoken2 = "", mtoken3 = "", mtokn1price = "", mtoken2price = "", mtoken3price = "";
                                    JSONObject jsonObject = object.getJSONObject("data");
                                    id = jsonObject.getString("user_id");
                                    hash = jsonObject.getString("user_security_hash");
                                    fname = jsonObject.getString("user_first_name");
                                    lname = jsonObject.getString("user_last_name");
                                    image = jsonObject.getString("user_profile_image_url");
                                    token = jsonObject.getString("user_token_count");
                                    login = jsonObject.getString("user_login");
                                    phone = jsonObject.getString("user_primary_contact");
                                    gender = jsonObject.getString("user_gender");
                                    email = jsonObject.getString("user_email");
                                    grup_slug=jsonObject.getString("group_slug");
                                    globalPrefrences.storeGroupSlug(grup_slug);
                                    free_tokn_avail = jsonObject.getString("user_free_tokens_available");
                                    trmsAccpt=jsonObject.getString("terms_accepted");
                                    globalPrefrences.storeTermsAcceptedToPrefrence(trmsAccpt);
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
                                   globalPrefrences.storeFreeTokenPerMonth(free_tokn);
                                    globalPrefrences.storeSingleTokenOne(stoken1);
                                    globalPrefrences.storeSingleTokenOnePrice(stokn1price);
                                    globalPrefrences.storeSingleTokenTwo(stoken2);
                                    globalPrefrences.storeSingleTokenTwoPrice(stokn2price);
                                    globalPrefrences.storeSingleTokenThree(stoken3);
                                    globalPrefrences.storeSingleTokenThreePrice(stokn3price);
                                    globalPrefrences.storeMonthlyTokenOne(mtoken1);
                                    globalPrefrences.storeMonthlyTokenOnePrice(mtokn1price);
                                    globalPrefrences.storeMonthlyTokenTwo(mtoken2);
                                    globalPrefrences.storeMonthlyTokenTwoPrice(mtoken2price);
                                    globalPrefrences.storeMonthlyTokenThree(mtoken3);
                                    globalPrefrences.storeMonthlyTokenThreePrice(mtoken3price);
                                    globalPrefrences.storeGMTMessageToPrefrence(gmtMsg);
                                    globalPrefrences.storeBroadbndMsgToPref(broadMsg);
                                    globalPrefrences.storeTokenMessageToPrefrence(token_msg);
                                    globalPrefrences.storeOneToOneChat(chat);
                                    globalPrefrences.storeOnetoOneCoaching(oneToOnecoachng);
                                    globalPrefrences.storeGroupCoaching(grupcoaching);
                                    globalPrefrences.storeTermMessageToPrefrence(term_msg);
                                    globalPrefrences.storeSurveyMessageToPrefrence(survey_msg);
                                    globalPrefrences.storeRateMessage(rate_msg);
                                    globalPrefrences.storeSocialMediaPlatform("facebook");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(globalPrefrences.getGroupSlug().equals("user")) {

                                if(globalPrefrences.getTermsAcceptd().equals("1")) {
                                    Intent fb = new Intent(getActivity(), GoalConsultationRoomActivity.class);
                                    startActivity(fb);

                                }
                                else
                                {
                                    Intent fb = new Intent(getActivity(), TermsAndConditionActivity.class);
                                    startActivity(fb);

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
                    Log.d(FB_id, prefsHelper.getEmailFromPreference()+" "+fname+" "+lname+" "+prefsHelper.getGenderFromPreference());
                    params.put("social_media_platform", "facebook");
                    params.put("social_media_id", FB_id);
                    params.put("user_email",prefsHelper.getEmailFromPreference());
                    params.put("user_first_name",fname);
                    params.put("user_last_name",lname);
                    params.put("user_gender",prefsHelper.getGenderFromPreference());
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES * 2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);

        }
        };

   public void onResume() {
        super.onResume();
        uiHelper.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        //  new LongOperation().execute("");
       }

@Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
        uiHelper.onSaveInstanceState(outState);
    }
   /*
    private void publishStory() {
        Session session = Session.getActiveSession();

        if (session != null){

            // Check for publish permissions
            List<String> permissions = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                pendingPublishReauthorization = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session
                        .NewPermissionsRequest(this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }

            Bundle postParams = new Bundle();
            postParams.putString("name", "Facebook SDK for Android");
            postParams.putString("caption", "Build great social apps and get more installs.");
            postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
            postParams.putString("link", "https://developers.facebook.com/android");
            postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {
                    JSONObject graphResponse = response
                            .getGraphObject()
                            .getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");

                    } catch (JSONException e) {
                        Log.i(TAG,
                                "JSON error "+ e.getMessage());
                    }
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Toast.makeText(getActivity()
                                        .getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity()
                                        .getApplicationContext(),
                                postId,
                                Toast.LENGTH_LONG).show();
                    }
                }
            };

            Request request = new Request(session, "me/feed", postParams,
                    HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }

    }
    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }


*/

}

