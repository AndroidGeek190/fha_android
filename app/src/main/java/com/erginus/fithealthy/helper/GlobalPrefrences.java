package com.erginus.fithealthy.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by paramjeet on 21/8/15.
 */
public class GlobalPrefrences {
    private Context context;
    public static SharedPreferences pref;

    public GlobalPrefrences(Context context) {
        this.context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context getContext() {
        return context;
    }

    public SharedPreferences getPreferences() {
        return pref;
    }

    public void storeTokenMessageToPrefrence(String tokn)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("token_message", tokn);
        edit.commit();

    }
    public void storeTermMessageToPrefrence(String term)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("terms_message", term);
        edit.commit();

    }
    public void storeSurveyMessageToPrefrence(String survey)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("survey_message", survey);
        edit.commit();

    }
    public void storeTermsAcceptedToPrefrence(String trms)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("terms_accepted", trms);
        edit.commit();

    }
    public void storeFreeTokenPerMonth(String hash)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("free_tokens_per_month", hash);
        edit.commit();

    }
    public void storeSocialMediaPlatform(String hash)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("social_media_platform", hash);
        edit.commit();

    }
    public void storeRateMessage(String msg)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("rate_message", msg);
        edit.commit();

    }
    public void storeGMTMessageToPrefrence(String msg)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("gmt_message", msg);
        edit.commit();

    }
    public void storeOnetoOneCoaching(String coachng)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("one_to_one_coaching", coachng);
        edit.commit();

    }
    public void storeOneToOneChat(String chat)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("one_to_one_chat", chat);
        edit.commit();

    }
    public void storeGroupCoaching(String grup)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("group_coaching", grup);
        edit.commit();

    }
    public void storeBroadbndMsgToPref(String msg)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("broadband_message", msg);
        edit.commit();

    }
    public void storeSingleTokenOne(String stokn)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("single_token_one", stokn);
        edit.commit();

    }
    public void storeSingleTokenOnePrice(String price)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("single_token_one_price", price);
        edit.commit();

    }

    public void storeSingleTokenTwo(String tokn)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("single_token_two", tokn);
        edit.commit();

    }
    public void storeSingleTokenTwoPrice(String price)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("single_token_two_price", price);
        edit.commit();

    }
    public void storeSingleTokenThree(String tokn)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("single_token_three", tokn);
        edit.commit();

    }
    public void storeSingleTokenThreePrice(String price)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("single_token_three_price", price);
        edit.commit();

    }
    public void storeMonthlyTokenOne(String hash)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("monthly_token_one", hash);
        edit.commit();

    }
    public void storeMonthlyTokenOnePrice(String hash)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("monthly_token_one_price", hash);
        edit.commit();

    }

    public void storeMonthlyTokenTwo(String hash)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("monthly_token_two", hash);
        edit.commit();

    }
    public void storeMonthlyTokenTwoPrice(String hash)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("monthly_token_two_price", hash);
        edit.commit();

    }
    public void storeMonthlyTokenThree(String hash)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("monthly_token_three", hash);
        edit.commit();

    }
    public void storeMonthlyTokenThreePrice(String hash)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("monthly_token_three_price", hash);
        edit.commit();

    }
    public void storeGroupSlug(String hash)
    {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString("group_slug", hash);
        edit.commit();

    }
    public String getGroupSlug() {
        return getPreferences().getString("group_slug", "");
    }

    public String getTokenMessage() {
        return getPreferences().getString("token_message", "");
    }
    public String getGMTMessage() {
        return getPreferences().getString("gmt_message", "");
    }
    public String getBroadbandMessage() {
        return getPreferences().getString("broadband_message", "");
    }
    public String getFreeTokensPerMonth() {
        return getPreferences().getString("free_tokens_per_month", "");
    }
    public String getOneToOneChat() {
        return getPreferences().getString("one_to_one_chat", "");
    }
    public String getGroupCoaching() {
        return getPreferences().getString("group_coaching", "");
    }
    public String getOneToOneCoaching() {
        return getPreferences().getString("one_to_one_coaching", "");
    }
    public String getSingleTokenOne() {
        return getPreferences().getString("single_token_one", "");
    }
    public String getSingleTokenOnePrice() {
        return getPreferences().getString("single_token_one_price", "");
    }
    public String getSingleTokenTwo() {
        return getPreferences().getString("single_token_two", "");
    }
    public String getSingleTokenTwoPrice() {
        return getPreferences().getString("single_token_two_price", "");
    }
    public String getSingleTokenThree() {
        return getPreferences().getString("single_token_three", "");
    }
    public String getSingleTokenThreePrice() {
        return getPreferences().getString("single_token_three_price", "");
    }
    public String getMonthlyTokenOne() {
        return getPreferences().getString("monthly_token_one", "");
    }
    public String getMonthlyTokenOnePrice() {
        return getPreferences().getString("monthly_token_one_price", "");
    }
    public String getMonthlyTokenTwo() {
        return getPreferences().getString("monthly_token_two", "");
    }
    public String getMonthlyTokenTwoPrice() {
        return getPreferences().getString("monthly_token_two_price", "");
    }
    public String getMonthlyTokenThree() {
        return getPreferences().getString("monthly_token_three", "");
    }
    public String getMonthlyTokenThreePrice() {
        return getPreferences().getString("monthly_token_three_price", "");
    }
    public String getTermMessage() {
        return getPreferences().getString("terms_message", "");
    }
    public String getSurveyMessage() {
        return getPreferences().getString("survey_message", "");
    }

    public String getTermsAcceptd() {
        return getPreferences().getString("terms_accepted", "");
    }

    public String getRateMessage() {
        return getPreferences().getString("rate_message", "");
    }

    public String getSocialMediaPlatform() {
        return getPreferences().getString("social_media_platform", "");
    }
}
