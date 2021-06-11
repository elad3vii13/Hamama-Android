package com.android.hamama.application.controller;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hamama.application.R;
import com.android.hamama.application.model.LogEntry;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Date;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.MyViewHolder> {
     ArrayList<LogEntry> entries; // ArrayList which contain the desired data to be on the listView
     Context context; // the context of the activity which contain the listView

     public LogAdapter(Context context, ArrayList<LogEntry> entries){
         this.entries = entries;
         this.context = context;
         notifyDataSetChanged();
         /* Notifies the attached observers that the underlying data has been changed
            and any View reflecting the data set should refresh itself.
         */
     }

    // Initialize the RecyclerView
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_entry, parent, false);
        return new MyViewHolder(view);
    }

    // returns the size of the arrayList
    @Override
    public int getItemCount() {
        return entries.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LogEntry entry = entries.get(position);
        holder.bindData(entry);
    }

    // the ViewHolder should be a binder of each recyclerView - bind every data to the right component
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPriority;
        TextView tvTime;
        ExpandableTextView expMessage;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgPriority = itemView.findViewById(R.id.imgPriority);
            tvTime = itemView.findViewById(R.id.tvTime);
            expMessage = itemView.findViewById(R.id.expMessage);
        }

        public void bindData(LogEntry entry){
            expMessage.setText(entry.getMessage());
            String dateString = DateFormat.format("dd/MM/yyyy", new Date(entry.getTime())).toString();
            tvTime.setText(dateString);

            switch (entry.getPriority()){
                case LogEntry.INFO:
                    imgPriority.setImageDrawable(context.getResources().getDrawable(R.drawable.info));
                    break;

                case LogEntry.WARNING:
                    imgPriority.setImageDrawable(context.getResources().getDrawable(R.drawable.warning));
                    break;

                case LogEntry.ERROR:
                    imgPriority.setImageDrawable(context.getResources().getDrawable(R.drawable.error));
                    break;
            }
        }
    }
}
