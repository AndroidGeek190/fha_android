/*
package com.erginus.fithealthy;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentCallbacks;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import com.erginus.fithealthy.calendar.CaldroidFragment;
import com.erginus.fithealthy.commons.CustomDateTimePicker;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.model.BookingModel;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CoachChatBookingFormActivity extends ActionBarActivity {
Button cntinue;
    LinearLayout ll_back;
    RelativeLayout relativeLayDate, relativeLayTime;
    TextView slctDt, txt_gmt,txt_usrNm, txt_desc;
    PrefsHelper prefsHelper;
    Spinner length_time, slctTopic;
    public static final int REQUEST_CODE_SELECT_DATE = 1;
    CustomDateTimePicker custom;
    Date startDtTime, endDtTime;
    ImageView img_profile;
    SimpleDateFormat format,serverFormat;
    String start_date_time;
    String end_date_time, selectedTime;
    String coach_id, availabaity_id, lengthOfTime;
    String startFrom;
    String endTo;
    String chat;
    String video;
    String gupVdo;
    String available_dt, available_time, dttt,dttt22,tim,time22, from_time,to_time, tot_time, dt, selectedDate;
    List<BookingModel> avail_list;
    List<String> topic_id_list,selected_avail_list,avaialable_dates_list, avail_ftime_list, avail_t_time_list;
    CaldroidFragment dialogCaldroidFragment;
    SimpleDateFormat dt1, timeFrmt, tfrmt, newtimeFrmt;
    Date avail_date=null, avail_time_frm=null, avail_time_to=null, timefrm=null, timeto=null;
    TimePicker timePicker;
    int hour, minut, av_id,time_length;
    List<String> time;
    Calendar c1, c2, c3;
    Date x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_coach_booking_form);
        ll_back = (LinearLayout) findViewById(R.id.linear_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        prefsHelper=new PrefsHelper(CoachChatBookingFormActivity.this);
        format=new SimpleDateFormat("dd-MM-yyyy");
        serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tfrmt=new SimpleDateFormat("HH:mm:ss");
        timeFrmt=new SimpleDateFormat("hh:mm a");
        newtimeFrmt=new SimpleDateFormat("HH:mm:00");
        dt1 = new SimpleDateFormat("yyyy-MM-dd");
        coach_id=getIntent().getStringExtra("id");
        slctDt = (TextView) findViewById(R.id.date_booking);
        txt_gmt=(TextView)findViewById(R.id.txtbooking_group_chat);
        txt_usrNm=(TextView)findViewById(R.id.txtvw_fst_coachename_book);
        txt_desc=(TextView)findViewById(R.id.txtvw_splztn_descptn);
        txt_gmt.setText(prefsHelper.getGMTMessage());
        slctTopic=(Spinner)findViewById(R.id.spinerDate);
        length_time=(Spinner)findViewById(R.id.spinner);
        img_profile=(ImageView)findViewById(R.id.img_icon_book);
        relativeLayDate=(RelativeLayout)findViewById(R.id.rl_selectDate);
        prefsHelper=new PrefsHelper(CoachChatBookingFormActivity.this);
        img_profile.setImageDrawable(LoadImageFromWebOperations(getIntent().getStringExtra("img")));
        txt_usrNm.setText(getIntent().getStringExtra("usrNm"));
        txt_desc.setText(getIntent().getStringExtra("desc"));
        avail_list=new ArrayList<BookingModel>();
        selected_avail_list=new ArrayList<String>();
        avaialable_dates_list=new ArrayList<String>();
        avail_ftime_list=new ArrayList<String>();
        avail_t_time_list=new ArrayList<String>();
        avail_list= (List<BookingModel>) getIntent().getSerializableExtra("c_list");
        topic_id_list=new ArrayList<>();
        topic_id_list= (List<String>) getIntent().getSerializableExtra("t1_list");
        time= new ArrayList<String>();
        time.add("20 min");
        time.add("30 min");
        time.add("40 min");
        time.add("50 min");
        time.add("60 min");
        ArrayAdapter arrayAdapter=new ArrayAdapter<String>(CoachChatBookingFormActivity.this,R.layout.layout_dropdown, time);
        length_time.setAdapter(arrayAdapter);
        ArrayAdapter adapter=new ArrayAdapter<String>(CoachChatBookingFormActivity.this,R.layout.layout_dropdown, topic_id_list);
        slctTopic.setAdapter(adapter);
        slctTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String topic = topic_id_list.get(position);
                Log.d("selected tpic", topic);
                //  Toast.makeText(CoachChatBookingFormActivity.this,topic+" is selected",Toast.LENGTH_SHORT).show();
                for (int i = 0; i < avail_list.size(); i++) {
                    String topicName = avail_list.get(i).getTopicName();
                    if (topicName.equals(topic)) {
                        available_dt = avail_list.get(i).getAvaialbilityFrom();
                        selected_avail_list.add(available_dt);
                        avaialable_dates_list = sortList(selected_avail_list);
                    }
                }
                Log.d("selected datessssss", selected_avail_list.toString() + " " + selected_avail_list.size() + "");
                Log.d("sorted list", avaialable_dates_list.toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        length_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                lengthOfTime= time.get(position);
                int istSpc=lengthOfTime.indexOf(" ");
                 time_length= Integer.parseInt(lengthOfTime.substring(0, istSpc));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        slctDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           dialog1();
            }
        });
        cntinue = (Button) findViewById(R.id.btn_contnu);
        cntinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookCoach();
            }
        });

    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    private void dialog1() {
        dialogCaldroidFragment = CaldroidFragment.newInstance("Select a date", Calendar.MONTH + 1, 2015);
        dialogCaldroidFragment.show(getSupportFragmentManager(), "TAG");
        for (int ii = 0; ii < selected_avail_list.size(); ii++) {
            String dtStart = selected_avail_list.get(ii);
            try {
                avail_date = dt1.parse(dtStart);
                dialogCaldroidFragment.setBackgroundResourceForDate(R.color.action_bar_color, avail_date);
                dialogCaldroidFragment.setTextColorForDate(R.color.caldroid_white, avail_date);
                dialogCaldroidFragment.refreshView();
                } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                int count=0;
                    Date currentdate = new Date();
                Log.d("dateeeeeeeeeeeeeee",date+"");
                    // int i = date.compareTo(currentdate);
                    SimpleDateFormat dt2 = new SimpleDateFormat("dd");
                    selectedDate = dt1.format(date);
                    String crntDate = dt1.format(currentdate);
                    if (selectedDate.compareTo(crntDate) > 0 || selectedDate.compareTo(crntDate) == 0) {

                        for (int i = 0; i < selected_avail_list.size(); i++) {
                            String dtStart = selected_avail_list.get(i);
                            try {
                                avail_date=dt1.parse(dtStart);
                                dt=dt1.format(avail_date);
                                Log.d("dkshdl fhf", avail_date.toString());
                                if (dt.compareTo(selectedDate)==0) {
                                    selectedDate = dt;

                                    Log.d("jjsef 745335ieheudies", selectedDate);
                                    for(int j=0;j<avail_list.size();j++)
                                    {
                                        avail_ftime_list.clear();
                                        String t_from=avail_list.get(j).getAvaialbilityFrom();
                                        String t_to=avail_list.get(j).getAvaialbilityTo();
                                        availabaity_id=avail_list.get(j).getAvailabilityId();
                                        avail_time_frm=serverFormat.parse(t_from);
                                        avail_time_to=serverFormat.parse(t_to);
                                        String av_time_frm=serverFormat.format(avail_time_frm);
                                        String av_time_to=serverFormat.format(avail_time_to);
                                        int istSpc=av_time_frm.indexOf(" ");
                                        dttt = av_time_frm.substring(0, istSpc);
                                        tim = av_time_frm.substring(istSpc).trim();
                                        Log.d("time.........", tim);
                                        dttt22 = av_time_to.substring(0, istSpc);
                                        time22 = av_time_to.substring(istSpc).trim();
                                        Log.d("time.........", time22);
                                        if(dttt.equals(dt) ||dttt22.equals(dt))
                                        {
                                            timefrm=tfrmt.parse(tim);
                                            timeto=tfrmt.parse(time22);
                                            from_time = timeFrmt.format(timefrm);
                                            to_time=timeFrmt.format(timeto);
                                            tot_time=from_time+" to "+to_time;
                                            avail_ftime_list.add(tot_time);
                                            avail_t_time_list.add(availabaity_id);
                                            Log.d("timeeeeett", avail_ftime_list.toString()+ avail_ftime_list.size());
                                            c1=Calendar.getInstance();
                                            c1.setTime(timefrm);

                                        }


                                    }
                                    dialogCaldroidFragment.dismiss();
                                    dialog_time_picker();
                                    break;
                                }
                                else {
                                    Toast.makeText(CoachChatBookingFormActivity.this, "Coach Not Available", Toast.LENGTH_SHORT);
                                    count++;
                                    continue;

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                         }

                    } else {
                        Toast.makeText(CoachChatBookingFormActivity.this, "Coach Not Avaialable.", Toast.LENGTH_SHORT).show();
                    }

            }
            @Override
            public void onChangeMonth(int month, int year) {
            }


            @Override
            public void onCaldroidViewCreated() {

            }
        };
        dialogCaldroidFragment.setCaldroidListener(listener);
    }

    private void dialog_time_picker() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Select Available Time");
        dialog.setContentView(R.layout.dialog_time_picker);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
      //  Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button done = (Button) dialog.findViewById(R.id.btn_done);
        timePicker = (TimePicker)dialog.findViewById(R.id.timePicker);
        TextView availdt=(TextView)dialog.findViewById(R.id.date_text);
        availdt.setText("Selected Date-"+" "+format.format(avail_date));
        TextView availtime=(TextView)dialog.findViewById(R.id.time_text);
        String time = "";
        for(int k=0; k<avail_ftime_list.size();k++)
        {
            time=time+" \n"+avail_ftime_list.get(k);

            Log.d("time 888888888", time);
            availtime.setText("Available Time:- "+time);

        }
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                hour = timePicker.getCurrentHour();
                minut = timePicker.getCurrentMinute();

                selectedTime = hour + ":" + minut + ":" + 00;
                c2 = Calendar.getInstance();
                x = c2.getTime();
                for (int l = 0; l < avail_t_time_list.size(); l++) {
                    if (x.after(c1.getTime())) {
                        av_id = Integer.parseInt(avail_t_time_list.get(l));

                    }
                }

                try {
                    c2.setTime(tfrmt.parse(selectedTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date tim_sl = null;
                try {
                    tim_sl=tfrmt.parse(selectedTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                start_date_time = dt + " " +tfrmt.format(tim_sl);
                Log.d("selectd time,", start_date_time);

                slctDt.setText(format.format(avail_date)+" "+selectedTime);


            }
    }

       );
       dialog.show();
   }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
        dialogCaldroidFragment.restoreDialogStatesFromKey(getSupportFragmentManager(),
                savedInstanceState, "DIALOG_CALDROID_SAVED_STATE",
                "CALDROID_DIALOG_FRAGMENT");
        Bundle args = dialogCaldroidFragment.getArguments();
        args.putString("dialogTitle", "Select a date");
    } else {
        // Setup arguments
        Bundle bundle = new Bundle();
        // Setup dialogTitle
        bundle.putString(CaldroidFragment.DIALOG_TITLE, "Select a date");
        dialogCaldroidFragment.setArguments(bundle);
    }
    }

    private void dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_in_app_purchase);
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button cancel = (Button) dialog.findViewById(R.id.btn_cance);
        Button buy = (Button) dialog.findViewById(R.id.btn_buy);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void bookCoach()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CoachChatBookingFormActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Answer " + MapAppConstants.API + MapAppConstants.BOOK);
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+MapAppConstants.BOOK, new Response.Listener<String>() {
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
                        Toast.makeText(CoachChatBookingFormActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if (MapAppConstants.RESPONSE_CODE_ONE.equals(serverCode)) {
                                    JSONObject object1 = object.getJSONObject(MapAppConstants.DATA);
                                    String usertkn=object1.getString(MapAppConstants.USER_TOKEN_COUNT);
                                    prefsHelper.storeTokenToPreference(usertkn);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }}

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
                        Toast.makeText(CoachChatBookingFormActivity.this, "Timeout Error",
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
                    params.put(MapAppConstants.USER_ID, prefsHelper.getStudentIdFromPreference());
                    params.put(MapAppConstants.USER_SEC_HASH,prefsHelper.getSecHashFromPrefrence());
                    params.put(MapAppConstants.AVAILABILITIES_ID,av_id+"");
                    params.put(MapAppConstants.BOOKING_LENGTH,time_length+"");
                    params.put(MapAppConstants.BOOKING_START_TIME, start_date_time);
                      Log.d(av_id+"",time_length+"");
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
    public static List<String> sortList(List<String> myList){
        Set<String> hashsetList = new HashSet<String>(myList);
        List<String> sortlist=new ArrayList<>(hashsetList);
        System.out.printf("\nUnique values using HashSet: %s%n", hashsetList);
        return sortlist;
    }

    @Override
    public void unregisterComponentCallbacks(ComponentCallbacks observer) {
        if (observer != null) {
            super.unregisterComponentCallbacks(observer);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

       // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
*/
