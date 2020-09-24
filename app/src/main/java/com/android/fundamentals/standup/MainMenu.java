package com.android.fundamentals.standup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainMenu extends AppCompatActivity {
    public GoogleSignInClient mGoogleSignInClient;

    TextView welcome_tv;
    Button signout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        signout_btn = findViewById(R.id.signout);
        welcome_tv = findViewById(R.id.welcome_txt);

        Intent intent = getIntent();
        GoogleSignInOptions gso = (GoogleSignInOptions) intent.getParcelableExtra("gso");
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        welcome_tv.setText(  "ברוכים הבאים, " + account.getGivenName());
    }

    public void signOut(View view) {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Intent intent = new Intent(MainMenu.this, LoginActivity.class);
                        Toast.makeText(MainMenu.this, "Disconnected From Google Account", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });
    }

    public void ApplicationSettingsActivity(View view) {
        Intent intent = new Intent(MainMenu.this, ApplicationSettings.class);
        startActivity(intent);
    }

    public void LogActivity(View view) {
        Intent intent = new Intent(MainMenu.this, Log.class);
        startActivity(intent);
    }

    public void SettingsActivity(View view) {
        Intent intent = new Intent(MainMenu.this, Settings.class);
        startActivity(intent);
    }


    public void MeasuresActivity(View view) {
        Intent intent = new Intent(MainMenu.this, Measures.class);
        startActivity(intent);
    }
}