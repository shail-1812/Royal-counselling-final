package com.myapp.royalcounselling.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myapp.royalcounselling.R;

public class ContactUsFragment extends Fragment {


    ImageView mapImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        mapImage = rootView.findViewById(R.id.map_image);
        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String url = (String) v.getTag();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
//        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        if (mSupportMapFragment == null) {
//            FragmentManager fragmentManager = getFragmentManager();
//            assert fragmentManager != null;
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            mSupportMapFragment = SupportMapFragment.newInstance();
//            fragmentTransaction.replace(R.id.map, mSupportMapFragment).commit();
//        }
//
//        mSupportMapFragment.getMapAsync(googleMap -> {
//            if (googleMap != null) {
//
//                googleMap.getUiSettings().setAllGesturesEnabled(true);
//
//
//                LatLng royal = new LatLng(23.037251364319918, 72.56541507774158);
//                googleMap.addMarker(new MarkerOptions().position(royal).title("Royal Technosoft pvt.ltd, Ahmedabad"));
//
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(royal).zoom(15.0f).build();
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//                googleMap.moveCamera(cameraUpdate);
//
//            }
//
//        });

        return rootView;
    }
}