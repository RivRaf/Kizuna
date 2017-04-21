package com.project.rafael.kizunaapp;

import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;

import com.project.rafael.kizunaapp.adapters.RegFragAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistroActivity extends AppCompatActivity {

    @BindView(R.id.tsSiSu)
    PagerTitleStrip tsSiSu;
    @BindView(R.id.vpSiSu)
    ViewPager vpSiSu;
    private RegFragAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new RegFragAdapter(getSupportFragmentManager());
        vpSiSu.setAdapter(adapter);
        tsSiSu.setGravity(Gravity.CENTER);
        //tsSiSu.setTextColor(Color.WHITE);
        tsSiSu.setTextColor(getResources().getColor(R.color.color04));
        tsSiSu.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
    }
}
