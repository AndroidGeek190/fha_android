package com.erginus.fithealthy.ui;

 
 
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.erginus.fithealthy.fragments.MyBookingFragment;
import com.erginus.fithealthy.helper.PrefsHelper;

import com.oovoo.core.Utils.LogSdk;
import com.oovoo.sdk.api.ooVooClient;
import com.erginus.fithealthy.R;
import com.erginus.fithealthy.app.ooVooSdkSampleShowApp;
import com.erginus.fithealthy.app.ooVooSdkSampleShowApp.Operation;
import com.erginus.fithealthy.app.ooVooSdkSampleShowApp.OperationChangeListener;
import com.erginus.fithealthy.ui.fragments.AVChatLoginFragment;
import com.erginus.fithealthy.ui.fragments.AVChatSessionFragment;
import com.erginus.fithealthy.ui.fragments.BaseFragment;
import com.erginus.fithealthy.ui.fragments.FlashScreen;
import com.erginus.fithealthy.ui.fragments.InformationFragment;

import com.erginus.fithealthy.ui.fragments.ReautorizeFragment;
import com.erginus.fithealthy.ui.fragments.SettingsFragment;
import com.erginus.fithealthy.ui.fragments.WaitingFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SampleActivity extends Activity implements OperationChangeListener{

 
	private static final String TAG	           	= "HostActivity";
	private BaseFragment	      	current_fragment	= null;
	private ooVooSdkSampleShowApp	application	   = null;
	private MenuItem mSettingsMenuItem = null;
	private MenuItem mInformationMenuItem = null;
	private MenuItem mSignalStrengthMenuItem = null;
	private boolean					mIsAlive = false;
	private boolean					mNeedShowFragment = false;
    PrefsHelper prefsHelper;
     public static SampleActivity sm;

    public static TextView timer= null;

    String showRateDialog;
    ProgressDialog pDialog;
    public static SampleActivity getInstance() {
        if(sm == null)
            sm= new SampleActivity();
        return sm;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        prefsHelper=new PrefsHelper(this);
		application = (ooVooSdkSampleShowApp) getApplication();
        showRateDialog=prefsHelper.getShowRatingFromPreference();
        Log.d("ycrseeeeeeeeeeeeeeeeeee",showRateDialog);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.host_activity);


        timer=(TextView)findViewById(R.id.text_time);

		application.addOperationChangeListener(this);
		if (savedInstanceState == null) {
			Fragment newFragment = new FlashScreen();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(R.id.host_activity, newFragment).commit();
		}

		if (!ooVooClient.isDeviceSupported()) {
			return;
		}

		try {
 
			application.onMainActivityCreated();
		} catch( Exception e) {
			Log.e(TAG, "onCreate exception: ", e);
		}
	}

	public void onResume() {
		super.onResume();

		mIsAlive = true;
 

		if(mNeedShowFragment){
			showFragment(current_fragment);
			mNeedShowFragment = false;
		}
	}


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		Object tag = v.getTag();
		if (tag != null && tag instanceof MenuList) {
			MenuList list = (MenuList) tag;
			list.fill(v, menu);
		}
	}

	public void logout(){
		if(current_fragment != null) {
			this.removeFragment(current_fragment);
			current_fragment = null ;
		}
		application.logout();

	}


	/*@Override
	public boolean onCreateOptionsMenu( Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.main_menu_1, menu);

		mSettingsMenuItem = menu.findItem(R.id.menu_settings);
		mSettingsMenuItem.setVisible(false);

		mInformationMenuItem = menu.findItem(R.id.menu_information);
		mInformationMenuItem.setVisible(false);

		mSignalStrengthMenuItem = menu.findItem(R.id.menu_signal_strenth);

		SignalBar signalBar = new SignalBar(this);

		mSignalStrengthMenuItem.setActionView(signalBar);
		mSignalStrengthMenuItem.setVisible(false);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item)
	{
		if( item == null)
			return false;

		switch( item.getItemId())
		{
			case R.id.menu_settings:
				SettingsFragment settings  = new SettingsFragment();
				settings.setBackFragment(current_fragment);

				mSettingsMenuItem.setVisible(false);

				addFragment(settings);

				current_fragment = settings;
			return true;

			case R.id.menu_information:
				InformationFragment information  = new InformationFragment();
				information.setBackFragment(current_fragment);

				mSignalStrengthMenuItem.setVisible(false);
				mInformationMenuItem.setVisible(false);

				addFragment(information);

				current_fragment = information;
			return true;
		}

		return super.onOptionsItemSelected( item);
	}
*/
	@Override
	public void onOperationChange(Operation state) {
		try {
			Fragment prev_fragment = current_fragment ;
			switch (state) {
			case Error:
			{
				switch (state.forOperation()) {
				case Authorized:
					current_fragment = ReautorizeFragment.newInstance(mSettingsMenuItem, state.getDescription());
					break;
				case LoggedIn:
					current_fragment = AVChatLoginFragment.newInstance(mSettingsMenuItem);
					break;
				case AVChatJoined:
					current_fragment = AVChatLoginFragment.newInstance(mSettingsMenuItem);
					break;
				default:
					return;
				}
			}
				break;
			case Processing:
				current_fragment = WaitingFragment.newInstance(state.getDescription());
				break;
			case AVChatJoined:
            	current_fragment = AVChatSessionFragment.newInstance(mSignalStrengthMenuItem, mInformationMenuItem);

				break;
                case LoggedIn:
                    current_fragment = AVChatLoginFragment.newInstance(mSettingsMenuItem);
                    break;
			/*case Authorized:
                current_fragment = LoginFragment.newInstance(state.getDescription());
				break;*/
			case AVChatDisconnected:

			default:
				return;
			}

			showFragment(current_fragment);
			//removeOldFragment(prev_fragment);
			prev_fragment = null ;
			System.gc();
			Runtime.getRuntime().gc();

		} catch (Exception err) {
			err.printStackTrace();
		}
	}


	private void showFragment(Fragment newFragment) {
		if(!mIsAlive){
			mNeedShowFragment = true;
			return;
		}

		try {
			if (newFragment != null) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.host_activity, newFragment);
				transaction.addToBackStack(newFragment.getClass().getSimpleName());
				transaction.commit();
			}
		}
		catch(Exception err){
			LogSdk.e(TAG, "showFragment " + err);
		}
	}


	public void removeOldFragment(Fragment fragment) {
		try {
			if(fragment != null) {
				FragmentTransaction trans = getFragmentManager().beginTransaction();
				trans.remove(fragment);
				trans.commit();
				System.gc();
				Runtime.getRuntime().gc();
				LogSdk.d(TAG, "ooVooCamera -> VideoPanel -> finalize removeOldFragment " + fragment);
			}
		}
		catch(Exception err){
			LogSdk.e(TAG, "removeOldFragment " + err);
		}
	}

	private void addFragment(Fragment newFragment) {

		try {
			if (newFragment != null) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.add(R.id.host_activity, newFragment);
				transaction.show(newFragment);
				transaction.hide(current_fragment);
				transaction.commit();
			}
		}
		catch(Exception err){
			LogSdk.e(TAG, "addFragment " + err);
		}
	}

	private void removeFragment(Fragment fragment) {

		try {
			if (fragment != null) {
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.remove(current_fragment);
				transaction.show(fragment);
				transaction.commit();
			}
		}
		catch(Exception err){
			LogSdk.e(TAG, "removeFragment " + err);
		}
	}


    public static interface MenuList {
		public void fill(View view, ContextMenu menu);
	}
    @Override
    public void onPause() {
        super.onPause();
        mIsAlive = false;
        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = null;
    }
	@Override
	public void onBackPressed() {
		/*try {
			if (current_fragment != null) {

				if((current_fragment instanceof WaitingFragment) || !current_fragment.onBackPressed()){

                    return;
				}

				BaseFragment fragment = current_fragment.getBackFragment();
				if (fragment != null) {

					if (current_fragment instanceof InformationFragment) {
						mSignalStrengthMenuItem.setVisible(true);
						mInformationMenuItem.setVisible(true);
						removeFragment(fragment);
					} else if (current_fragment instanceof SettingsFragment) {
						mSettingsMenuItem.setVisible(true);
						removeFragment(fragment);
					} else {

						showFragment(fragment);
					//	removeOldFragment(current_fragment);
						System.gc();
						Runtime.getRuntime().gc();
					}
					current_fragment = fragment;

					return ;
				}

			}
		} catch (Exception err) {
			Log.e(TAG, "");
		}*/
        Toast.makeText(SampleActivity.this, "Please Use Disconnect Button", Toast.LENGTH_SHORT).show();

	}

}
