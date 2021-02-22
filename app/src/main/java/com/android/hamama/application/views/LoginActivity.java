package com.android.hamama.application.views;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.hamama.application.R;
import com.android.hamama.application.communication.CommService;


public class LoginActivity extends BroadcastBasedActivity {
    EditText username_txt, password_txt;
    Button button;

    @Override
    protected void onBroadcastReceived(Intent intent) {
        switch(intent.getAction()){
            case CommService.SIGNIN_RESPONSE:
                String result = intent.getStringExtra("signin_result");
                if(Integer.parseInt(result) == -1)
                    Toast.makeText(this, "Failed to login", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(LoginActivity.this, MainMenu.class);
                    intent1.putExtra("name", username_txt.getText().toString());
                    startActivity(intent1);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(CommService.SIGNIN_RESPONSE);
        registerReceiver(drr, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_txt = findViewById(R.id.username);
        password_txt = findViewById(R.id.password);
        button = findViewById(R.id.login_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putInt("recipient", CommService.SIGNIN_RECIPIENT);
                bundle.putString("nickname", username_txt.getText().toString());
                bundle.putString("password", password_txt.getText().toString());

                Intent intent = new Intent(LoginActivity.this, CommService.class);
                intent.putExtras(bundle);
                startForegroundService(intent);
            }
        });
    }
}