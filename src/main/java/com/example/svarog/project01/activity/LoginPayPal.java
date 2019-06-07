package com.example.svarog.project01.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.svarog.project01.R;
public class LoginPayPal extends AppCompatActivity {

    Button login;
    TextView user;
    TextView password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pay_pal);
        setTitle("PayPal");
        login=(Button)findViewById(R.id.bt_SignIn);
        user = (TextView) findViewById(R.id.et_Username);
        password = (TextView) findViewById(R.id.et_Password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((String.valueOf(user.getText()).equals("djordjevicana55@gmail.com")
                        ||(String.valueOf(user.getText()).equals("sofija.coka.milicevic@gmail.com"))) &&
                        String.valueOf(password.getText()).length()>5){
                    Intent intent = new Intent(LoginPayPal.this, Pay.class);
                        startActivity(intent);
                } else {
                    Toast.makeText(LoginPayPal.this, "Incorrect username or password!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}
