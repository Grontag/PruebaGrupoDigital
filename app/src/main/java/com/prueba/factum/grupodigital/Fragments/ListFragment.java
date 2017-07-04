package com.prueba.factum.grupodigital.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.prueba.factum.grupodigital.Activities.DetalleActivity;
import com.prueba.factum.grupodigital.Adapter.ListaEstudiantesAdapter;
import com.prueba.factum.grupodigital.Beans.Estudiante;
import com.prueba.factum.grupodigital.Interfaces.RestClient;
import com.prueba.factum.grupodigital.R;
import com.prueba.factum.grupodigital.Util.APICliente;
import com.prueba.factum.grupodigital.Util.InternetCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by David on 03/07/2017.
 */

public class ListFragment extends Fragment {

    private RecyclerView rView;
    private ProgressDialog pDialog;
    private ArrayList<Estudiante> datos;
    private ListaEstudiantesAdapter adapter;
    private SharedPreferences prefs;
    private FloatingActionButton flaAdd;
    private int respuesta;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefs=getActivity().getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        rView=(RecyclerView)getView().findViewById(R.id.rview);
        rView.setHasFixedSize(true);
        flaAdd=(FloatingActionButton)getView().findViewById(R.id.floatingActionButton);
        flaAdd.bringToFront();
        new InternetCheck(getActivity()).isInternetConnectionAvailable(new InternetCheck.InternetCheckListener() {

            @Override
            public void onComplete(boolean connected) {
                if(connected){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog=new ProgressDialog(getActivity());
                            pDialog.setTitle(getString(R.string.waitplease));
                            pDialog.setMessage(getString(R.string.fetchingdata));
                            pDialog.setCancelable(false);
                            pDialog.show();
                            obtenerDatos();
                        }
                    });

                }else{
                    showAlertDialog(getString(R.string.nointernet));
                }

            }
        });

        flaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogAñadir();
            }
        });

    }

    private void mostrarDialogAñadir() {
        final Dialog aDialog=new Dialog(getActivity());
        aDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aDialog.setCancelable(false);
        aDialog.setContentView(R.layout.addestudiantelayout);
        TextView ok=(TextView)aDialog.findViewById(R.id.ok);
        TextView cancel=(TextView)aDialog.findViewById(R.id.cancel);
        final TextInputLayout tilNombre=(TextInputLayout)aDialog.findViewById(R.id.textInputLayout);
        final DatePicker dPicker=(DatePicker)aDialog.findViewById(R.id.datePicker2);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre=tilNombre.getEditText().getText().toString();

                GregorianCalendar calendarBeg=new GregorianCalendar(dPicker.getYear(),
                        dPicker.getMonth(),dPicker.getDayOfMonth());
                Date birthdate=calendarBeg.getTime();

                Estudiante estudiante=new Estudiante();
                estudiante.setName(nombre);
                estudiante.setBirthdate(birthdate);
                int retorno=sendEstudiante(estudiante);
                if(retorno==1){
                    Toast.makeText(getActivity(),getString(R.string.addok), Toast.LENGTH_LONG).show();
                    obtenerDatos();
                }else{
                    Toast.makeText(getActivity(),getString(R.string.addnotok), Toast.LENGTH_LONG).show();
                }

                aDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aDialog.dismiss();
            }
        });

        aDialog.show();
    }

    private int sendEstudiante(Estudiante estudiante) {
        RestClient service = APICliente.getService().create(RestClient.class);
        Call<ResponseBody> peticion=service.createNewUser(estudiante);
        peticion.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.i("David","Response successful");
                    respuesta=1;
                }else{
                    Log.i("David", "Response no es successful");
                    respuesta=0;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("David", "onFailure");
                respuesta=0;
            }
        });
        return respuesta;
    }

    private void obtenerDatos() {

        datos=new ArrayList<>();
        RestClient service = APICliente.getService().create(RestClient.class);
        Call<ResponseBody> peticion=service.getAllStudents();
        peticion.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("David", "onResponse");
                if(response.isSuccessful()){
                    Log.i("David", "Response successful");
                    try {
                        JSONArray obj=new JSONArray(response.body().string());
                        for(int i=0;i<obj.length();i++){
                            Estudiante estudiante=new Estudiante();
                            JSONObject object=obj.getJSONObject(i);
                            if(!object.getString("name").equals("null")){
                                estudiante.setName(object.getString("name"));
                            }else{
                                estudiante.setName(getString(R.string.unknownname));
                            }
                            if(!object.getString("id").equals("null")){
                                estudiante.setId(Integer.parseInt(object.getString("id")));
                            }else{
                                estudiante.setId(0);
                            }
                            if(!object.getString("birthdate").equals("null")){
                                String dateTime=object.getString("birthdate");
                                String[] parts = dateTime.split("T");
                                String fecha=parts[0];
                                String hora=parts[1];
                                String dateFinal=fecha+" "+hora;
                                Date date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateFinal);
                                estudiante.setBirthdate(date);
                                datos.add(estudiante);
                            }




                        }

                        if(pDialog!=null){
                            if(pDialog.isShowing()){
                                pDialog.dismiss();
                            }
                        }
                        adapter=new ListaEstudiantesAdapter(datos);
                        rView.setLayoutManager(
                                new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.i("David", "Hemos pulsado el elemento "+rView.getChildLayoutPosition(view));
                                Log.i("David", "En el elemento seleccionado, el nombre es: "+datos.get(rView.getChildLayoutPosition(view)).getName());
                                Intent intent=new Intent(getActivity(), DetalleActivity.class);
                                prefs.edit().putString("id",String.valueOf(datos.get(rView.getChildLayoutPosition(view)).getId())).apply();
                                prefs.edit().putString("name", datos.get(rView.getChildLayoutPosition(view)).getName()).apply();
                                DateFormat df=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String stringDate=df.format(datos.get(rView.getChildLayoutPosition(view)).getBirthdate());
                                prefs.edit().putString("fechaNac", stringDate).apply();
                                startActivity(intent);

                            }
                        });
                        rView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.i("David", "Response no es successful");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("David", "onFailure");
                if(pDialog!=null){
                    if(pDialog.isShowing()){
                        pDialog.dismiss();
                    }
                }
                showAlertDialog(getString(R.string.falloservidor));
            }
        });
    }

    private void showAlertDialog(final String text){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder aDialog=new AlertDialog.Builder(getActivity());
                aDialog.setTitle(getString(R.string.alert));
                aDialog.setMessage(text);
                aDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getActivity().finish();
                    }
                });
                aDialog.show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("David", "onResume");
        obtenerDatos();


    }
}
