package com.prueba.factum.grupodigital.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prueba.factum.grupodigital.Activities.DetalleActivity;
import com.prueba.factum.grupodigital.Beans.Estudiante;
import com.prueba.factum.grupodigital.Interfaces.RestClient;
import com.prueba.factum.grupodigital.R;
import com.prueba.factum.grupodigital.Util.APICliente;

import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by David on 03/07/2017.
 */

public class DetalleFragment extends Fragment {
    private String id;
    private String name;
    private String fechaNac;
    private TextView idTv;
    private TextView nombreTv;
    private TextView fechaNacTv;
    private ImageView delete;
    private ImageView update;
    private int respuesta;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        id=String.valueOf(((DetalleActivity)getActivity()).getId());
        name=((DetalleActivity)getActivity()).getNombre();
        fechaNac=((DetalleActivity)getActivity()).getFechaNac();
        idTv=(TextView)getView().findViewById(R.id.idtv);
        nombreTv=(TextView)getView().findViewById(R.id.nametv);
        fechaNacTv=(TextView)getView().findViewById(R.id.fechanactv);
        idTv.setText(id);
        nombreTv.setText(name);
        fechaNacTv.setText(removeTime(fechaNac));
        delete=(ImageView)getView().findViewById(R.id.delete);
        update=(ImageView)getView().findViewById(R.id.update);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAlumno(id);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAlumno(id);
            }
        });
    }

    private String removeTime(String fechaNac) {
        String[] parts = fechaNac.split(" ");
        return parts[0];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("David", "onCreateView llamado");
        return inflater.inflate(R.layout.detalle_fragment_layout,container,false);
    }

    private void updateAlumno(final String id) {
        final Dialog aDialog=new Dialog(getActivity());
        aDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aDialog.setCancelable(false);
        aDialog.setContentView(R.layout.addestudiantelayout);
        TextView ok=(TextView)aDialog.findViewById(R.id.ok);
        TextView cancel=(TextView)aDialog.findViewById(R.id.cancel);
        final TextInputLayout tilNombre=(TextInputLayout)aDialog.findViewById(R.id.textInputLayout);
        final DatePicker dPicker=(DatePicker)aDialog.findViewById(R.id.datePicker2);
        tilNombre.getEditText().setText(name);
        String fecha=removeTime(fechaNac);
        String[] parts = fecha.split("-");
        String year=parts[2];
        String month=parts[1];
        String day=parts[0];

        dPicker.updateDate(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
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
                estudiante.setId(Integer.parseInt(id));
                updateEstudiante(estudiante);
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

    private void updateEstudiante(Estudiante estudiante) {
        RestClient service = APICliente.getService().create(RestClient.class);
        Call<ResponseBody> peticion=service.updateUser(estudiante);
        peticion.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.i("David","Response successful");
                    Toast.makeText(getActivity(),getString(R.string.addok), Toast.LENGTH_LONG).show();
                }else{
                    Log.i("David", "Response no es successful");
                    Toast.makeText(getActivity(),getString(R.string.addnotok), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("David", "onFailure");
                Toast.makeText(getActivity(),getString(R.string.addnotok), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteAlumno(String id) {
        RestClient service = APICliente.getService().create(RestClient.class);
        Call<ResponseBody> peticion=service.deleteUser(Integer.parseInt(id));
        peticion.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.i("David", "Alumno eliminado");
                    getActivity().finish();
                }else{
                    Log.i("David", "Fallo eliminando alumno");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("David", "onFailure");
            }
        });
    }
}
