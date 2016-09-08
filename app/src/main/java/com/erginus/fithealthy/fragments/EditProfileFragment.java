package com.erginus.fithealthy.fragments;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.erginus.fithealthy.BuyTokenActivity;
import com.erginus.fithealthy.CoachHomeActivity;
import com.erginus.fithealthy.CoachListActivity;
import com.erginus.fithealthy.GoalConsultationRoomActivity;
import com.erginus.fithealthy.R;
import com.erginus.fithealthy.commons.MapAppConstants;
import com.erginus.fithealthy.commons.MultipartRequest;
import com.erginus.fithealthy.commons.VolleySingleton;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileFragment extends android.support.v4.app.Fragment {
    ImageView pic, image;
    Button bt_gallery, bt_camera, buttonUpdate;
    private static final int PHOTO_PICKER_ID = 1;
    private static final int CAMERA_PIC_REQUEST = 2500;
    File f;
    public static EditText edt_fname, edt_lname,edt_userName, edt_email, edt_contact;
    public static RadioGroup gender;
    public static RadioButton radioButton;
    int selectedId;
    PrefsHelper prefsHelper;
    String userName, user_email,user_phn, gendr, fname, lname;
    ProgressDialog pDialog;
    String usrLogin="",  email="", contact="", gndr="", token="", gndrValue="";
    public  static RadioButton rbu1, rbu2;
    String img, filename;
    LinearLayout ll_edit_photo;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        pic=(ImageView)rootView.findViewById(R.id.profilePic);
        gender=(RadioGroup)rootView.findViewById(R.id.radio_group);
        rbu1 =(RadioButton)rootView.findViewById(R.id.radio_male);
        rbu2 =(RadioButton)rootView.findViewById(R.id.radio_female);
        ll_edit_photo=(LinearLayout)rootView.findViewById(R.id.ll_edt_profile);

        prefsHelper=new PrefsHelper(getActivity());
        edt_fname=(EditText)rootView.findViewById(R.id.edt_userFname);
        edt_lname=(EditText)rootView.findViewById(R.id.edt_userLname);

        edt_userName=(EditText)rootView.findViewById(R.id.edt_userNm);
        edt_email=(EditText)rootView.findViewById(R.id.edt_email);
        edt_contact=(EditText)rootView.findViewById(R.id.edt_contact);
        buttonUpdate=(Button)rootView.findViewById(R.id.button_updt);

        edt_fname.setText(prefsHelper.getUserFNameFromPreference());
        edt_lname.setText(prefsHelper.getUserLNameFromPreference());
        edt_userName.setText(prefsHelper.getUserNameFromPreference());
        edt_contact.setText(prefsHelper.getPhoneFromPref());
        edt_email.setText(prefsHelper.getEmailFromPreference());
        gendr=prefsHelper.getGenderFromPreference();
        if(gendr.equals("0")) {
            rbu2.setChecked(true);
            rbu1.setChecked(false);
        }
        else{
            rbu1.setChecked(true);
            rbu2.setChecked(false);
        }
        Picasso.with(getActivity()).load(prefsHelper.getUserImageFromPreference()).into(pic);
        ll_edit_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(getActivity());
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
       buttonUpdate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        View focusView = null;
        boolean cancelLogin = false;
        fname=edt_fname.getText().toString();
        lname=edt_lname.getText().toString();
        userName=edt_userName.getText().toString();
        user_email=edt_email.getText().toString();
        user_phn=edt_contact.getText().toString();
        if(gendr.equals("")) {
            radioButton.isClickable();
            selectedId = gender.getCheckedRadioButtonId();
            radioButton = (RadioButton) rootView.findViewById(selectedId);
            gendr = radioButton.getText().toString();
        }
        else
        {
            gendr=prefsHelper.getGenderFromPreference();
            if(gendr.equals("0")) {
                rbu2.setChecked(true);
                rbu1.setChecked(false);
            }
            else{
                rbu1.setChecked(true);
                rbu2.setChecked(false);
            }
        }
        if (TextUtils.isEmpty(userName)) {
            edt_userName.setError(getString(R.string.userName_required));
            focusView = edt_userName;
            cancelLogin = true;
        }
        if (TextUtils.isEmpty(fname)) {
            edt_fname.setError(getString(R.string.userName_required));
            focusView = edt_fname;
            cancelLogin = true;
        } if (TextUtils.isEmpty(lname)) {
            edt_lname.setError(getString(R.string.userName_required));
            focusView = edt_lname;
            cancelLogin = true;
        }


        if (TextUtils.isEmpty(user_email)) {
            edt_email.setError(getString(R.string.email_required));
            focusView = edt_email;
            cancelLogin = true;
        } else if (!isValidEmail(user_email)) {
            edt_email.setError(getString(R.string.invalid_email));
            focusView = edt_email;
            cancelLogin = true;
        }
        if (TextUtils.isEmpty(user_phn) & !isValidPhone((user_phn))) {
            edt_contact.setError(getString(R.string.phone_required));
            focusView = edt_contact;
            cancelLogin = true;
        }
        if (cancelLogin) {
            // error in login
            focusView.requestFocus();
        } else {
            editProfile();
        }


    }
});
        return rootView;
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhone(String pass) {
        if (pass != null && pass.length()==10) {
            return true;
        }
        return false;
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
                f = new File(filename);
                uploadImage();

            }

            break;
        case PHOTO_PICKER_ID:
            if (requestCode == PHOTO_PICKER_ID
                    && resultCode == Activity.RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                compressImage(getPath(selectedImage));
                f= new File(filename);
                uploadImage();

            }

            break;
    }
    }catch (Exception e)
    {
        Log.d("krvrrusbviuritiribtr",e.getMessage());
    }
    }


    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null)
            return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
   
    public void uploadImage()
    {
        try {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "LOGIN " + MapAppConstants.API +"/profile_image");
            HashMap params = new HashMap<String, String>();
            params.put("user_id", prefsHelper.getUserIdFromPrefrence());
            params.put("user_security_hash", prefsHelper.getSecHashFromPrefrence());
            Log.d(prefsHelper.getUserIdFromPrefrence(), prefsHelper.getSecHashFromPrefrence());

            MultipartRequest sr = new MultipartRequest(MapAppConstants.API +"/profile_image", new Response.Listener<String>() {

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
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject object1 = object.getJSONObject("data");
                                    img=object1.getString("user_profile_image_url");
                                    prefsHelper.storeUserImageBitmapToPreference(img);

                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            Picasso.with(getActivity()).load(prefsHelper.getUserImageFromPreference()).into(GoalConsultationRoomActivity.profileImage);
                            Picasso.with(getActivity()).load(prefsHelper.getUserImageFromPreference()).into(pic);
                            Picasso.with(getActivity()).load(prefsHelper.getUserImageFromPreference()).into(CoachListActivity.profileImage);
                            Picasso.with(getActivity()).load(prefsHelper.getUserImageFromPreference()).into(BuyTokenActivity.profileImage);

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

            }, f, params);
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);


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

        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = null;
    }
    public void editProfile()
    {
        try {
             pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Edit Profile" + MapAppConstants.API +"/edit_profile");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstants.API+"/edit_profile", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
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
                                   String fname, lname;
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    usrLogin=jsonObject.getString("user_login");
                                    email=jsonObject.getString("user_email");
                                    contact=jsonObject.getString("user_primary_contact");
                                    gndr=jsonObject.getString("user_gender");
                                    fname=jsonObject.getString("user_first_name");
                                    lname=jsonObject.getString("user_last_name");
                                    prefsHelper.storeUserFNameToPrefrence(fname);
                                    prefsHelper.storeUserLNameToPrefrence(lname);
                                    prefsHelper.storeEmailToPreference(email);
                                    prefsHelper.storePhoneToPreference(contact);
                                    prefsHelper.storeGenderToPreference(gndr);
                                    prefsHelper.storeUserNameToPreference(usrLogin);

                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent=new Intent(getActivity(), GoalConsultationRoomActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                            getActivity().finish();
                            edt_userName.setText(prefsHelper.getUserNameFromPreference());
                            edt_fname.setText(prefsHelper.getUserFNameFromPreference());
                            edt_lname.setText(prefsHelper.getUserLNameFromPreference());
                            edt_email.setText(prefsHelper.getEmailFromPreference());
                            edt_contact.setText(prefsHelper.getPhoneFromPref());
                            GoalConsultationRoomActivity.user_name.setText(prefsHelper.getUserFNameFromPreference()+" "+prefsHelper.getUserLNameFromPreference());

                            gndrValue=prefsHelper.getGenderFromPreference();
                            if(gndrValue.equalsIgnoreCase("0")) {
                                rbu2.setChecked(true);
                                rbu1.setChecked(false);
                            }
                            else{
                                rbu1.setChecked(true);
                                rbu2.setChecked(false);
                            }

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

                    params.put("user_id",prefsHelper.getUserIdFromPrefrence());
                    params.put("user_security_hash",prefsHelper.getSecHashFromPrefrence());
                    params.put("user_first_name", fname);
                    params.put("user_last_name", lname);
                    params.put("user_login", userName);
                    params.put("user_email",user_email);
                    params.put("user_primary_contact",user_phn);
                    params.put("user_gender",gendr);

                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

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
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
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


}

