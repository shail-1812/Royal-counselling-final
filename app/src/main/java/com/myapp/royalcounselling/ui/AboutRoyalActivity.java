package com.myapp.royalcounselling.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.myapp.royalcounselling.R;
import com.myapp.royalcounselling.Utils;
import com.myapp.royalcounselling.VolleySingleton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class AboutRoyalActivity extends Fragment {


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MYAPP", Context.MODE_PRIVATE);
        int flag = sharedPreferences.getInt("flag", 0);

        if (flag == 1) {

            String counsellingID = sharedPreferences.getString("counsellingID", "");
            String email = sharedPreferences.getString("KEY_EMAIL", "");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("flag", 0);
            editor.apply();

            String urlPost1 = Utils.main_url + "requestForPersonalCounselling/" + email + "/" + counsellingID;


            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, urlPost1, response1 -> {


                try {
                    JSONObject jsonObject1 = new JSONObject(response1);
                    int status = jsonObject1.getInt("status");

                    if (status == 200) {
                        Toast.makeText(getContext(), "Personal counselling session booked. You will receive notification once the admin approves.", Toast.LENGTH_LONG).show();
                    } else if (status == -1) {
                        Toast.makeText(getContext(), "Slot Already booked by someone or requested by you!!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Error please try again after some time!!", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error Please try again!!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }, error -> Log.e("api error", "something went wrong" + error)
            ) {


            };
            stringRequest1.setRetryPolicy(new DefaultRetryPolicy(
                    0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest1);

        }
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }
}