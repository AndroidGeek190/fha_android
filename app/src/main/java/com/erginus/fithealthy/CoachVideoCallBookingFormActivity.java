package com.erginus.fithealthy;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentCallbacks;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.erginus.fithealthy.calendar.CaldroidFragment;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.GlobalPrefrences;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.model.BookingModel;

import com.roomorama.caldroid.CaldroidListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

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

public class CoachVideoCallBookingFormActivity extends ActionBarActivity {
    Button cntinue;
    LinearLayout ll_back;
    RelativeLayout relativeLayDate, relativeLayTime;
    TextView slctDt, txt_gmt,txt_usrNm, txt_desc;
    PrefsHelper prefsHelper;
    Spinner length_time, slctTopic;
    ImageView img_profile;
    SimpleDateFormat format,serverFormat;
    String start_date_time;
    String end_date_time, selectedTime;
    String coach_id, availabaity_id, lengthOfTime;
    String available_dt, available_time, dttt,dttt22,tim,time22, from_time,to_time, tot_time, dt, selectedDate;
    List<BookingModel> avail_list;
    List<String> topic_id_list,selected_avail_list,avaialable_dates_list, avail_ftime_list, avail_t_time_list;
    CaldroidFragment dialogCaldroidFragment;
    SimpleDateFormat dt1, timeFrmt, tfrmt, newtimeFrmt;
    Date avail_date=null, avail_time_frm=null, avail_time_to=null, timefrm=null, timeto=null;
    int hour, minute, av_id,time_length;
    List<String> time;
    Calendar c1, c2, c3;
    Date x;
    Date tim_sl=null;
    GlobalPrefrences globalPrefrences;

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

            }
        });
        prefsHelper=new PrefsHelper(CoachVideoCallBookingFormActivity.this);
        globalPrefrences=new GlobalPrefrences(this);
        format=new SimpleDateFormat("dd-MM-yyyy");
        serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tfrmt=new SimpleDateFormat("HH:mm:ss");
        timeFrmt=new SimpleDateFormat("hh:mm a");
        dt1 = new SimpleDateFormat("yyyy-MM-dd");
        newtimeFrmt=new SimpleDateFormat("HH:mm:00");
        coach_id=getIntent().getStringExtra("id");
        slctDt = (TextView) findViewById(R.id.date_booking);
        txt_gmt=(TextView)findViewById(R.id.txtbooking_group_chat);
        txt_usrNm=(TextView)findViewById(R.id.txtvw_fst_coachename_book);
        txt_desc=(TextView)findViewById(R.id.txtvw_splztn_descptn);
        txt_gmt.setText(globalPrefrences.getGMTMessage());
        slctTopic=(Spinner)findViewById(R.id.spinerDate);
        length_time=(Spinner)findViewById(R.id.spinner);
        img_profile=(ImageView)findViewById(R.id.img_icon_book);
        relativeLayDate=(RelativeLayout)findViewById(R.id.rl_selectDate);
        prefsHelper=new PrefsHelper(CoachVideoCallBookingFormActivity.this);
        Picasso.with(this).load(getIntent().getStringExtra("img")).into(img_profile);
        txt_usrNm.setText(getIntent().getStringExtra("usrNm"));
        txt_desc.setText(getIntent().getStringExtra("desc"));
        avail_list=new ArrayList<BookingModel>();
        selected_avail_list=new ArrayList<String>();
        avaialable_dates_list=new ArrayList<String>();
        avail_ftime_list=new ArrayList<String>();
        avail_t_time_list=new ArrayList<String>();
        avail_list= (List<BookingModel>) getIntent().getSerializableExtra("v_list");
        topic_id_list=new ArrayList<>();
        topic_id_list= (List<String>) getIntent().getSerializableExtra("t2_list");
        time= new ArrayList<String>();

        time.add("20 Minutes");
        time.add("30 Minutes");
        time.add("40 Minutes");
        time.add("50 Minutes");
        time.add("60 Minutes");
        ArrayAdapter arrayAdapter=new ArrayAdapter<String>(CoachVideoCallBookingFormActivity.this,R.layout.layout_dropdown, time);
        length_time.setAdapter(arrayAdapter);
        ArrayAdapter adapter=new ArrayAdapter<String>(CoachVideoCallBookingFormActivity.this,R.layout.layout_dropdown, topic_id_list);
        slctTopic.setAdapter(adapter);
        slctTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String topic = topic_id_list.get(position);
                Log.d("selected tpic", topic);
                selected_avail_list.clear();
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

    private void dialog1() {
        dialogCaldroidFragment = CaldroidFragment.newInstance("Select a date", Calendar.MONTH + 1, 2015);
        dialogCaldroidFragment.show(getSupportFragmentManager(), "TAG");
        for (int ii = 0; ii < selected_avail_list.size(); ii++) {

            String dtStart = selected_avail_list.get(ii);
            try {
                avail_date = dt1.parse(dtStart);
                dialogCaldroidFragment.setBackgroundResourceForDate(R.color.primary, avail_date);
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

                if (selectedDate.compareTo(crntDate) > 0 || selectedDate.compareTo(crntDate) <= 0) {

                    for (int i = 0; i < selected_avail_list.size(); i++) {

                        String dtStart = selected_avail_list.get(i);
                        try {
                            avail_date=dt1.parse(dtStart);

                            dt=dt1.format(avail_date);
                            Log.d("dkshdl fhf", avail_date.toString());
                            if (dt.compareTo(selectedDate)==0) {
                                selectedDate = dt;
                                avail_ftime_list.clear();

                                Log.d("jjsef 745335ieheudies", selectedDate);
                                for(int j=0;j<avail_list.size();j++)
                                {
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
                                Toast.makeText(CoachVideoCallBookingFormActivity.this, "Coach Not Available", Toast.LENGTH_SHORT);
                                count++;
                                continue;

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                } /*else {
                    Toast.makeText(CoachVideoCallBookingFormActivity.this, "Coach Not Available", Toast.LENGTH_SHORT).show();
                }*/
               // dialogCaldroidFragment.dismiss();
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
            Button cancel = (Button) dialog.findViewById(R.id.button_cancel);
            Button done = (Button) dialog.findViewById(R.id.button_set);
            final TextView availdt = (TextView) dialog.findViewById(R.id.date_text);
            availdt.setText("Selected Date-" + " " + format.format(avail_date));
            final TextView availtime = (TextView) dialog.findViewById(R.id.time_text);
            String time = "";
            final Spinner spr_hr, spr_min, spr_am_pm;

            for (int k = 0; k < avail_ftime_list.size(); k++) {

                time = time + "\n " + avail_ftime_list.get(k);

                Log.d("time 888888888", time);
                availtime.setText("Available Time:- " + time);

            }

            spr_hr = (Spinner) dialog.findViewById(R.id.spinner_hour);
            spr_min = (Spinner) dialog.findViewById(R.id.spinner_min);
            spr_am_pm = (Spinner) dialog.findViewById(R.id.spinner_am_pm);


            final List<String> list2 = new ArrayList<String>();
            list2.add("00");
            list2.add("01");
            list2.add("02");
            list2.add("03");
            list2.add("04");
            list2.add("05");
            list2.add("06");
            list2.add("07");
            list2.add("08");
            list2.add("09");
            list2.add("10");
            list2.add("11");
            list2.add("12");
            list2.add("13");
            list2.add("14");
            list2.add("15");
            list2.add("16");
            list2.add("17");
            list2.add("18");
            list2.add("19");
            list2.add("20");
            list2.add("21");
            list2.add("22");
            list2.add("23");
            list2.add("24");
            list2.add("25");
            list2.add("26");
            list2.add("27");
            list2.add("28");
            list2.add("29");
            list2.add("30");
            list2.add("31");
            list2.add("32");
            list2.add("33");
            list2.add("34");
            list2.add("35");
            list2.add("36");
            list2.add("37");
            list2.add("38");
            list2.add("39");
            list2.add("40");
            list2.add("41");
            list2.add("42");
            list2.add("43");
            list2.add("44");
            list2.add("45");
            list2.add("46");
            list2.add("47");
            list2.add("48");
            list2.add("49");
            list2.add("50");
            list2.add("51");
            list2.add("52");
            list2.add("53");
            list2.add("54");
            list2.add("55");
            list2.add("56");
            list2.add("57");
            list2.add("58");
            list2.add("59");

            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(CoachVideoCallBookingFormActivity.this,
                    R.layout.layout_dropdown, list2);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spr_min.setAdapter(dataAdapter2);
            spr_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    minute = Integer.parseInt(spr_min.getSelectedItem().toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            final List<String> list1 = new ArrayList<String>();

            list1.add("00");
            list1.add("01");
            list1.add("02");
            list1.add("03");
            list1.add("04");
            list1.add("05");
            list1.add("06");
            list1.add("07");
            list1.add("08");
            list1.add("09");
            list1.add("10");
            list1.add("11");
            list1.add("12");
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(CoachVideoCallBookingFormActivity.this,
                    R.layout.layout_dropdown, list1);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spr_hr.setAdapter(dataAdapter1);
            spr_hr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    hour = Integer.parseInt(spr_hr.getSelectedItem().toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            final List<String> list = new ArrayList<String>();
            list.add("AM");
            list.add("PM");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CoachVideoCallBookingFormActivity.this,
                    R.layout.layout_dropdown, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spr_am_pm.setAdapter(dataAdapter);
            spr_am_pm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String am_pm = spr_am_pm.getSelectedItem().toString();
                    if (am_pm.equals("AM")) {
                        if (hour < 12) {
                            hour = hour;
                        } else if (hour == 12) {
                            hour = hour - 12;
                        }
                    } else {

                        if (hour < 12) {
                            hour = hour + 12;
                        } else if (hour == 12) {
                            hour = 12;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            done.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            availtime.setText("");
                                            selectedTime = hour + ":" + minute + ":" + 00;

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
                                                tim_sl = tfrmt.parse(selectedTime);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            start_date_time = dt + " " + tfrmt.format(tim_sl);
                                            Log.d("selectd time,", start_date_time);

                                            slctDt.setText(start_date_time);


                                        }
                                    }

            );

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    availtime.setText("");
                }
            });
            dialog.show();
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


    public void bookCoach()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(CoachVideoCallBookingFormActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Answer " + MapAppConstants.API + "/book");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/book", new Response.Listener<String>() {
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
                        Toast.makeText(CoachVideoCallBookingFormActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject object1 = object.getJSONObject("data");
                                    String usertkn=object1.getString("user_token_count");
                                    prefsHelper.storeTokenToPreference(usertkn);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            GoalConsultationRoomActivity.user_tokn.setText(prefsHelper.getTokenFromPreference()+" "+"Tokens");
                            CoachListActivity.user_tokn.setText(prefsHelper.getTokenFromPreference()+" "+"Tokens");
                            BuyTokenActivity.user_tokn.setText(prefsHelper.getTokenFromPreference()+" "+"Tokens");
                            BuyTokenActivity.txt_redm_tokn.setText(prefsHelper.getTokenFromPreference()+" "+"Tokens");
                            Intent intent=new Intent(CoachVideoCallBookingFormActivity.this, CoachListActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();

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
                        Toast.makeText(CoachVideoCallBookingFormActivity.this, "Timeout Error",
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
                    params.put("user_security_hash",prefsHelper.getSecHashFromPrefrence());
                    params.put("availabilities_id",av_id+"");
                    params.put("booking_length",time_length+"");
                    params.put("booking_start_time", start_date_time);
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
    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onPause() {
        super.onPause();


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


    }

}
