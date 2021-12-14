package com.myapp.royalcounselling;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    public static VolleySingleton mInstance;
    Context context;

    RequestQueue mRequestQueue;

    public VolleySingleton(Context context) {
        this.context = context;
        mRequestQueue = (RequestQueue) getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }

        return mInstance;
    }

    private <T> RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
