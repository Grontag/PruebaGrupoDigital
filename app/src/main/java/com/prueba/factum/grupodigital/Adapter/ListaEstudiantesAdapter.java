package com.prueba.factum.grupodigital.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prueba.factum.grupodigital.Beans.Estudiante;
import com.prueba.factum.grupodigital.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by David on 03/07/2017.
 */

public class ListaEstudiantesAdapter extends RecyclerView.Adapter<ListaEstudiantesAdapter.EstudiantesViewHolder> implements View.OnClickListener{

    private ArrayList<Estudiante> datos;
    private View.OnClickListener listener;

    public ListaEstudiantesAdapter(ArrayList<Estudiante> datos) {
        this.datos = datos;
    }

    @Override
    public EstudiantesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        itemView.setOnClickListener(this);
        EstudiantesViewHolder evh=new EstudiantesViewHolder(itemView);
        return evh;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(EstudiantesViewHolder holder, int position) {
        Estudiante estudiante=datos.get(position);
        holder.bindEstudiante(estudiante);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public static class EstudiantesViewHolder extends RecyclerView.ViewHolder {

        private TextView id;
        private TextView nombre;
        private TextView fechaNac;

    public EstudiantesViewHolder(View itemView) {
        super(itemView);
        id=(TextView)itemView.findViewById(R.id.idtv);
        nombre=(TextView)itemView.findViewById(R.id.nombretv);
        fechaNac=(TextView)itemView.findViewById(R.id.fechanactv);
    }
        public void bindEstudiante(Estudiante estudiante) {
            id.setText(String.valueOf(estudiante.getId()));
            nombre.setText(estudiante.getName());
            DateFormat df=new SimpleDateFormat("dd-MM-yyyy");
            String fecha=df.format(estudiante.getBirthdate());
            fechaNac.setText(fecha);
        }




    }
}
