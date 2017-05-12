package com.example.arjaireynolds.emailtest;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button send = (Button) this.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try{
                    GMailSender sender = new GMailSender("group7testdata@gmail.com","javaisshit");
                    sender.sendMail("This is the subject",
                                    "body",
                                    "group7testdata@gmail.com",
                                    "arjai.reynolds@outlook.com");

                } catch (Exception e ){
                    e.printStackTrace();
                }
            }
        });

    }
}
