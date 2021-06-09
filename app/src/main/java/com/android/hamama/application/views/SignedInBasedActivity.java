package com.android.hamama.application.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import com.android.hamama.application.R;

public abstract class  SignedInBasedActivity extends BroadcastBasedActivity {

    /*
        This class contains all the screens, besides the login screen
        (all the screens which extends from that class (it also means
        that all the screens have the BroadcastReceive)
        in other words, all of the screen which bypassed the login process
        are extends from that class.
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*
        when you click the menu(and select 'settings'), you opens a new fragment which
        contain the content of preferences.xml (the 'stay-login' option)
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, new MySettingsFragment()) // sets the screen
                        .addToBackStack(null) // when you press button, you'll be in home screen, instead of exit the app
                        .commit();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /*
        the class, which sets the fragment after clicking the 'settings' menu
    */

    public static class MySettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            if (view != null) {
                view.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
            return view;
        }
    }
}
