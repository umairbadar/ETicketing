package com.example.lubna.eticketing.Others;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lubna.eticketing.R;
import com.example.lubna.eticketing.Survey.Survey_Login;


public class splash extends Activity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);

    }
    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartAnimations();
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        SharedPreferences pref = getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        Boolean saveLogin = pref.getBoolean("saveLogin",false);

        if (saveLogin.equals(true))
        {
            finish();
            startActivity(new Intent(splash.this, MainMenu.class));
        }
        else {


            //Toast.makeText(splash.this,name,Toast.LENGTH_LONG).show();

            splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        int waited = 0;
                        // Splash screen pause time
                        while (waited < 3500) {
                            sleep(100);
                            waited += 100;
                        }
                        Intent intent = new Intent(splash.this, Survey_Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        splash.this.finish();

                    } catch (InterruptedException e) {
                        // do nothing
                    } finally {
                        splash.this.finish();
                    }

                }
            };
            splashTread.start();
        }

    }
}
