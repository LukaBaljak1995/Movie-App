package com.example.svarog.project01.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.svarog.project01.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.GregorianCalendar;


public class Pay extends AppCompatActivity {

Button pay;
ImageView image;
TextView screenshot;

    ByteArrayOutputStream bytearrayoutputstream;
    File file;
    FileOutputStream fileoutputstream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        int p = GeneratorActivity.mPrice;
        pay = (Button) findViewById(R.id.paybtn);
        image = (ImageView) findViewById(R.id.qr_image);
        screenshot = (TextView)findViewById(R.id.screenshot);

        setTitle("Payment");

        TextView tvPrice = (TextView)findViewById(R.id.price);
        tvPrice.setText(String.valueOf(p) + " din");

        TextView tvTitle = (TextView)findViewById(R.id.movie);
        tvTitle.setText(DetailsActivity.title);

        TextView dateTitle = (TextView)findViewById(R.id.date);
        int day;
        GregorianCalendar now = new GregorianCalendar();
        int month = now.get(GregorianCalendar.MONTH)+1;
        dateTitle.setText(String.valueOf(GeneratorActivity.mDay+". "+month+". " + DetailsActivity.hour +" h"));

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text2Qr = DetailsActivity.title + GeneratorActivity.mPrice;
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                    screenshot.setText("Payment succesfully done! Your QR code was successfully saved on your phone for ticket validation!");
                    bytearrayoutputstream = new ByteArrayOutputStream();


                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytearrayoutputstream);




                    file = new File( Environment.getExternalStorageDirectory() + "/qr.png");
                    try

                    {
                        file.createNewFile();

                        fileoutputstream = new FileOutputStream(file);

                        fileoutputstream.write(bytearrayoutputstream.toByteArray());

                        fileoutputstream.close();

                    }

                    catch (Exception e)

                    {

                        e.printStackTrace();

                    }

                    Toast.makeText(Pay.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();



                }catch (WriterException e){
                    e.printStackTrace();
                }
            }

        });


    }


}
