package com.erginus.fithealthy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erginus.fithealthy.helper.GlobalPrefrences;


public class TermsAndConditionActivity extends Activity {
  CheckBox chk_id;
    TextView terms;
    Button accpt;
    GlobalPrefrences globalPrefrences;
    private boolean isChecked;
    LinearLayout linearLayout;
    private SharedPreferences mPrefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terms_and_condition);
      //  chk_id=(CheckBox)findViewById(R.id.checkBox);
        terms=(TextView)findViewById(R.id.textView4);
        globalPrefrences=new GlobalPrefrences(this);
        String term= globalPrefrences.getTermMessage();
        linearLayout=(LinearLayout)findViewById(R.id.linear_back);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        terms.setText(term);
        accpt=(Button)findViewById(R.id.accept);
        accpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*isChecked = chk_id.isChecked();
                if (isChecked) {
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putBoolean("is_checked", isChecked);
                    mEditor.commit();*/
                    Intent i = new Intent(TermsAndConditionActivity.this,
                            SurveyActivity.class);
                    startActivity(i);
                    finish();
               /* } else {
                    Toast.makeText(TermsAndConditionActivity.this,
                            "Please accept the terms and conditions",
                            Toast.LENGTH_LONG).show();
                }*/

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
}
