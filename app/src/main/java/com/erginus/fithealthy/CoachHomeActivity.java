package com.erginus.fithealthy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RatingBar;
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
import com.erginus.fithealthy.adapter.GoalConsultationAdapter;
import com.erginus.fithealthy.adapter.MenuListAdapter;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.fragments.ChangePasswordFragment;
import com.erginus.fithealthy.fragments.CoachAvailabilityFragment;
import com.erginus.fithealthy.fragments.CoachBookingListFragment;
import com.erginus.fithealthy.fragments.EditCoachProfileFragment;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.model.GoalConsultationModel;
import com.jafarkhq.views.EndlessListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoachHomeActivity extends ActionBarActivity {
    List<GoalConsultationModel> questionList, searchedQuestionList;
    String[] title;
    EditText edit_search;
    int icon[];
    PrefsHelper prefsHelper;
    DrawerLayout drawerLayout;
    ListView listView;

    LinearLayout linearLayout, ll_spce, ll_search,ll_back;
    MenuListAdapter menuListAdapter;
    int page;
    ImageView nav_icon, search, logo, add_icon;
    public static ImageView profileImage;
    FrameLayout f;
    public  static TextView name, user_name;
    GoalConsultationAdapter goalConsultationAdapter;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    LinearLayout navigation;
    private EndlessListView endlessListView;
    private boolean mHaveMoreDataToLoad;
    String search_data;
    public static RatingBar user_rating;
    TextView textError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_home);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams
                (ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.FILL_HORIZONTAL | Gravity.CENTER_VERTICAL);
        View customNav = LayoutInflater.from(this).inflate(R.layout.custom_act_bar, null);
        actionBar.setCustomView(customNav, layout);
        Toolbar parent =(Toolbar) customNav.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);// set padding programmatically to 0dp
        prefsHelper=new PrefsHelper(this);
        f=(FrameLayout)findViewById(R.id.content_frame);
        profileImage=(ImageView)findViewById(R.id.ivwLogo);
        user_name=(TextView)findViewById(R.id.usrName);
        user_rating=(RatingBar)findViewById(R.id.ratingBar3);
        user_name.setText(prefsHelper.getUserFNameFromPreference() + " " + prefsHelper.getUserLNameFromPreference());
        user_rating.setRating(Float.parseFloat(prefsHelper.getRateFromPreference()));
        textError=(TextView)findViewById(R.id.textView5);
        Picasso.with(this).load(prefsHelper.getUserImageFromPreference()).into(profileImage);
        name=(TextView)actionBar.getCustomView().findViewById(R.id.menu_name_tv);
        endlessListView=(EndlessListView)findViewById(R.id.dis_lv);
        title = new String[] {"Goal Consultation Room","Edit Profile", "Change Password",
                "My Bookings", "Availability", "Logout" };
        icon=new int[]{R.drawable.nav_goal_consultion_room_icon_green, R.drawable.nav_edit_profile_icon, R.drawable.change_password,
                R.drawable.nav_mybooking_icon,R.drawable.nav_tracking_progress_icon, R.drawable.nav_logout_icon};
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        listView=(ListView)findViewById(R.id.listview_drawer);

        linearLayout=(LinearLayout)findViewById(R.id.left_drawer);
        menuListAdapter=new MenuListAdapter(CoachHomeActivity.this, title, icon);
        nav_icon=(ImageView)actionBar.getCustomView().findViewById(R.id.menu_navi);
        search=(ImageView)actionBar.getCustomView().findViewById(R.id.menu_search);
        ll_back=(LinearLayout)actionBar.getCustomView().findViewById(R.id.search_bar);
        logo=(ImageView)actionBar.getCustomView().findViewById(R.id.action_bar_logo);
        add_icon=(ImageView)actionBar.getCustomView().findViewById(R.id.action_bar_add);
        edit_search=(EditText)actionBar.getCustomView().findViewById(R.id.editTxt_Search);
        navigation=(LinearLayout)actionBar.getCustomView().findViewById(R.id.navigation_lay);
        ll_spce=(LinearLayout)actionBar.getCustomView().findViewById(R.id.space);
        ll_search=(LinearLayout)actionBar.getCustomView().findViewById(R.id.search_bar);
        questionList=new ArrayList<GoalConsultationModel>();
        searchedQuestionList=new ArrayList<GoalConsultationModel>();

        questionList(page);
        mHaveMoreDataToLoad = true;
        endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (true == mHaveMoreDataToLoad) {
                    loadMoreData();
                } else {
                    Toast.makeText(CoachHomeActivity.this, "No more data to load",
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
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edtPrf = new Intent(CoachHomeActivity.this, CoachHomeActivity.class);
                startActivity(edtPrf);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    endlessListView.setVisibility(View.GONE);
                    search_data = edit_search.getText().toString();
                    searchedQuestionList.clear();
                    getQuestionList().clear();
                    searchQuestionList(search_data, page);
                    mHaveMoreDataToLoad = true;
                    endlessListView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
                        @Override
                        public boolean onLoadMore() {
                            if (true == mHaveMoreDataToLoad) {
                                loadMoreData(search_data);
                            } else {
                                Toast.makeText(CoachHomeActivity.this, "No more data to load",
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
        listView.setAdapter(menuListAdapter);
        listView.setOnItemClickListener(new DrawerItemClickListener());
        if(drawerLayout!=null) {
            drawerLayout.setDrawerShadow(R.drawable.list_back, GravityCompat.START);

            mDrawerToggle = new ActionBarDrawerToggle(CoachHomeActivity.this, drawerLayout,
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

        questionList=new ArrayList<GoalConsultationModel>();



        endlessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i1 = new Intent(CoachHomeActivity.this, CoachAnswerActivity.class);
                i1.putExtra("id", questionList.get(position).getQuestionId());
                i1.putExtra("topic", questionList.get(position).getTopic());
                i1.putExtra("question", questionList.get(position).getQuestion());
                i1.putExtra("count",questionList.get(position).getQuesAnsCount());
                i1.putExtra("time", questionList.get(position).getQuesTime());
                i1.putExtra("image", questionList.get(position).getImage());
                startActivity(i1);

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

    private void loadMoreData() {
        page++;
        questionList(page);

    }

    private void loadMoreData(String dta) {
        page++;
        searchQuestionList(dta,page);

    }

    private  class DrawerItemClickListener implements  ListView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerLayout.closeDrawer(linearLayout);

            if(position==0){
                finish();
                Intent edtPrf=new Intent(CoachHomeActivity.this,CoachHomeActivity.class);
                startActivity(edtPrf);
                overridePendingTransition(0, 0);

            }
            if(position==1){
                f.removeAllViews();
                name.setText("Edit Profile");
                search.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                add_icon.setVisibility(View.GONE);
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new EditCoachProfileFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

            if(position==2){
                f.removeAllViews();
                name.setText("Change Password");
                search.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                add_icon.setVisibility(View.GONE);
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new ChangePasswordFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

            if(position==3){
                f.removeAllViews();
                name.setText("My Bookings");
                search.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                add_icon.setVisibility(View.GONE);
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new CoachBookingListFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            if(position==4){
                f.removeAllViews();
                name.setText("Availabilty");
                search.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                add_icon.setVisibility(View.GONE);
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new CoachAvailabilityFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }


            if(position==5)
            {


                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CoachHomeActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.clear();
                editor.commit();

                finish();
            }

        }
    }

    public List<GoalConsultationModel> getQuestionList() {
        return questionList;
    }
    public List<GoalConsultationModel> getSQuestionList() {
        return searchedQuestionList;
    }

    public void setSQuestionList(List<GoalConsultationModel> questionList) {
        this.searchedQuestionList = questionList;
    }


    public void setQuestionList(List<GoalConsultationModel> questionList) {
        this.questionList = questionList;
    }
    private GoalConsultationModel goalConsultationModel(String qId, String quesTopic, String quesValue,  String queAnsCount, String userId, String time, String image)
    {
        GoalConsultationModel goal = new GoalConsultationModel();
        goal.setQuestionId(qId);
        goal.setTopic(quesTopic);
        goal.setQuestion(quesValue);
        goal.setQuesAnsCount(queAnsCount);
        goal.setUserId(userId);
        goal.setQuesTime(time);
        goal.setImage(image);
        return goal;
    }


    public void questionList(final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CoachHomeActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "CoachList " + MapAppConstants.API +"/questions_list");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstants.API +"/questions_list", new Response.Listener<String>() {
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
                            Toast.makeText(CoachHomeActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();

                        }
                        if (serverCode.equalsIgnoreCase("1")) {

                            try {
                                if ("1".equals(serverCode)) {
                                    JSONArray array = object.getJSONArray("data");

                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject objet = array.getJSONObject(i);
                                            String id = "", topic = "", question = "", count = "", time = "", userID= "", image="";
                                            id = objet.getString("question_id");
                                            topic = objet.getString("question_topic");
                                            question = objet.getString("question_value");
                                            count = objet.getString("question_answer_count");
                                            time = objet.getString("question_created_timestamp");
                                            userID = objet.getString("users_id");
                                            image=objet.getString("user_profile_image_url");

                                            questionList.add(goalConsultationModel(id,topic,question,count,userID, time, image));

                                        }
                                    }
                                    else{
                                        mHaveMoreDataToLoad=false;
                                    }

                                    setQuestionList(questionList);
                                   /* if(page==0 && questionList.isEmpty())
                                    {
                                        endlessListView.setVisibility(View.GONE);
                                        textError.setVisibility(View.VISIBLE);
                                    }*/

                                }
                                if(endlessListView.getAdapter()==null){
                                    goalConsultationAdapter = new GoalConsultationAdapter(CoachHomeActivity.this, questionList);
                                    endlessListView.setAdapter(goalConsultationAdapter);

                                    Log.d("i am here at adapter", "jhjgxbexey");
                                }
                                else{
                                    endlessListView.loadMoreCompleat();
                                    goalConsultationAdapter.notifyDataSetChanged();
                                }


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
                        Toast.makeText(CoachHomeActivity.this, "Timeout Error",
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
                    params.put("page", ""+page);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(CoachHomeActivity.this).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void searchQuestionList(final String data,final int page) {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CoachHomeActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "CoachList " + MapAppConstants.API + "/search_questions");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstants.API + "/search_questions", new Response.Listener<String>() {
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
                                            String id = "", topic = "", question = "", count = "", time = "", userID = "", image = "";
                                            id = objet.getString("question_id");
                                            topic = objet.getString("question_topic");
                                            question = objet.getString("question_value");
                                            count = objet.getString("question_answer_count");
                                            time = objet.getString("question_created_timestamp");
                                            userID = objet.getString("users_id");
                                            image = objet.getString("user_profile_image_url");

                                            searchedQuestionList.add(goalConsultationModel(id, topic, question, count, userID, time, image));

                                        }
                                    }   else{
                                        mHaveMoreDataToLoad=false;
                                    }

                                    setSQuestionList(searchedQuestionList);
                                   /* if(page==0 && searchedQuestionList.isEmpty())
                                    {
                                        endlessListView.setVisibility(View.GONE);
                                        textError.setVisibility(View.VISIBLE);
                                    }*/
                                }
                                if(!searchedQuestionList.isEmpty()) {
                                    goalConsultationAdapter = new GoalConsultationAdapter(CoachHomeActivity.this, searchedQuestionList);
                                    endlessListView.setAdapter(goalConsultationAdapter);
                                    endlessListView.setVisibility(View.VISIBLE);
                                    endlessListView.loadMoreCompleat();
                                    goalConsultationAdapter.notifyDataSetChanged();
                                    Log.d("i am here at adapter", "jhjgxbexey");
                                }

                                else
                                {
                                    endlessListView.setVisibility(View.GONE);
                                    Toast.makeText(CoachHomeActivity.this, "Sorry no data found", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(CoachHomeActivity.this, "Timeout Error",
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
                    params.put("page", "" + page);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(CoachHomeActivity.this).addToRequestQueue(sr);


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

        Toast.makeText(CoachHomeActivity.this,"Please Use Side Menu", Toast.LENGTH_SHORT).show();
    }
}


