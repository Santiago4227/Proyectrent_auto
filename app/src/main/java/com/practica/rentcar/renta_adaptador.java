package com.practica.rentcar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class renta_adaptador extends RecyclerView.Adapter<renta_adaptador.rentaviewHolder> {

    public renta_adaptador(ArrayList<renta> mainActivity_lista_renta){
        MainActivity_lista_renta = mainActivity_lista_renta;
    }
    ArrayList<renta> MainActivity_lista_renta;

    @NonNull
    @Override
    public renta_adaptador.rentaviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vrenta = LayoutInflater.from(parent.getContext()).inflate(R.layout.renta_item, null, false);
        return new rentaviewHolder(vrenta);
    }

    @Override
    public void onBindViewHolder(@NonNull renta_adaptador.rentaviewHolder holder, int position) {
        holder.renta_numero.setText(MainActivity_lista_renta.get(position).getRenta_numero().toString());
        holder.renta_placa.setText(MainActivity_lista_renta.get(position).getRenta_placa().toString());
        holder.renta_usuario.setText(MainActivity_lista_renta.get(position).getRenta_usuario().toString());
        holder.fecha_inicial.setText(MainActivity_lista_renta.get(position).getFecha_inicial().toString());
        holder.fecha_final.setText(MainActivity_lista_renta.get(position).getFecha_final().toString());
    }

    @Override
    public int getItemCount() {
        return MainActivity_lista_renta.size();
    }

    public class rentaviewHolder extends RecyclerView.ViewHolder {

        TextView renta_numero, renta_placa, renta_usuario, fecha_inicial, fecha_final;

        public rentaviewHolder(@NonNull View itemView){

            super(itemView);

            renta_numero = itemView.findViewById(R.id.tvlistNumberRent);
            renta_placa = itemView.findViewById(R.id.tvlistPlate);
            renta_usuario = itemView.findViewById(R.id.tvlistUserName);
            fecha_inicial = itemView.findViewById(R.id.tvlistInitialDate);
            fecha_final = itemView.findViewById(R.id.tvlistEndDate);
        }
    }
}
