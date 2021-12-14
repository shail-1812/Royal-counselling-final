package com.myapp.royalcounselling;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    EditText state, city, institutionName, board, grade;
    RadioGroup gender;
    Button register;
    CircleImageView imageDP;
    Button btnGallery, btnCamera, btnCancel;
    int finalStatus = 0, finalStatusGender = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        state = findViewById(R.id.edt_user_profile_state);
        city = findViewById(R.id.edt_user_profile_city);
        grade = findViewById(R.id.edt_user_profile_grade);
        board = findViewById(R.id.edt_user_profile_board);
        institutionName = findViewById(R.id.edt_user_profile_institution);
        gender = findViewById(R.id.radio_gender);
        register = findViewById(R.id.btn_user_profile_register);

        SharedPreferences sharedPreferences = getSharedPreferences("MYAPP", MODE_PRIVATE);
        String email = sharedPreferences.getString("KEY_EMAIL", "");
        imageDP = findViewById(R.id.profile_image);

        imageDP.setOnClickListener((View.OnClickListener) v -> {
            LayoutInflater layoutInflater = getLayoutInflater();
            View myview = layoutInflater.inflate(R.layout.raw_image_picker_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setView(myview);
            alertDialog.show();
            btnCancel = (Button) myview.findViewById(R.id.cancel_button);
            btnCamera = (Button) myview.findViewById(R.id.camera_button);
            btnGallery = (Button) myview.findViewById(R.id.gallery_button);

            btnGallery.setOnClickListener(v1 -> {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, 11);
                alertDialog.dismiss();
            });
            btnCamera.setOnClickListener(v12 -> {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 12);
                alertDialog.dismiss();
            });
            btnCancel.setOnClickListener(v13 -> alertDialog.dismiss());
        });
        register.setOnClickListener(v -> {
            String sta = state.getText().toString();
            String cit = city.getText().toString();
            String gra = grade.getText().toString();
            String boa = board.getText().toString();
            String institute = institutionName.getText().toString();
            String gen = null;
            if (cit.length() < 3) {
                Toast.makeText(UserProfileActivity.this, "Please enter city again", Toast.LENGTH_LONG).show();
                finalStatus = -1;
            } else if (sta.length() < 3) {
                finalStatus = -1;
                Toast.makeText(UserProfileActivity.this, "Please enter state again", Toast.LENGTH_LONG).show();
            } else if (institute.length() < 1) {
                finalStatus = -1;
                Toast.makeText(UserProfileActivity.this, "Please enter institution name again", Toast.LENGTH_LONG).show();
            } else if (gra.length() < 1) {
                finalStatus = -1;
                Toast.makeText(UserProfileActivity.this, "Please enter grade again", Toast.LENGTH_LONG).show();
            } else if (boa.length() < 2) {
                finalStatus = -1;
                Toast.makeText(UserProfileActivity.this, "Please enter board again", Toast.LENGTH_LONG).show();
            }
            if (gender.getCheckedRadioButtonId() == -1) {
                Toast.makeText(UserProfileActivity.this, "Please select gender", Toast.LENGTH_LONG).show();
                finalStatus = 0;
                finalStatusGender = -1;
            } else {
                finalStatus = 0;
                finalStatusGender = 0;
                gen = ((RadioButton) findViewById(gender.getCheckedRadioButtonId())).getText().toString();
            }
            if (finalStatus == 0 && finalStatusGender == 0) {
                loadData(cit, sta, gra, boa, institute, gen, email);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (data != null) {
                Uri selectedImage = data.getData();
                imageDP.setTag("new");
                imageDP.setImageURI(selectedImage);
            } else {
                imageDP.setImageResource(R.drawable.ic_icon);
                imageDP.setTag("default");
            }
        } else if (requestCode == 12) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageDP.setImageBitmap(imageBitmap);
                imageDP.setTag("new");
            } else {
                imageDP.setImageResource(R.drawable.ic_icon);
                imageDP.setTag("default");
            }
        }
    }

    public Bitmap getBitMapCustom(Drawable drawable) {
        int COLORDRAWABLE_DIMENSION = 2;
        Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadData(String city, String state, String grade, String board, String institutename, String gender, String emailID) {
//        ProgressDialog progressDialog = new ProgressDialog(UserProfileActivity.this);
//        progressDialog.setMessage("Loading");
//        progressDialog.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Utils.main_url + "signUpUserProfile", response -> {
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
            JSONObject jsonObject;
            try {
//                progressDialog.dismiss();
                jsonObject = new JSONObject(new String(response.data));
                String strData = jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("city", city);
                params.put("state", state);
                params.put("institutionName", institutename);
                params.put("gender", gender);
                params.put("board", board);
                params.put("grade", grade);
                params.put("emailID", emailID);
                params.put("imageDesc", (String) imageDP.getTag());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                BitmapDrawable drawable = (BitmapDrawable) imageDP.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(getBitMapCustom(imageDP.getDrawable()))));
                return params;
            }

        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(UserProfileActivity.this).add(volleyMultipartRequest);
        Intent intent = new Intent(UserProfileActivity.this, NavigationDrawerActivity.class);
        startActivity(intent);
        finish();
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}