package com.example.svarog.project01.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.svarog.project01.R;

import java.util.GregorianCalendar;


public class GeneratorActivity extends AppCompatActivity   {

    EditText text;
    Button gen_btn;
    ImageView image;
    String text2Qr;

    private int mQuantity =1;
    public static int mPrice=300;

    public static int mDay;
    TextView dateTextView1;
    TextView dateTextView;
    private int minDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);
        setTitle("Ticket");
        mPrice=300;
      

        text = (EditText) findViewById(R.id.text);
        gen_btn = (Button) findViewById(R.id.gen_btn);
        image = (ImageView)  findViewById(R.id.image);


        dateTextView1 = (TextView) findViewById(R.id.quantity_text_view1);
        dateTextView = (TextView) findViewById(R.id.date);
        GregorianCalendar now = new GregorianCalendar();
        if(now.get(GregorianCalendar.HOUR_OF_DAY) > DetailsActivity.hour){
            mDay = now.get(GregorianCalendar.DAY_OF_MONTH)+1;
        } else {
            mDay = now.get(GregorianCalendar.DAY_OF_MONTH);
        }
        minDay=mDay;
        dateTextView1.setText(String.valueOf(mDay));
        dateTextView.setText(String.valueOf(mDay));

        gen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneratorActivity.this, LoginPayPal.class);
                startActivity(intent);
            }
        });



    }

    private void displayPrice(int num){
        TextView ptv = (TextView) findViewById(R.id.totalPrice);
        mPrice=300*num;
        ptv.setText(String.valueOf(300*num + " din"));

    }

    private void displayDate(int num){
        TextView ptv = (TextView) findViewById(R.id.date);
        ptv.setText(String.valueOf(num));

    }

    public void increment(View view){

        mQuantity = mQuantity + 1;
        displayQuantity(mQuantity);
        displayPrice(mQuantity);

    }

    public void decrement(View view){
        if (mQuantity > 1){

            mQuantity = mQuantity - 1;
            displayQuantity(mQuantity);
            displayPrice(mQuantity);

        }
    }

    public void increment1(View view){

        mDay = mDay + 1;
        displayDate(mDay);
        displayQuantity1(mDay);

    }

    public void decrement1(View view){
        if (mDay > minDay){
            mDay = mDay - 1;
            displayDate(mDay);
            displayQuantity1(mDay);

        }
    }

    public void displayQuantity(int num)
    {
        TextView qtv = (TextView) findViewById(R.id.quantity_text_view);
        qtv.setText(String.valueOf(num));
    }


    public void displayQuantity1(int num1)
    {
        TextView dtv = (TextView) findViewById(R.id.quantity_text_view1);
        dtv.setText(String.valueOf(num1));
    }

}