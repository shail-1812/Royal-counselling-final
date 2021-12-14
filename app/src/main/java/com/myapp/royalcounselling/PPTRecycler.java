package com.myapp.royalcounselling;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PPTRecycler extends RecyclerView.Adapter<PPTRecycler.ViewHolder> {

    private static final String TAG = "PPTRecyclerView";

    private ArrayList<PPTBean> pptList = new ArrayList<>();
    private OnNoteListener mOnNoteListener;

    public PPTRecycler(ArrayList<PPTBean> mNotes, OnNoteListener onNoteListener) {
        this.pptList = mNotes;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_ppt_card_view, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            holder.thumbnail.setImageResource(R.drawable.ic_pdf);
            holder.fileName.setText(pptList.get(position).getFileName());
        } catch (NullPointerException e) {
            Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return pptList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thumbnail;
        TextView fileName;
        OnNoteListener mOnNoteListener;

        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.img_view_thumbnail);
            fileName = itemView.findViewById(R.id.tv_ppt_name);
            mOnNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}

