package com.project.rafael.kizunaapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.project.rafael.kizunaapp.views.TextViewCustom1;

import io.codetail.animation.SupportAnimator;

public class SplashActivity extends AppCompatActivity {
    private TextViewCustom1 tvSplash;
    private Handler mHandler= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvSplash= (TextViewCustom1)findViewById(R.id.tvSplash);
        getSupportActionBar().hide();

        tvSplash.post( new Runnable(){
                           @Override
                           public void run() {
                               int x= tvSplash.getRight();
                               int y=tvSplash.getBottom();
                               float Rad= Math.max(tvSplash.getHeight(),tvSplash.getWidth())*1.5f;
                               if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                                   Animator animator = ViewAnimationUtils.createCircularReveal(tvSplash, x, y, 0, Rad);
                                   animator.setDuration(2400);
                                   tvSplash.setVisibility(View.VISIBLE);
                                   animator.addListener(new AnimatorListenerAdapter() {
                                       @Override
                                       public void onAnimationEnd(Animator animation) {
                                           super.onAnimationEnd(animation);
                                           Intent intent = new Intent(SplashActivity.this, RegistroActivity.class);
                                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                           startActivity(intent);
                                       }
                                   });
                                   animator.start();
                               }else{
                                   //
                                   //
                                   tvSplash.setVisibility(View.VISIBLE);

                                   mHandler.postDelayed(new Runnable() {
                                       public void run() {
                                           Intent intent = new Intent(SplashActivity.this, RegistroActivity.class);
                                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                           startActivity(intent);
                                       }
                                   }, 2200);


                               }
                           }
                       }
        );
    }
}
