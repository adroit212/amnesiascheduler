package amnesiascheduler.ephraim.com.amnesiascheduler;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread logoTimer=new Thread(){
            public void run(){
                try{
                    int timer=0;
                    while(timer<3000){
                        sleep(100);
                        timer+=100;
                    }
                    Intent in=new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(in);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    finish();
                }
            }
        };
        logoTimer.start();
    }
}
