package com.erginus.fithealthy.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oovoo.sdk.api.ooVooClient;
import com.erginus.fithealthy.R;

public class FlashScreen extends Fragment {
	private static final String TAG = "FlashScreen";

	public FlashScreen(){
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.flash_layout, container, false);
		
		TextView errorTextView = (TextView)view.findViewById(R.id.error_label);
		errorTextView.setVisibility(View.INVISIBLE);
		
		if (!ooVooClient.isDeviceSupported()) {
			errorTextView.setVisibility(View.VISIBLE);
			errorTextView.setText(getActivity().getResources().getString(R.string.device_is_not_supported));
		}
		
		return view;
	}
	
	public void onBackPressed()
	{
		try
		{
			this.getActivity().finish();
		}
		catch(Exception err){
			Log.e(TAG, "onBackPressed " + err);
		}
	}
}
