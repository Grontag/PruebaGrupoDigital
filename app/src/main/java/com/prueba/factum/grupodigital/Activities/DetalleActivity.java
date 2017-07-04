package com.prueba.factum.grupodigital.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.prueba.factum.grupodigital.R;

/**
 * Created by David on 03/07/2017.
 */

public class DetalleActivity extends FragmentActivity {

    private int id;
    private String nombre;
    private String fechaNac;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs=getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        id=Integer.parseInt(prefs.getString("id", "0"));
        nombre=prefs.getString("name", "");
        fechaNac=prefs.getString("fechaNac", "");
        setContentView(R.layout.detalle_activity_layout);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }
}
