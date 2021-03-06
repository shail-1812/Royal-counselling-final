package com.myapp.royalcounselling.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.myapp.royalcounselling.MySeminarAdapter;
import com.myapp.royalcounselling.R;
import com.myapp.royalcounselling.Seminar;
import com.myapp.royalcounselling.Utils;
import com.myapp.royalcounselling.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DisplaySeminarFragment extends Fragment {


    ArrayList<Seminar> seminarsList = new ArrayList<>();
    ListView listView;
    Button viewRegistered;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_display_seminar, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MYAPP", MODE_PRIVATE);
        String email = sharedPreferences.getString("KEY_EMAIL", "");
        String urlPost = Utils.main_url + "getActiveSeminarListForRegisteration/" + email;
        textView = rootView.findViewById(R.id.txt_empty);

//        Toast.makeText(getActivity(), urlPost, Toast.LENGTH_LONG).show();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPost, response -> {
            Log.e("TAG", "onResponse: " + response);
            String seminarID;
            String seminarName;
            String seminarRegistrationEnd;
            String seminarRegistrationStart;
            String seminarStart;
            String seminarEnd;
            String seminarType;
            String seminarZoomLink;
            String whatsappLink;
            String seminarFees;
            String seminarDescription;


            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Seminar seminar = new Seminar();
                    JSONObject object = jsonArray.getJSONObject(i);
                    seminarID = (String.valueOf(object.getString("seminarID")));
                    seminarName = (String.valueOf(object.getString("seminarName")));
                    seminarRegistrationEnd = (String.valueOf(object.getString("seminarRegistrationEnd"))).replace('T', ' ');
                    seminarRegistrationStart = (String.valueOf(object.getString("seminarRegistrationStart"))).replace('T', ' ');
                    seminarStart = (String.valueOf(object.getString("seminarStart"))).replace('T', ' ');
                    seminarEnd = (String.valueOf(object.getString("seminarEnd"))).replace('T', ' ');
                    seminarType = (String.valueOf(object.getString("seminarType")));
                    seminarZoomLink = (String.valueOf(object.getString("seminarZoomLink")));
                    whatsappLink = (String.valueOf(object.getString("whatsappLink")));
                    seminarFees = (String.valueOf(object.getString("seminarFees")));
                    seminarDescription = (String.valueOf(object.getString("seminarDescription")));


                    seminar.setSeminarId(seminarID);
                    seminar.setSeminarName(seminarName);
                    seminar.setSeminarRegistrationEnd(seminarRegistrationEnd);
//                    seminar.setSeminarRegistrationStart(seminarRegistrationStart);
                    seminar.setSeminarStart(seminarStart);
                    seminar.setSeminarEnd(seminarEnd);
                    seminar.setSeminarType(seminarType);
                    seminar.setSeminarZoomLink(seminarZoomLink);
                    seminar.setWhatsappLink(whatsappLink);
                    seminar.setSeminarFees(seminarFees);
                    seminar.setSeminarDescription(seminarDescription);
                    seminarsList.add(seminar);
                }
                Log.e("seminar size", String.valueOf(seminarsList.isEmpty()));

                if (seminarsList.isEmpty()) {
                    viewRegistered = rootView.findViewById(R.id.btn_view_registered);
                    viewRegistered.setVisibility(View.VISIBLE);
                    textView.setText("Sorry!!! No seminars at the moment");
                    viewRegistered.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Fragment fragment = new DisplayRegisteredSeminarFragment();
                            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame, fragment);
                            fragmentTransaction.commit();

                        }
                    });
                } else {
                    textView.setText("");
                }
                listView = rootView.findViewById(R.id.list);
                MySeminarAdapter mySeminarAdapter = new MySeminarAdapter(getContext(), seminarsList, "nonRegistered");
                listView.setAdapter(mySeminarAdapter);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> Log.e("api error", "something went wrong" + error)) {


        };


//        if (seminarsList.size() == 0) {
//
//        }

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


        return rootView;
    }
}