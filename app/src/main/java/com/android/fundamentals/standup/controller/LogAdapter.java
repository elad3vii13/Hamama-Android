package com.android.fundamentals.standup.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fundamentals.standup.R;
import com.android.fundamentals.standup.communication.CommService;
import com.android.fundamentals.standup.model.LogEntry;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class LogAdapter extends RecyclerView.Adapter<LogAdapter.MyViewHolder> {
     ArrayList<LogEntry> entries;
     Context context;

     public LogAdapter(Context context, ArrayList<LogEntry> entries){
         this.entries = entries;
         this.context = context;
         notifyDataSetChanged();
     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_entry, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LogEntry entry = entries.get(position);
        holder.bindData(entry);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
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
