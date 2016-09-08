package com.erginus.fithealthy.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;





public class PrefsHelper {
    public static final String KEY_PREFS_USER_INFO = "user_info";
	private Context context;
	public static SharedPreferences preferences;

	public PrefsHelper(Context context) {
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public Context getContext() {
		return context;
	}

	public SharedPreferences getPreferences() {
		return preferences;
	}

	public void storeKey0authTokenToPreference(String token) {
        Editor edit = getPreferences().edit();
        edit.putString("oauth_token", token);
        edit.commit();

    }
    public void storeKey0authSecretToPreference(String secret) {
        Editor edit = getPreferences().edit();
        edit.putString("oauth_token_secret", secret);
        edit.commit();
    }

    public void storeTwitterLoginoPreference(Boolean t) {
        Editor edit = getPreferences().edit();
        edit.putBoolean("isTwitterLogedIn", t);
        edit.commit();
    }

    public void storeUserNameToPreference(String name) {
        Editor edit = getPreferences().edit();
        edit.putString("user_login", name);
        edit.commit();
    }


    public void storeUserImageBitmapToPreference(String path)
    {
        Editor edit = getPreferences().edit();
        edit.putString("user_profile_image_url", path);
        edit.commit();

    }


    public void storeHashToPrefrences(String hash)
    {
        Editor edit = getPreferences().edit();
        edit.putString("user_security_hash", hash);
        edit.commit();

    }
    public void storeSecondsToPrefrences(String hash)
    {
        Editor edit = getPreferences().edit();
        edit.putString("seconds_remaining", hash);
        edit.commit();

    }

    public void storeShowRatingToPrefrences(String rate)
    {
        Editor edit = getPreferences().edit();
        edit.putString("show_rating_dialog", rate);
        edit.commit();

    }
    public void storeConferenceIdToPrefrences(String cid)
    {
        Editor edit = getPreferences().edit();
        edit.putString("conference_id", cid);
        edit.commit();

    }
    public void storeBookingIdToPrefrences(String id)
    {
        Editor edit = getPreferences().edit();
        edit.putString("bookings_id", id);
        edit.commit();

    }


    public void storeGenderToPreference(String gender)
    {
        Editor edit = getPreferences().edit();
        edit.putString("user_gender", gender);
        edit.commit();

    }
    public void storePhoneToPreference(String phn)
    {
        Editor edit = getPreferences().edit();
        edit.putString("user_primary_contact", phn);
        edit.commit();

    }

    public void storeUserIdToPreference(String usrId)
    {
        Editor edit = getPreferences().edit();
        edit.putString("user_id",usrId);
        edit.commit();
    }


    public void storeRedeemToPreference(String avail)
    {
        Editor edit = getPreferences().edit();
        edit.putString("user_free_tokens_available", avail);
        edit.commit();
    }
    public void storeRatingToPreference(String rate)
    {
        Editor edit = getPreferences().edit();
        edit.putString("user_rating_average",rate);
        edit.commit();

    }

    public  void storeUserFNameToPrefrence(String coach)
    {
        Editor edit=getPreferences().edit();
        edit.putString("user_first_name", coach);
        edit.commit();
    }
    public  void storeUserLNameToPrefrence(String coach)
    {
        Editor edit=getPreferences().edit();
        edit.putString("user_last_name", coach);
        edit.commit();
    }


    public  void storeCoachDescToPrefrence(String coachDesc)
    {
        Editor edit=getPreferences().edit();
        edit.putString("user_description", coachDesc);
        edit.commit();
    }
    public String getUsersIdFromPreference() {
        return getPreferences().getString("users_id", "");
    }


    public String getUserNameFromPreference() {
        return getPreferences().getString("user_login", "");
    }
    public String getUserFNameFromPreference() {
        return getPreferences().getString("user_first_name", "");
    }
    public String getUserLNameFromPreference() {
        return getPreferences().getString("user_last_name", "");
    }

    public String getGenderFromPreference() {
        return getPreferences().getString("user_gender", "");
    }

    public void storeEmailToPreference(String email) {
		Editor edit = getPreferences().edit();
		edit.putString("user_email", email);
		edit.commit();
	}
	public String getEmailFromPreference() {
		return getPreferences().getString("user_email", "");
	}
    public void storeTokenToPreference(String token) {
        Editor edit = getPreferences().edit();
        edit.putString("user_token_count", token);
        edit.commit();
    }
    public String getTokenFromPreference() {
        return getPreferences().getString("user_token_count", "");
    }
    public String getShowRatingFromPreference() {
        return getPreferences().getString("show_rating_dialog", "");
    }


    public String getUserImageFromPreference() {
        return getPreferences().getString("user_profile_image_url", "");
    }

    public String getKeyTokenFromPrefrence() {
        return getPreferences().getString("oauth_token", "");
    }

    public String getUserIdFromPrefrence() {
        return getPreferences().getString("user_id", "");
    }
    public String getSecHashFromPrefrence() {
        return getPreferences().getString("user_security_hash", "");
    }
    public String getConferenceIdFromPrefrence() {
        return getPreferences().getString("conference_id", "");
    }

    public String getKeySecretFromPrefrence() {
        return getPreferences().getString("oauth_token_secret", "");
    }
    public Boolean getTwitterLoginFromPreference() {
        return getPreferences().getBoolean("isTwitterLogedIn", false);
    }


    public String getSecondsFromPref() {
        return getPreferences().getString("seconds_remaining", "");
    }
    public String getRedeemFromPref() {
        return getPreferences().getString("user_free_tokens_available", "");
    }
    public String getPhoneFromPref() {
        return getPreferences().getString("user_primary_contact", "");
    }
    public String getUserDescFromPref() {
        return getPreferences().getString("user_description", "");
    }
    public String getRateFromPreference() {
        return getPreferences().getString("user_rating_average", "");
    }

    public String getBookingIdFromPrefrences() {
        return getPreferences().getString("bookings_id", "");
    }

}
