package com.erginus.fithealthy;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.erginus.fithealthy.adapter.MenuListAdapter;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.fragments.AskQuestionFragment;
import com.erginus.fithealthy.fragments.EditProfileFragment;
import com.erginus.fithealthy.fragments.MyBookingFragment;
import com.erginus.fithealthy.fragments.TrackingProgressFragment;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.model.CoachesModel;
import com.erginus.fithealthy.adapter.CoachListAdapter;
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


public class CoachListActivity extends ActionBarActivity {
    List<CoachesModel> coachList, searchedCoachList;

    CoachListAdapter coachListAdapter;
    EndlessListView coachesListView;
    PrefsHelper prefsHelper;
    private boolean mHaveMoreDataToLoad;
    String search_data;
    LinearLayout  ll_spce, ll_search, navigation, linearLayout,ll_back;
    ImageView nav_icon, search, logo, add_icon;
    String[] title;

    public  static ImageView profileImage;
    EditText edit_search;
    int icon[];
    DrawerLayout drawerLayout;
    ListView listView;
    FrameLayout frameLayout;
    MenuListAdapter menuListAdapter;
    FrameLayout f;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    int page=0;
    public static TextView name, user_name, user_tokn;
    TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_list);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        prefsHelper=new PrefsHelper(this);
        coachList=new ArrayList<CoachesModel>();
        searchedCoachList=new ArrayList<CoachesModel>();
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams
                (ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.FILL_HORIZONTAL | Gravity.CENTER_VERTICAL);

        View customNav = LayoutInflater.from(this).inflate(R.layout.custom_act_bar, null);
        actionBar.setCustomView(customNav, layout);
        Toolbar parent =(Toolbar) customNav.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);// set padding programmatically to 0dp
        TextView titleTxtView = (TextView)customNav.findViewById(R.id.menu_name_tv);
        titleTxtView.setText("Coaches");
        textError=(TextView)findViewById(R.id.textView5);
        profileImage=(ImageView)findViewById(R.id.ivwLogo);
        user_name=(TextView)findViewById(R.id.usrName);
        user_tokn=(TextView)findViewById(R.id.userEmail);
        user_name.setText(prefsHelper.getUserFNameFromPreference()+" "+prefsHelper.getUserLNameFromPreference());
        user_tokn.setText((prefsHelper.getTokenFromPreference())+ " " + "Tokens");
        Picasso.with(this).load(prefsHelper.getUserImageFromPreference()).into(profileImage);
        name=(TextView)actionBar.getCustomView().findViewById(R.id.menu_name_tv);
        title = new String[] {"Goal Consultation Room","Coaches", "Buy Tokens",
                "My Bookings", "Tracking Progress", "Edit Profile","Ask Question" ,"Logout" };
        icon=new int[]{R.drawable.nav_goal_consultion_room_icon_green, R.drawable.nav_coaches_icon, R.drawable.nav_buy_tokens_icon,
                R.drawable.nav_mybooking_icon,R.drawable.nav_tracking_progress_icon, R.drawable.nav_edit_profile_icon,R.drawable.ask_question,
                R.drawable.nav_logout_icon};
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        listView=(ListView)findViewById(R.id.listview_drawer);
        f=(FrameLayout)findViewById(R.id.content_frame);
        frameLayout=(FrameLayout)findViewById(R.id.content_frame);
        linearLayout=(LinearLayout)findViewById(R.id.left_drawer);
        menuListAdapter=new MenuListAdapter(CoachListActivity.this, title, icon);
        nav_icon=(ImageView)actionBar.getCustomView().findViewById(R.id.menu_navi);
        search=(ImageView)actionBar.getCustomView().findViewById(R.id.menu_search);
        ll_back=(LinearLayout)actionBar.getCustomView().findViewById(R.id.search_bar);
        logo=(ImageView)actionBar.getCustomView().findViewById(R.id.action_bar_logo);
        add_icon=(ImageView)actionBar.getCustomView().findViewById(R.id.action_bar_add);
        edit_search=(EditText)actionBar.getCustomView().findViewById(R.id.editTxt_Search);
        navigation=(LinearLayout)actionBar.getCustomView().findViewById(R.id.navigation_lay);
        ll_spce=(LinearLayout)actionBar.getCustomView().findViewById(R.id.space);
        ll_search=(LinearLayout)actionBar.getCustomView().findViewById(R.id.search_bar);
        coachesListView=(EndlessListView)findViewById(R.id.dis_lv);
        coachesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent coach = new Intent(CoachListActivity.this, CoachDetailActivity.class);
                coach.putExtra("id", coachList.get(position).getId());
                coach.putExtra("firstName", coachList.get(position).getFName());
                coach.putExtra("lastName", coachList.get(position).getLName());
                coach.putExtra("description", coachList.get(position).getDescription());
                coach.putExtra("imagePath", coachList.get(position).getImage());
                coach.putExtra("ratingCount", coachList.get(position).getRatingCount());
                coach.putExtra("ratingAvg", coachList.get(position).getRatingAverage());
                startActivity(coach);


            }
        });
        coachList(page);
        mHaveMoreDataToLoad = true;
        coachesListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (true == mHaveMoreDataToLoad) {
                    loadMoreData();
                } else {
                    Toast.makeText(CoachListActivity.this, "No more data to load",
                            Toast.LENGTH_SHORT).show();
                }

                return mHaveMoreDataToLoad;

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.setVisibility(View.GONE);
                ll_spce.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                ll_search.setVisibility(View.VISIBLE);
            }
        });
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    coachesListView.setVisibility(View.GONE);
                    search_data = edit_search.getText().toString();
                    searchedCoachList.clear();
                    getCoachList().clear();
                    searchCoachList(search_data, page);
                    mHaveMoreDataToLoad = true;
                    coachesListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
                        @Override
                        public boolean onLoadMore() {
                            if (true == mHaveMoreDataToLoad) {
                                loadMoreData(search_data);
                            } else {
                                Toast.makeText(CoachListActivity.this, "No more data to load",
                                        Toast.LENGTH_SHORT).show();
                            }

                            return mHaveMoreDataToLoad;

                        }
                    });

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edtPrf = new Intent(CoachListActivity.this, CoachListActivity.class);
                startActivity(edtPrf);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        listView.setAdapter(menuListAdapter);
        listView.setOnItemClickListener(new DrawerItemClickListener());
        if(drawerLayout!=null) {
            drawerLayout.setDrawerShadow(R.drawable.list_back, GravityCompat.START);

            mDrawerToggle = new ActionBarDrawerToggle(CoachListActivity.this, drawerLayout,
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
                Intent intent=new Intent(CoachListActivity.this,InputWeightDetailActivity.class);
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

                Intent edtPrf = new Intent(CoachListActivity.this, GoalConsultationRoomActivity.class);
                startActivity(edtPrf);
                overridePendingTransition(0, 0);
                finish();

            }
            if(position==1){
               Intent edtPrf = new Intent(CoachListActivity.this, CoachListActivity.class);
                startActivity(edtPrf);
                overridePendingTransition(0, 0);
                finish();

            }

            if(position==2){
                Intent edtPrf = new Intent(CoachListActivity.this, BuyTokenActivity.class);
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

                    session = new Session(CoachListActivity.this);
                    Session.setActiveSession(session);
                    session.closeAndClearTokenInformation();


                }
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CoachListActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.clear();
                editor.commit();
                finish();
            }

        }
    }

    private void loadMoreData() {
        //  new LoadMore().execute((Void) null);
        page++;
        coachList(page);
    }
    private void loadMoreData(String dta) {
        //  new LoadMore().execute((Void) null);
        page++;
        searchCoachList(dta, page);

    }
    @Override
    public void onResume() {
        super.onResume();


    }

    public List<CoachesModel> getCoachList() {
        return coachList;
    }

    public void setCoachList(List<CoachesModel> coachList) {
        this.coachList = coachList;
    }
    public List<CoachesModel> getSCoachList() {
        return searchedCoachList;
    }

    public void setSCoachList(List<CoachesModel> coachList) {
        this.searchedCoachList= coachList;
    }

    private CoachesModel coachesModel(String id,String fname, String lname, String rating,  String desc, String image, String rateAvg)
    {
        CoachesModel coach = new CoachesModel();
        coach.setId(id);
        coach.setFName(fname);
        coach.setLName(lname);
        coach.setRatingCount(rating);
        coach.setDescription(desc);
        coach.setImage(image);
        coach.setRatingAverage(rateAvg);
        return coach;
    }
    public void coachList(final int page)
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CoachListActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "CoachList " + MapAppConstants.API +"/get_active_coaches");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/get_active_coaches", new Response.Listener<String>() {
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

                        }
                        if (serverCode.equalsIgnoreCase("1")) {

                         try {
                               if ("1".equals(serverCode)) {
                                    JSONArray array = object.getJSONArray("data");

                                    if (array.length() > 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject objet = array.getJSONObject(i);
                                        String id = "", fname = "", lname = "", rating = "", image = "", desc = "", ratingAvg="";
                                        id = objet.getString("user_id");
                                        fname = objet.getString("user_first_name");
                                        lname = objet.getString("user_last_name");
                                        rating = objet.getString("user_rating_count");
                                        image = objet.getString("user_profile_image_url");
                                        desc = objet.getString("user_description");
                                        ratingAvg=objet.getString("user_rating_average");
                                        coachList.add(coachesModel(id,fname, lname, rating, desc, image, ratingAvg));
Log.d("coach list items", id+" "+fname+" "+lname+" "+rating+" "+image+" "+desc+" "+ratingAvg);

                                    }
                                    }   else{
                                        mHaveMoreDataToLoad=false;
                                    }

                                 setCoachList(coachList);
                                   /*if(page==0 && coachList.isEmpty())
                                   {
                                       coachesListView.setVisibility(View.GONE);
                                       textError.setVisibility(View.VISIBLE);
                                   }*/
                        }
                                 if(coachesListView.getAdapter()==null){
                                     coachListAdapter = new CoachListAdapter(CoachListActivity.this, coachList);
                                     coachesListView.setAdapter(coachListAdapter);
                                    Log.d("i am here at adapter", "jhjgxbexey");

                                 }
                                 else
                                 {
                                     coachesListView.loadMoreCompleat();
                                     coachListAdapter.notifyDataSetChanged();

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
                        Toast.makeText(CoachListActivity.this, "Timeout Error",
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
                    Log.d(prefsHelper.getUserIdFromPrefrence(),prefsHelper.getSecHashFromPrefrence());
                    params.put("user_id", prefsHelper.getUserIdFromPrefrence());
                    params.put("user_security_hash", prefsHelper.getSecHashFromPrefrence());
                    params.put("page", ""+page);
                    Log.d("user_id"+ "page", "user_security_hash");

                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(CoachListActivity.this).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchCoachList(final String data,final int page)
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CoachListActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "CoachList " + MapAppConstants.API +"/search_coaches");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/search_coaches", new Response.Listener<String>() {
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

                        }
                        if (serverCode.equalsIgnoreCase("1")) {

                            try {
                              if ("1".equals(serverCode)) {
                                    JSONArray array = object.getJSONArray("data");

                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject objet = array.getJSONObject(i);
                                            String id = "", fname = "", lname = "", rating = "", image = "", desc = "", ratingAvg="";
                                            id = objet.getString("user_id");
                                            fname = objet.getString("user_first_name");
                                            lname = objet.getString("user_last_name");
                                            rating = objet.getString("user_rating_count");
                                            image = objet.getString("user_profile_image_url");
                                            desc = objet.getString("user_description");
                                            ratingAvg=objet.getString("user_rating_average");
                                            searchedCoachList.add(coachesModel(id, fname, lname, rating, desc, image, ratingAvg));
                                            Log.d("coach list items", id+" "+fname+" "+lname+" "+rating+" "+image+" "+desc+" "+ratingAvg);


                                        }
                                    }
                                    else{
                                        mHaveMoreDataToLoad=false;
                                    }

                                    setSCoachList(searchedCoachList);
                                 /* if(page==0 && searchedCoachList.isEmpty())
                                  {
                                      coachesListView.setVisibility(View.GONE);
                                      textError.setVisibility(View.VISIBLE);
                                  }*/
                                }

                                if(!searchedCoachList.isEmpty()) {
                                        coachListAdapter = new CoachListAdapter(CoachListActivity.this, searchedCoachList);
                                        coachesListView.setAdapter(coachListAdapter);
                                        coachesListView.setVisibility(View.VISIBLE);
                                        coachesListView.loadMoreCompleat();
                                        coachListAdapter.notifyDataSetChanged();


                                }
                                else
                                {
                                    coachesListView.setVisibility(View.GONE);
                                    Toast.makeText(CoachListActivity.this, "Sorry no data found", Toast.LENGTH_SHORT).show();

                                }

                                Log.d("i am here at adapter", "jhjgxbexey");

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
                        Toast.makeText(CoachListActivity.this, "Timeout Error",
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
                    params.put("search", data);
                    params.put("page", ""+page);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(CoachListActivity.this).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    protected void onDestroy(){
        super.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();


    }
    @Override
    public void onBackPressed() {

        Toast.makeText(CoachListActivity.this,"Please Use Side Menu", Toast.LENGTH_SHORT).show();
    }

}
