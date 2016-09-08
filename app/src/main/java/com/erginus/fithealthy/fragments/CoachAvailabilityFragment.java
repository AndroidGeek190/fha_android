package com.erginus.fithealthy.fragments;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
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
import com.erginus.fithealthy.CoachHomeActivity;
import com.erginus.fithealthy.GoalConsultationRoomActivity;
import com.erginus.fithealthy.R;
import com.erginus.fithealthy.calendar.CaldroidFragment;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.model.TopicModel;

import com.roomorama.caldroid.CaldroidListener;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;


public class CoachAvailabilityFragment extends android.support.v4.app.Fragment {
    AutoCompleteTextView topic;
    TextView startDate;
    Button update;
    Spinner slctTpc, spr_minutes;
    PrefsHelper prefsHelper;
    int availabiltity_for;
    String start_date_time;
    Calendar myCalendar;
    SimpleDateFormat format, serverFormat, newformat, tfrmt, timeFrmt, newtimeFrmt, dt1;
    List<TopicModel> topic_list, arrayTemplist;
    List<String> time;
    private Button btn_set, btn_cancl;
    Spinner spr_am_pm, spr_hr,spr_min;
    TextView txt_slctd_dt;
    String selectedDate, selectedTime,  hr, lengthOfTime;
    int hour, minute, time_length;

    ArrayAdapter<String> adapter;
    Filter filter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_coach_availability_ew, container, false);
        slctTpc = (Spinner) rootView.findViewById(R.id.chatDate);
        spr_minutes = (Spinner) rootView.findViewById(R.id.spinner_min);
        topic = (AutoCompleteTextView) rootView.findViewById(R.id.txt_topic);
        topic_list = new ArrayList<TopicModel>();
        prefsHelper = new PrefsHelper(getActivity());
        startDate = (TextView) rootView.findViewById(R.id.startDt);

        format = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
        newformat = new SimpleDateFormat("dd-MM-yyyy");
        serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tfrmt=new SimpleDateFormat("HH:mm:ss");
        timeFrmt=new SimpleDateFormat("hh:mm a");
        dt1 = new SimpleDateFormat("yyyy-MM-dd");
        newtimeFrmt=new SimpleDateFormat("HH:mm:00");

        myCalendar = Calendar.getInstance();
        update = (Button) rootView.findViewById(R.id.btn_update);
        time= new ArrayList<String>();
        time.add("20 Minutes");
        time.add("30 Minutes");
        time.add("40 Minutes");
        time.add("50 Minutes");
        time.add("60 Minutes");
        ArrayAdapter arrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.layout_dropdown, time);
        spr_minutes.setAdapter(arrayAdapter);

        filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
            }

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                Log.i("Filter",
                        "Filter:" + constraint + " thread: " + Thread.currentThread());
                if (constraint!=null  && constraint.length()>0) {
                    Log.i("Filter", "doing a search ..");
                    new AdapterUpdaterTask().execute();
                }
                return null;
            }
        };

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_dropdown) {
            public android.widget.Filter getFilter() {
                return filter;
            }
        };

        topic.setAdapter(adapter);
        topic.setThreshold(1);
        adapter.setNotifyOnChange(false);
        spr_minutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                lengthOfTime = time.get(position);
                int istSpc = lengthOfTime.indexOf(" ");
                time_length = Integer.parseInt(lengthOfTime.substring(0, istSpc));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog1();
            }
        });

        final List<String> list = new ArrayList<String>();
     //   list.add("1-2-1 Chat");
        list.add("1-2-1 Video Coaching");
        list.add("Group Video Coaching");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.layout_dropdown, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slctTpc.setAdapter(dataAdapter);
        slctTpc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = slctTpc.getSelectedItem().toString();
               /* if (type.equals("1-2-1 Chat")) {
                    availabiltity_for = 1;
                    Log.d("postonnnn", 1 + "");
                }*/
                if (type.equals("1-2-1 Video Coaching")) {
                    Log.d("postonnnn", 2 + "");
                    availabiltity_for = 2;


                }
                if (type.equals("Group Video Coaching")) {
                    Log.d("postonnnn", 3 + "");
                    availabiltity_for = 3;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Please select a type", Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                availability();
            }
        });
        topic_list();
        return rootView;
    }

    private void dialog1() {
        final CaldroidFragment dialogCaldroidFragment = CaldroidFragment.newInstance("Select a date", Calendar.MONTH+1, 2015);
        dialogCaldroidFragment.show(getFragmentManager(), "TAG");
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Date currentdate = new Date();
                int i = date.compareTo(currentdate);
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate = dt1.format(date);
                String crntDate = dt1.format(currentdate);
                if (selectedDate.compareTo(crntDate) > 0
                        || selectedDate.compareTo(crntDate) <= 0) {
                    dialog3();

                }/* else {
                    Toast.makeText(getActivity(), "Past Date, Please select the correct date not past one.", Toast.LENGTH_SHORT).show();
                }*/
                dialogCaldroidFragment.setBackgroundResourceForDate(R.color.blue_normal,
                        date);
                dialogCaldroidFragment.refreshView();

                dialogCaldroidFragment.dismiss();
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



    public void availability() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Add coach avail " + MapAppConstants.API +"/add_coach_availability");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstants.API +"/add_coach_availability", new Response.Listener<String>() {
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

                                }
                            } catch (Exception e) {
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

                    params.put("user_id", prefsHelper.getUserIdFromPrefrence());
                    params.put("user_security_hash", prefsHelper.getSecHashFromPrefrence());
                    params.put("availability_for", ""+availabiltity_for);
                    params.put("topic_name", topic.getText().toString());
                    params.put("availability_from", start_date_time);
                    params.put("availability_length", ""+time_length);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void dialog3()
    {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Select Time");
        dialog.setContentView(R.layout.dialog_time_picker_1);
        btn_cancl = (Button) dialog.findViewById(R.id.button_cancel);
        Date date=null;
        btn_set= (Button) dialog.findViewById(R.id.button_set);
        try {
            date=dt1.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txt_slctd_dt=(TextView)dialog.findViewById(R.id.text_selct_dt);
        txt_slctd_dt.setText("Selected Date-"+" "+newformat.format(date));
        spr_hr=(Spinner)dialog.findViewById(R.id.spinner_hour);
        spr_min=(Spinner)dialog.findViewById(R.id.spinner_min);
        spr_am_pm=(Spinner)dialog.findViewById(R.id.spinner_am_pm);


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

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(),
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
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(),
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.layout_dropdown, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_am_pm.setAdapter(dataAdapter);
        spr_am_pm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String am_pm = spr_am_pm.getSelectedItem().toString();
                if (am_pm.equals("AM")) {
                    if(hour<12) {
                        hour = hour;
                    }
                    else if(hour==12)
                    {
                        hour=hour-12;
                    }
                }
                else
                {

                    if (hour < 12) {
                        hour = hour + 12;
                    }
                    else  if(hour==12){
                        hour = 12;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                selectedTime = hour + ":" + minute + ":" + 00;
                Date tim_sl = null;
                try {
                    tim_sl = tfrmt.parse(selectedTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                start_date_time = selectedDate + " " + tfrmt.format(tim_sl);
                Log.d("selectd time,", start_date_time);
                startDate.setText(start_date_time);

            }
        });
        btn_cancl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void topic_list() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Topucs" + MapAppConstants.API +"/topics");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstants.API +"/topics", new Response.Listener<String>() {
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
                      //  Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONArray jsonArray = object.getJSONArray("data");
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String topic_id = jsonObject.getString("topic_id");
                                            String topic_nm = jsonObject.getString("topic_name");
                                            topic_list.add(topicModel(topic_id, topic_nm));

                                        }
                                    }
                                    setTopicList(topic_list);
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

                    params.put("current_timestamp", "1");
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private TopicModel topicModel(String id, String name) {
        TopicModel tpc = new TopicModel();
        tpc.setId(id);
        tpc.setName(name);

        return tpc;
    }

    public List<TopicModel> getTopicList() {

        return topic_list;
    }

    public void setTopicList(List<TopicModel> list) {
        this.topic_list = list;
    }

    public class AdapterUpdaterTask extends AsyncTask<Void, Void, Void> {
        List<TopicModel> topicList = new ArrayList<TopicModel>();

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("UPDATE", "1");
            try {
                topicList = getTopicList();
                arrayTemplist = new ArrayList<TopicModel>();
                String searchString = topic.getText().toString().toLowerCase();

                for (int i = 0; i < topicList.size(); i++) {
                    String currentString = topicList.get(i).getName();
                    if (currentString.toLowerCase().contains(searchString)) {
                        arrayTemplist.add(topicList.get(i));
                    }
                }


            } catch (NullPointerException e) {
            }
            Log.i("UPDATE", "2");
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("UPDATE", "3");

            int size = arrayTemplist.size();

            if (size > 0) {
                adapter.clear();
                Log.i("ADAPTER_SIZE", "" + size);
                for (int i = 0; i < size; i++) {
                    adapter.add(arrayTemplist.get(i).getName());
                    Log.i("ADDED", arrayTemplist.get(i).getName());
                }
                Log.i("UPDATE", "4");

                adapter.notifyDataSetChanged();
                topic.showDropDown();

            }
            super.onPostExecute(aVoid);
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

