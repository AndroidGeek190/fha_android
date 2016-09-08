package com.erginus.fithealthy;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.MultipartRequest;
import com.erginus.fithealthy.commons.VolleySingleton;
import com.erginus.fithealthy.fragments.TrackingProgressFragment;
import com.erginus.fithealthy.helper.PrefsHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;


public class InputWeightDetailActivity extends ActionBarActivity {
    LinearLayout ll_backArrow, ll_uplod;
    private static final int PHOTO_PICKER_ID = 1;
    private static final int CAMERA_PIC_REQUEST = 2500;
    public static ImageView img;
    PrefsHelper prefsHelper;
    File f;
    EditText edt_input_wt, edt_bmi,edt_bdy_fat, edt_cal_cnsumd,edt_cal_burn,edt_fat_consumd, edt_protein_consumd,edt_body_area,
    edt_measure_diff, edt_sports_perf,edt_dist_perf, edt_time_perf,edt_position_perf, edt_win_loose,edt_trainng_ssn, edt_exercise,
    edt_load_perf, edt_avg_repition, edt_avg_sets, edt_avg_pace, edt_heart_race,edt_avg_watts,edt_cadence, edt_recovery_sess,
    edt_flex_sesson;
    Button btn_save, bt_camera,bt_gallery;
    public static final int REQUEST_CODE_SELECT_PHOTO = 1;
    String weight, filename, image;
    ProgressDialog pDialog;
    String filePath;
    Bitmap scaledBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_input_weight_detail);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ll_backArrow=(LinearLayout)findViewById(R.id.linear_back);
        ll_uplod=(LinearLayout)findViewById(R.id.ll_upload);
        img=(ImageView)findViewById(R.id.img_uploadpic);
        btn_save=(Button)findViewById(R.id.bttn_save);
        prefsHelper=new PrefsHelper(this);
        edt_input_wt=(EditText)findViewById(R.id.edttxt_wgtstone);
        edt_bmi=(EditText)findViewById(R.id.edttxt_bmi);
        edt_bdy_fat=(EditText)findViewById(R.id.edttxt_bdyfat);
        edt_cal_cnsumd=(EditText)findViewById(R.id.edttxt_caloconsu);
        edt_cal_burn=(EditText)findViewById(R.id.edttxt_caloburn);
        edt_fat_consumd=(EditText)findViewById(R.id.edttxt_grmsfat);
        edt_protein_consumd=(EditText)findViewById(R.id.edttxt_grmsprotin);
        edt_body_area=(EditText)findViewById(R.id.edttxt_area);
        edt_measure_diff=(EditText)findViewById(R.id.edttxt_msrdiff);
        edt_sports_perf=(EditText)findViewById(R.id.edttxt_sport);
        edt_dist_perf=(EditText)findViewById(R.id.edttxt_distance);
        edt_time_perf=(EditText)findViewById(R.id.edttxt_time);
        edt_position_perf=(EditText)findViewById(R.id.edttxt_pstn);
        edt_win_loose=(EditText)findViewById(R.id.edttxt_winlose);
        edt_trainng_ssn=(EditText)findViewById(R.id.edttxt_trngssn);
        edt_exercise=(EditText)findViewById(R.id.edttxt_exercse_prfmd);
        edt_load_perf=(EditText)findViewById(R.id.edttxt_load_achived);
        edt_avg_repition=(EditText)findViewById(R.id.edttxt_avrg_repettn);
        edt_avg_sets=(EditText)findViewById(R.id.edttxt_avrg_set);
        edt_avg_pace=(EditText)findViewById(R.id.edttxt_avrg_pace);
        edt_heart_race=(EditText)findViewById(R.id.edttxt_hrt_rate);
        edt_avg_watts=(EditText)findViewById(R.id.edttxt_avrg_watts);
        edt_cadence=(EditText)findViewById(R.id.edttxt_avrg_cadence);
        edt_recovery_sess=(EditText)findViewById(R.id.edttxt_rcvry_sessn);
        edt_flex_sesson=(EditText)findViewById(R.id.edttxt_flexblty_sessn);

        ll_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           finish();
            }
        });
        ll_uplod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(InputWeightDetailActivity.this);
                dialog.getWindow();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_popup_camera_gallry);
                bt_camera = (Button) dialog.findViewById(R.id.bt_camera);
                bt_gallery = (Button) dialog.findViewById(R.id.bt_gallery);

                bt_gallery.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        i.setType("image/*");
                        startActivityForResult(i, PHOTO_PICKER_ID);
                    }
                });
                bt_camera.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        Intent cameraIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);


                        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                    }
                });
                dialog.show();
            }

        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weight=edt_input_wt.getText().toString();
                if(!weight.equalsIgnoreCase("")){
                    inputWeightDetail();
                }
                else{
                    Toast.makeText(InputWeightDetailActivity.this, "Please enter the value of weight", Toast.LENGTH_SHORT).show();

                }
                    }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case CAMERA_PIC_REQUEST:

                    if (requestCode == CAMERA_PIC_REQUEST
                            && resultCode == Activity.RESULT_OK && null!=data) {
                        Uri selectedImage = data.getData();
                        compressImage(getPath(selectedImage));
                        img.setImageBitmap(scaledBitmap);
                        f = new File(filename);

                    }

                    break;
                case PHOTO_PICKER_ID:
                    if (requestCode == PHOTO_PICKER_ID
                            && resultCode == Activity.RESULT_OK && null != data) {
                        Uri selectedImage = data.getData();
                        compressImage(getPath(selectedImage));
                        img.setImageBitmap(scaledBitmap);
                        f= new File(filename);

                    }
                    break;
            }
        } catch (Exception e) {
            Log.d("krvrrusbviuritiribtr", e.getMessage());
        }
    }


    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null)
            return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public void inputWeightDetail()
    {
        try {
            pDialog = new ProgressDialog(InputWeightDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Tracking " + MapAppConstants.API + "/tracking");
            HashMap params = new HashMap<String, String>();
            params.put("user_id", Integer.parseInt(prefsHelper.getUserIdFromPrefrence())+"");
            params.put("user_security_hash", prefsHelper.getSecHashFromPrefrence());
            params.put("weight",weight);
            params.put("bmi", edt_bmi.getText().toString());
            params.put("body_fat", edt_bdy_fat.getText().toString());
            params.put("calories_consumed",edt_cal_cnsumd.getText().toString());
            params.put("calories_burned", edt_cal_burn.getText().toString());
            params.put("fat_consumed", edt_fat_consumd.getText().toString());
            params.put("protein_consumed", edt_protein_consumd.getText().toString());
            params.put("body_area", edt_body_area.getText().toString());
            params.put("measurement_difference", edt_measure_diff.getText().toString());
            params.put("sports_performance", edt_sports_perf.getText().toString());
            params.put("distance_performance", edt_dist_perf.getText().toString());
            params.put("time_performance", edt_time_perf.getText().toString());
            params.put("position_performance", edt_position_perf.getText().toString());
            params.put("win_lose_performance", edt_win_loose.getText().toString());
            params.put("training_sessions_performance", edt_trainng_ssn.getText().toString());
            params.put("exercise_performance", edt_exercise.getText().toString());
            params.put("load_performance", edt_load_perf.getText().toString());
            params.put("average_repetitions_performance", edt_avg_repition.getText().toString());
            params.put("average_sets_performance", edt_avg_sets.getText().toString());
            params.put("average_pace_performance", edt_avg_pace.getText().toString());
            params.put("average_heart_rate_performance", edt_heart_race.getText().toString());
            params.put("average_watts_performance", edt_avg_watts.getText().toString());
            params.put("average_cadence", edt_cadence.getText().toString());
            params.put("recovery_sessions", edt_recovery_sess.getText().toString());
            params.put("flexibility_sessions", edt_flex_sesson.getText().toString());


            MultipartRequest sr = new MultipartRequest(MapAppConstants.API +"/tracking", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Log.d("file", f + "");
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(InputWeightDetailActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {

                                }


                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent=new Intent(InputWeightDetailActivity.this, GoalConsultationRoomActivity.class);
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
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    ;
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(InputWeightDetailActivity.this, "Timeout Error",
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

            }, f, params);
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(InputWeightDetailActivity.this).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = null;
    }
    @Override
    public void onResume() {
        super.onResume();


    }
    public String compressImage(String imageUri) {

        filePath = getRealPathFromURI(imageUri);


        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //   overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
