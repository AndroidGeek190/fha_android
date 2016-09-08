package com.erginus.fithealthy.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.helper.GlobalPrefrences;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.erginus.fithealthy.ui.SampleActivity;
import com.oovoo.core.Utils.LogSdk;
import com.oovoo.sdk.api.ui.VideoPanel;
import com.oovoo.sdk.interfaces.Effect;
import com.oovoo.sdk.interfaces.VideoController;
import com.oovoo.sdk.interfaces.VideoDevice;
import com.erginus.fithealthy.R;
import com.erginus.fithealthy.ui.VideoPanelPreviewRect;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AVChatLoginFragment extends BaseFragment {
	private static final int CONFERENCE_ID_LIMIT = 200;
	private static final int DISPLAY_NAME_LIMIT = 100;
	private static final String TAG = "AVChatLoginFragment" ;
	private EditText sessionIdEditText	= null;
	private EditText displayNameEditText	= null;
	private MenuItem settingsMenuItem = null;
	private VideoPanelPreviewRect previewRect = null;
    PrefsHelper prefsHelper;
    long secnds;
    String showRateDialog, rating;
    ProgressDialog pDialog;
    GlobalPrefrences globalPrefrences;


    public AVChatLoginFragment(){}
	
	public static final AVChatLoginFragment newInstance(MenuItem settingsMenuItem) {
		AVChatLoginFragment instance = new AVChatLoginFragment();
	    instance.setSettingsMenuItem(settingsMenuItem);
	    return instance;
	}
	
	public void setSettingsMenuItem(MenuItem settingsMenuItem) {
		this.settingsMenuItem = settingsMenuItem;
	}
	
	@Override
    public void onResume() {
	    super.onResume();
		if(settingsMenuItem  != null)
	    	settingsMenuItem.setVisible(true);
    }
	
	@Override
    public void onPause() {
        super.onPause();
		if(settingsMenuItem  != null)
        	settingsMenuItem.setVisible(false);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    if (app().isTablet()) {			
	    	updatePreviewLayout(newConfig);
        }	    
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.avchat_login_fragment, container, false);

		VideoPanel panel = (VideoPanel) view.findViewById(R.id.preview_view);

		previewRect = (VideoPanelPreviewRect) view.findViewById(R.id.preview_rect);
        SampleActivity.timer.setVisibility(View.VISIBLE);
        prefsHelper=new PrefsHelper(getActivity());
        globalPrefrences=new GlobalPrefrences(getActivity());
        showRateDialog=prefsHelper.getShowRatingFromPreference();
        secnds= Long.parseLong(prefsHelper.getSecondsFromPref())*1000;
        new CountDownTimer(secnds, 1000) {

            public void onTick(long millisUntilFinished) {
                long durationSeconds=millisUntilFinished/1000;
                SampleActivity.timer.setText(String.format("%02d:%02d", (durationSeconds % 3600) / 60, (durationSeconds % 60)));
                   }

            public void onFinish() {
                SampleActivity.timer.setText("Time Up!");
                app().onEndOPfCall();
                if(showRateDialog.equals("1")) {

                   dialog();
                }
                else
                {
                    getActivity().finish();
                }
            }
        }.start();
        if (app().isTablet()) {
			Configuration config = getResources().getConfiguration();
			updatePreviewLayout(config);
		}

		ArrayList<VideoDevice> cameras = app().getVideoCameras();
	    for (VideoDevice camera : cameras) {
	    	if (camera.toString().equals("FRONT") && !app().getActiveCamera().getID().equalsIgnoreCase(camera.getID())) {
	    		app().switchCamera(camera);
	    		break;
	    	}
		}




	    ArrayList<Effect> filters = app().getVideoFilters();

		for(Effect effect : filters){
			if(effect.getName().equalsIgnoreCase("original")){
				app().changeVideoEffect(effect);
				break ;
			}
		}


		app().changeResolution(VideoController.ResolutionLevel.ResolutionLevelMed);

	    app().openPreview();

		String lastSessionId = prefsHelper.getConferenceIdFromPrefrence();
		String lastDisplayName =prefsHelper.getUserFNameFromPreference()+" "+prefsHelper.getUserLNameFromPreference()+" "+"("+prefsHelper.getUserIdFromPrefrence()+")";

		sessionIdEditText = (EditText) view.findViewById(R.id.session_field);

		if (lastSessionId != null) {
			sessionIdEditText.setText(lastSessionId);
		}

		displayNameEditText = (EditText) view.findViewById(R.id.displayname_field);

		if (lastDisplayName != null) {
			displayNameEditText.setText(lastDisplayName);
		}

		/****
		 * Let's bind the view for camera preview output
		 */
		app().bindVideoPanel(null, panel);

		Button join = (Button) view.findViewById(R.id.join_button);
		join.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (app().isOnline()) {
					join();
//				} else {
//					Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
//				}
			}
		});

		return view;
	}
	
	private void updatePreviewLayout(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			int width = app().getDisplaySize().x;
	    	int padding = (width - ((int)(app().getDisplaySize().y * 0.75f * (4.0/3.0))))/2;
        	previewRect.setPadding(padding, 0, padding, 0);
		} else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
	    	previewRect.setPadding(0, 0, 0, 0);
	    }
	}

	private void join() 
	{
		String sessionId = sessionIdEditText.getText().toString();
		String displayName = displayNameEditText.getText().toString();
		if (sessionId.isEmpty()) {
			Toast.makeText(getActivity(), R.string.enter_conference_id, Toast.LENGTH_LONG).show();
			
			return;
		}
		
		if (!sessionId.matches("^([a-zA-Z0-9-%])+$") || sessionId.length() > CONFERENCE_ID_LIMIT) {
			showErrorMessageBox(getString(R.string.join_session), getString(R.string.wrong_conference_id));
			
			return;
		}
		
		if (displayName.isEmpty()) {
			Toast.makeText(getActivity(), R.string.enter_conference_display_name, Toast.LENGTH_LONG).show();
			
			return;
		}
		
		if (displayName.length() > DISPLAY_NAME_LIMIT) {
			showErrorMessageBox(getString(R.string.join_session), getString(R.string.display_name_too_long));
			
			return;
		}

		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(displayNameEditText.getWindowToken(), 0);
			
		if(!app().isOnline()){
			showErrorMessageBox("Network Error", getString(R.string.no_internet));
			return;
		}
			
		app().join(sessionId, displayName);
	}


	protected void finalize() throws Throwable {
		LogSdk.d(TAG, "ooVooCamera -> VideoPanel -> finalize AVChatLoginFragment ->");
		super.finalize();
	}
	
	public void showErrorMessageBox(String title,String msg)
	{
		try {		
				AlertDialog.Builder popupBuilder = new AlertDialog.Builder(getActivity());
				TextView myMsg = new TextView(getActivity());
				myMsg.setText(msg);
				myMsg.setGravity(Gravity.CENTER);
				popupBuilder.setTitle(title);
				popupBuilder.setPositiveButton("OK", null);
				popupBuilder.setView(myMsg);

				popupBuilder.show();
		} catch( Exception e) {
		}
	}
    private void dialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rate_caoch);
        final RatingBar rateCoch=(RatingBar)dialog.findViewById(R.id.ratingBar);
        rateCoch.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean fromUser) {
                int ratingValue= (int) rate;
                rating= String.valueOf(ratingValue);

            }
        });
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        Button cancel = (Button) dialog.findViewById(R.id.btn_cance);
        TextView ratetxt=(TextView)dialog.findViewById(R.id.text_rate);
        ratetxt.setText(globalPrefrences.getRateMessage());
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                rateCoach();


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().finish();

            }
        });
        dialog.show();
    }
    public void rateCoach()
    {
        try {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Answer " + MapAppConstants.API + "/rate");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/rate", new Response.Listener<String>() {
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
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
                    params.put("user_security_hash",prefsHelper.getSecHashFromPrefrence());
                    params.put("bookings_id",prefsHelper.getBookingIdFromPrefrences());
                    params.put("rating_value",rating);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(500000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	public void onBackPressed() {
        Toast.makeText(getActivity(), "Please Use Disconnect Button", Toast.LENGTH_SHORT).show();
		//app().releaseAVChat();

    }

}
