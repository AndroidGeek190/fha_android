package com.erginus.fithealthy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
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
import com.erginus.fithealthy.adapter.GoalConsultationAdapter;
import com.erginus.fithealthy.adapter.MenuListAdapter;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.MyApplicationClass;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.fragments.AskQuestionFragment;
import com.erginus.fithealthy.fragments.EditProfileFragment;
import com.erginus.fithealthy.fragments.MyBookingFragment;
import com.erginus.fithealthy.fragments.TrackingProgressFragment;
import com.erginus.fithealthy.helper.GlobalPrefrences;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.model.GoalConsultationModel;
import com.erginus.fithealthy.util.IabHelper;
import com.erginus.fithealthy.util.IabResult;
import com.erginus.fithealthy.util.Inventory;
import com.erginus.fithealthy.util.Purchase;
import com.facebook.Session;
import com.jafarkhq.views.EndlessListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BuyTokenActivity extends ActionBarActivity {

    String[] title;
    EditText edit_search;
    int icon[];
    DrawerLayout drawerLayout;
    ListView listView;
    FrameLayout frameLayout;
    LinearLayout linearLayout, ll_spce, ll_search, ll_back;
    MenuListAdapter menuListAdapter;
    ImageView nav_icon, search, logo, add_icon;
    FrameLayout f;
    public  static ImageView profileImage;
    public static IabHelper mHelper;
    public static  TextView name, user_name, user_tokn;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    LinearLayout navigation;
    String token="";
    String redeem_avail;
    public static final int RC_REQUEST = 10001;
    public static final String SKU_150 = "token150";
    public static final String SKU_300 = "tokens300";
    public static final String SKU_600 = "tokens600";
    public static final String SKU_1500 = "tokens1500";
    public static final String SKU_2100 = "tokens2100";
    public static final String SKU_2700 = "tokens2700";
    LinearLayout ll_balance;
    public static RelativeLayout rl_redeam;
    TextView text_discount, text_info, text_free_tokn,txt_gmt, txt_braod,txt_token_pur,txt_token_org, txt_token_blue,txt_150tokn,txt_300tokn,txt_600tokn;
    ImageView img_150Token,img_300Token, img_600Token;
    public  static TextView txt_redm_tokn;
    PrefsHelper prefsHelper;
    GlobalPrefrences globalPrefrences;
    ProgressDialog pDialog;
    IInAppBillingService mService;
    static final String TAG = "Family Health";
    boolean mIsPremium = false;
    String token150value,token300value,token600value;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_token);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        prefsHelper=new PrefsHelper(this);
        f=(FrameLayout)findViewById(R.id.content_frame);
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams
                (ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.FILL_HORIZONTAL | Gravity.CENTER_VERTICAL);

        View customNav = LayoutInflater.from(this).inflate(R.layout.custom_act_bar, null);
        Log.e("TAG", "Creating IAB helper.");
        mHelper = new IabHelper(BuyTokenActivity.this,
                com.erginus.fithealthy.util.AppProperties.BASE_64_KEY);
        mHelper.enableDebugLogging(true);
        initialinapp();
          globalPrefrences=new GlobalPrefrences(this);
        profileImage=(ImageView)findViewById(R.id.ivwLogo);
        user_name=(TextView)findViewById(R.id.usrName);
        user_tokn=(TextView)findViewById(R.id.userEmail);
        user_name.setText(prefsHelper.getUserFNameFromPreference()+" "+prefsHelper.getUserLNameFromPreference());
        user_tokn.setText((prefsHelper.getTokenFromPreference())+ " " + "Tokens");
        Picasso.with(this).load(prefsHelper.getUserImageFromPreference()).into(profileImage);

        actionBar.setCustomView(customNav, layout);
        Toolbar parent =(Toolbar) customNav.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);
        TextView titleTxtView = (TextView)customNav.findViewById(R.id.menu_name_tv);
        titleTxtView.setText("Buy Tokens");

        name=(TextView)actionBar.getCustomView().findViewById(R.id.menu_name_tv);
        title = new String[] {"Goal Consultation Room","Coaches", "Buy Tokens",
                "My Bookings", "Tracking Progress", "Edit Profile","Ask Question" ,"Logout" };
        icon=new int[]{R.drawable.nav_goal_consultion_room_icon_green, R.drawable.nav_coaches_icon, R.drawable.nav_buy_tokens_icon,
                R.drawable.nav_mybooking_icon,R.drawable.nav_tracking_progress_icon, R.drawable.nav_edit_profile_icon,R.drawable.ask_question,
                R.drawable.nav_logout_icon};
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        listView=(ListView)findViewById(R.id.listview_drawer);
        frameLayout=(FrameLayout)findViewById(R.id.content_frame);
        linearLayout=(LinearLayout)findViewById(R.id.left_drawer);
        menuListAdapter=new MenuListAdapter(BuyTokenActivity.this, title, icon);
        nav_icon=(ImageView)actionBar.getCustomView().findViewById(R.id.menu_navi);
        search=(ImageView)actionBar.getCustomView().findViewById(R.id.menu_search);
        ll_back=(LinearLayout)actionBar.getCustomView().findViewById(R.id.search_bar);
        logo=(ImageView)actionBar.getCustomView().findViewById(R.id.action_bar_logo);
        add_icon=(ImageView)actionBar.getCustomView().findViewById(R.id.action_bar_add);
        edit_search=(EditText)actionBar.getCustomView().findViewById(R.id.editTxt_Search);
        navigation=(LinearLayout)actionBar.getCustomView().findViewById(R.id.navigation_lay);
        ll_spce=(LinearLayout)actionBar.getCustomView().findViewById(R.id.space);
        ll_search=(LinearLayout)actionBar.getCustomView().findViewById(R.id.search_bar);
        ll_balance=(LinearLayout)findViewById(R.id.ll_balncetoken_dsc);
        rl_redeam=(RelativeLayout)findViewById(R.id.ll_balncetoken);
        text_discount=(TextView)findViewById(R.id.txt_discount);
        text_info=(TextView)findViewById(R.id.txtLabel);
        text_free_tokn=(TextView)findViewById(R.id.txt_blncetoken_rdm_no);
        txt_150tokn=(TextView)findViewById(R.id.textView_token_ten);
        txt_300tokn=(TextView)findViewById(R.id.textView_twnt_token);
        txt_600tokn=(TextView)findViewById(R.id.textView_ffty_token);
        txt_token_blue=(TextView)findViewById(R.id.textView_ffty_token_price);
        txt_token_org=(TextView)findViewById(R.id.textView_twnty_token_price);
        txt_token_pur=(TextView)findViewById(R.id.textView_ten_token_price);
        txt_gmt=(TextView)findViewById(R.id.txtLabel);
        txt_braod=(TextView)findViewById(R.id.txt_internet);
        txt_redm_tokn=(TextView)findViewById(R.id.txt_blncetoken_no);
        img_150Token=(ImageView)findViewById(R.id.imgvw_purple);
        img_300Token=(ImageView)findViewById(R.id.imgvw_orange);
        img_600Token=(ImageView)findViewById(R.id.imgvw_blue);
        prefsHelper=new PrefsHelper(this);
        text_discount.setText(globalPrefrences.getTokenMessage());
        text_free_tokn.setText(globalPrefrences.getFreeTokensPerMonth());
        txt_150tokn.setText(globalPrefrences.getSingleTokenOne());
        txt_300tokn.setText(globalPrefrences.getSingleTokenTwo());
        txt_600tokn.setText(globalPrefrences.getSingleTokenThree());
        txt_token_blue.setText(globalPrefrences.getSingleTokenThreePrice());
        txt_token_org.setText(globalPrefrences.getSingleTokenTwoPrice());
        txt_token_pur.setText(globalPrefrences.getSingleTokenOnePrice());
        txt_gmt.setText(globalPrefrences.getGMTMessage());
        txt_braod.setText(globalPrefrences.getBroadbandMessage());
        txt_redm_tokn.setText(prefsHelper.getTokenFromPreference());
        redeem_avail=prefsHelper.getRedeemFromPref();
        search.setVisibility(View.GONE);
        if(redeem_avail.equals("0"))
        {
            rl_redeam.setVisibility(View.GONE);
        }
        else
        {
            rl_redeam.setVisibility(View.VISIBLE);
        }
        rl_redeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redeemToken();
                rl_redeam.setVisibility(View.GONE);
            }
        });

        img_150Token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token150value = txt_150tokn.getText().toString();
                String payload = "";
                type="single_token_one";
                mHelper.launchPurchaseFlow(BuyTokenActivity.this,SKU_150,RC_REQUEST,
                        mPurchaseFinishedListener, payload);

            }
        });

        img_300Token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token300value = txt_300tokn.getText().toString();
                String payload = "";
                type = "single_token_two";
                mHelper.launchPurchaseFlow(BuyTokenActivity.this, SKU_300, RC_REQUEST,
                        mPurchaseFinishedListener, payload);

            }
        });
        img_600Token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token600value = txt_600tokn.getText().toString();
                String payload = "";
                type = "single_token_three";
                mHelper.launchPurchaseFlow(BuyTokenActivity.this,
                        SKU_600, RC_REQUEST, mPurchaseFinishedListener, payload);

            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setAdapter(menuListAdapter);
        listView.setOnItemClickListener(new DrawerItemClickListener());
        if(drawerLayout!=null) {
            drawerLayout.setDrawerShadow(R.drawable.list_back, GravityCompat.START);

            mDrawerToggle = new ActionBarDrawerToggle(BuyTokenActivity.this, drawerLayout,
                    R.drawable.actionbar_nav_icon, R.string.Drawer_open, R.string.Drawer_close) {
                public void onDrawerClosed(View view) {

                    super.onDrawerClosed(view);
                }

                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(mDrawerTitle);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                   super.onDrawerOpened(drawerView);

                }
            };
            drawerLayout.setDrawerListener(mDrawerToggle);
        }
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(linearLayout)) {
                    drawerLayout.closeDrawer(linearLayout);
                } else {
                    drawerLayout.openDrawer(linearLayout);
                }
            }
        });
        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BuyTokenActivity.this,InputWeightDetailActivity.class);
                startActivity(intent);
            }
        });


    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private  class DrawerItemClickListener implements  ListView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerLayout.closeDrawer(linearLayout);

            if(position==0)
            {

                Intent edtPrf = new Intent(BuyTokenActivity.this, GoalConsultationRoomActivity.class);
                startActivity(edtPrf);
                overridePendingTransition(0, 0);
                finish();

            }
            if(position==1){
               Intent edtPrf = new Intent(BuyTokenActivity.this, CoachListActivity.class);
                startActivity(edtPrf);
                overridePendingTransition(0, 0);
                finish();

            }

            if(position==2){
                Intent edtPrf = new Intent(BuyTokenActivity.this, BuyTokenActivity.class);
                search.setVisibility(View.GONE);
                startActivity(edtPrf);
                overridePendingTransition(0, 0);
                finish();

            }

            if(position==3){
                f.removeAllViews();
                name.setText("My Bookings");
                search.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                add_icon.setVisibility(View.GONE);
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new MyBookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            if(position==4){
                f.removeAllViews();
                name.setText("Tracking Progress");
                search.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                add_icon.setVisibility(View.VISIBLE);
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
               fragmentTransaction.replace(R.id.content_frame,new TrackingProgressFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

            if(position==5){
                f.removeAllViews();
                name.setText("Edit Profile");
                search.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                add_icon.setVisibility(View.GONE);

                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new EditProfileFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            if(position==6){
                f.removeAllViews();
                name.setText("Ask Question");
                search.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                add_icon.setVisibility(View.GONE);

                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
               fragmentTransaction.replace(R.id.content_frame,new AskQuestionFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            if(position==7){

                Session session = Session.getActiveSession();
                if (session != null) {

                    if (!session.isClosed()) {
                        session.closeAndClearTokenInformation();
                    }
                } else {

                    session = new Session(BuyTokenActivity.this);
                    Session.setActiveSession(session);
                    session.closeAndClearTokenInformation();


                }
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(BuyTokenActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.clear();
                editor.commit();


                finish();
            }

        }
    }
    private void initialinapp() {

        /*** inapp **/
        // /

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {

            @Override
            public void onIabSetupFinished(
                    com.erginus.fithealthy.util.IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data)
    {
        if (mHelper!=null && !mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplicationClass.activityResumed();
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = null;
    }
    // Callback for when a purchase is finished update_token_after_chat
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: "
                    + purchase);
            if (result.isFailure()) {
                if (result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
                    Log.d(TAG, "already purchased item.");
                    mIsPremium = true;
                    // updateUi();
                    return;
                }
                //commented 07-02-15
                complain("Error purchasing: " + result);
                // setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_150) ){

                Log.d(TAG, "Purchase is 150	. Congratulating User.");
                // call web services to update database
                ;
                purchaseToken(type);
                mHelper.consumeAsync(purchase,
                        mConsumeFinishedListener);

            } else if (purchase.getSku().equals(SKU_300)) {

                Log.d(TAG, "Purchase 300 token. Congratulating user.");
                // call web services to update database
                purchaseToken(type);

                mHelper.consumeAsync(purchase,
                        mConsumeFinishedListener);

            } else if (purchase.getSku().equals(SKU_600)) {

                Log.d(TAG, "Purchase 600 token. Congratulating user.");
                // call web services to update database
                purchaseToken(type);
                mHelper.consumeAsync(purchase,
                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener=new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }
            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            Purchase token_pur = inventory.getPurchase(SKU_150);
            if (token_pur != null && verifyDeveloperPayload(token_pur)) {
                Log.d(TAG, "We have tokens. Consuming it.");
                mHelper.consumeAsync(token_pur, mConsumeFinishedListener);
                return;
            }
            Purchase token_pur1= inventory.getPurchase(SKU_300);
            if (token_pur1 != null && verifyDeveloperPayload(token_pur1)) {
                Log.d(TAG, "We have tokens. Consuming it.");
                mHelper.consumeAsync(token_pur1,
                        mConsumeFinishedListener);
                return;
            }

            Purchase token_pur2 = inventory.getPurchase(SKU_600);
            if (token_pur2 != null && verifyDeveloperPayload(token_pur2)) {
                Log.d(TAG, "We have tokens. Consuming it.");
                mHelper.consumeAsync(token_pur2, mConsumeFinishedListener);
                return;
            }

        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase
                    + ", result: " + result);
            if (mHelper == null) return;

            if (result.isSuccess()) {

                alert("You Purchased Token "
                        + purchase.getSku());
            } else {
                complain("Error while consuming: " + result);
            }
            Log.d(TAG, "End consumption flow.");
        }
    };

    void setWaitScreen(boolean set) {

    }
    void complain(String message) {
        Log.e(TAG, "****  Error: " + message);
        alert("Error: " + message);
    }

    protected boolean verifyDeveloperPayload(Purchase purchase) {
        String payload=purchase.getDeveloperPayload();
        return true;
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(BuyTokenActivity.this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    public void redeemToken()
    {
        try {
            pDialog = new ProgressDialog(BuyTokenActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Purchase Tokens" + MapAppConstants.API +"/redeem_free_tokens");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/redeem_free_tokens", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    ;
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(BuyTokenActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

                                    JSONObject jsonObject=object.getJSONObject("data");
                                    token=jsonObject.getString("user_token_count");
                                    prefsHelper.storeTokenToPreference(token);
                                    redeem_avail="0";
                                    prefsHelper.storeRedeemToPreference(redeem_avail);
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            rl_redeam.setVisibility(View.GONE);
                            txt_redm_tokn.setText(prefsHelper.getTokenFromPreference());
                            user_tokn.setText(prefsHelper.getTokenFromPreference()+" " + "Tokens");
                            GoalConsultationRoomActivity.user_tokn.setText((prefsHelper.getTokenFromPreference()) + " " + "Tokens");
                            CoachListActivity.user_tokn.setText((prefsHelper.getTokenFromPreference()) + " " + "Tokens");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    ;
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(BuyTokenActivity.this, "Timeout Error",
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
                    params.put("user_security_hash",prefsHelper.getSecHashFromPrefrence());

                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(BuyTokenActivity.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    public void purchaseToken(final String type)
    {
        try {
            pDialog = new ProgressDialog(BuyTokenActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Purchase Tokens" + MapAppConstants.API +"/purchase");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/purchase", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    ;
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(BuyTokenActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

                                    JSONObject jsonObject=object.getJSONObject("data");
                                    token=jsonObject.getString("user_token_count");
                                    prefsHelper.storeTokenToPreference(token);

                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            txt_redm_tokn.setText(prefsHelper.getTokenFromPreference());
                            user_tokn.setText(prefsHelper.getTokenFromPreference()+" " + "Tokens");
                            GoalConsultationRoomActivity.user_tokn.setText((prefsHelper.getTokenFromPreference()) + " " + "Tokens");
                            CoachListActivity.user_tokn.setText((prefsHelper.getTokenFromPreference()) + " " + "Tokens");
                            }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    ;
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(BuyTokenActivity.this, "Timeout Error",
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
                    params.put("user_security_hash",prefsHelper.getSecHashFromPrefrence());
                    params.put("token_type", type);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(BuyTokenActivity.this.getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        Toast.makeText(BuyTokenActivity.this,"Please Use Side Menu", Toast.LENGTH_SHORT).show();
    }
}
