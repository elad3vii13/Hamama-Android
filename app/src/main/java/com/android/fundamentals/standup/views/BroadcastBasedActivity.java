package com.android.fundamentals.standup.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.preference.PreferenceFragmentCompat;
import com.android.fundamentals.standup.R;

public abstract class BroadcastBasedActivity extends AppCompatActivity {

    protected abstract void onBroadcastReceived(Intent intent);
    DataResultReceiver drr;

    @Override
    protected void onPause() {
        unregisterReceiver(drr);
        super.onPause();
    }

    @Override
    protected void onResume() {
        drr = new DataResultReceiver();
        super.onResume();
    }

    public class DataResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);
        }
    }
}

