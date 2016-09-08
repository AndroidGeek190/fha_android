package com.erginus.fithealthy;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.erginus.fithealthy.helper.PrefsHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.net.URL;


public class InputWeightDetailViewActivity extends ActionBarActivity {
    LinearLayout ll_backArrow, ll_uplod;
    private static final int PHOTO_PICKER_ID = 1;
    private static final int CAMERA_PIC_REQUEST = 2500;
    ImageView img;
    PrefsHelper prefsHelper;
    File f;
    TextView edt_input_wt, edt_bmi,edt_bdy_fat, edt_cal_cnsumd,edt_cal_burn,edt_fat_consumd, edt_protein_consumd,edt_body_area,
    edt_measure_diff, edt_sports_perf,edt_dist_perf, edt_time_perf,edt_position_perf, edt_win_loose,edt_trainng_ssn, edt_exercise,
    edt_load_perf, edt_avg_repition, edt_avg_sets, edt_avg_pace, edt_heart_race,edt_avg_watts,edt_cadence, edt_recovery_sess,
    edt_flex_sesson, trckngdate;
    public static final int REQUEST_CODE_SELECT_PHOTO = 1;
    String weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_input_weight_detail_view);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ll_backArrow=(LinearLayout)findViewById(R.id.linear_back);
        ll_uplod=(LinearLayout)findViewById(R.id.ll_upload);
        img=(ImageView)findViewById(R.id.img_uploadpic);
        prefsHelper=new PrefsHelper(this);
        edt_input_wt=(TextView)findViewById(R.id.edttxt_wgtstone);
        edt_bmi=(TextView)findViewById(R.id.edttxt_bmi);
        edt_bdy_fat=(TextView)findViewById(R.id.edttxt_bdyfat);
        edt_cal_cnsumd=(TextView)findViewById(R.id.edttxt_caloconsu);
        edt_cal_burn=(TextView)findViewById(R.id.edttxt_caloburn);
        edt_fat_consumd=(TextView)findViewById(R.id.edttxt_grmsfat);
        edt_protein_consumd=(TextView)findViewById(R.id.edttxt_grmsprotin);
        edt_body_area=(TextView)findViewById(R.id.edttxt_area);
        edt_measure_diff=(TextView)findViewById(R.id.edttxt_msrdiff);
        edt_sports_perf=(TextView)findViewById(R.id.edttxt_sport);
        edt_dist_perf=(TextView)findViewById(R.id.edttxt_distance);
        edt_time_perf=(TextView)findViewById(R.id.edttxt_time);
        edt_position_perf=(TextView)findViewById(R.id.edttxt_pstn);
        edt_win_loose=(TextView)findViewById(R.id.edttxt_winlose);
        edt_trainng_ssn=(TextView)findViewById(R.id.edttxt_trngssn);
        edt_exercise=(TextView)findViewById(R.id.edttxt_exercse_prfmd);
        edt_load_perf=(TextView)findViewById(R.id.edttxt_load_achived);
        edt_avg_repition=(TextView)findViewById(R.id.edttxt_avrg_repettn);
        edt_avg_sets=(TextView)findViewById(R.id.edttxt_avrg_set);
        edt_avg_pace=(TextView)findViewById(R.id.edttxt_avrg_pace);
        edt_heart_race=(TextView)findViewById(R.id.edttxt_hrt_rate);
        edt_avg_watts=(TextView)findViewById(R.id.edttxt_avrg_watts);
        edt_cadence=(TextView)findViewById(R.id.edttxt_avrg_cadence);
        edt_recovery_sess=(TextView)findViewById(R.id.edttxt_rcvry_sessn);
        edt_flex_sesson=(TextView)findViewById(R.id.edttxt_flexblty_sessn);
        trckngdate=(TextView)findViewById(R.id.txtvw_date_trck);
        Picasso.with(this).load(getIntent().getStringExtra("image")).into(img);
        edt_input_wt.setText(getIntent().getStringExtra("weight"));
        edt_bmi.setText(getIntent().getStringExtra("bmi"));
        edt_bdy_fat.setText(getIntent().getStringExtra("bdyFat"));
        edt_cal_cnsumd.setText(getIntent().getStringExtra("calCon"));
        edt_cal_burn.setText(getIntent().getStringExtra("calBurn"));
        edt_fat_consumd.setText(getIntent().getStringExtra("fatCon"));
        edt_protein_consumd.setText(getIntent().getStringExtra("protCon"));
        edt_body_area.setText(getIntent().getStringExtra("bdyArea"));
        edt_measure_diff.setText(getIntent().getStringExtra("measDiff"));
        edt_sports_perf.setText(getIntent().getStringExtra("sports"));
        edt_dist_perf.setText(getIntent().getStringExtra("distanc"));
        edt_time_perf.setText(getIntent().getStringExtra("time"));
        edt_position_perf.setText(getIntent().getStringExtra("postion"));
        edt_win_loose.setText(getIntent().getStringExtra("winLose"));
        edt_trainng_ssn.setText(getIntent().getStringExtra("training"));
        edt_exercise.setText(getIntent().getStringExtra("exercise"));
        edt_load_perf.setText(getIntent().getStringExtra("load"));
        edt_avg_repition.setText(getIntent().getStringExtra("repetition"));
        edt_avg_sets.setText(getIntent().getStringExtra("sets"));
        edt_avg_pace.setText(getIntent().getStringExtra("pace"));
        edt_heart_race.setText(getIntent().getStringExtra("heartRt"));
        edt_avg_watts.setText(getIntent().getStringExtra("watt"));
        edt_cadence.setText(getIntent().getStringExtra("cadence"));
        edt_recovery_sess.setText(getIntent().getStringExtra("recvry"));
        edt_flex_sesson.setText(getIntent().getStringExtra("flexblty"));
       trckngdate.setText(getIntent().getStringExtra("date"));
        ll_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           finish();
            }
        });
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
        super.onBackPressed();
        finish();

    }

}
