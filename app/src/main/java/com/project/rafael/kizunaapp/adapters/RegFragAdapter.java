package com.project.rafael.kizunaapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.project.rafael.kizunaapp.fragments.Registro_Crear;
import com.project.rafael.kizunaapp.fragments.Registro_Ingresar;

/**
 * Created by RAFAEL on 27/03/2017.
 */

public class RegFragAdapter extends FragmentStatePagerAdapter {
    public RegFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return Registro_Ingresar.newInstance();
            case 1: return Registro_Crear.newInstance();
            default: return Registro_Ingresar.newInstance();
        }
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Sign In";
            case 1:return "Sign Up";
            default: return "Sign In";
        }
    }
}
