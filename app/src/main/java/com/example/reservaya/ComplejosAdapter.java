package com.example.reservaya;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ComplejosAdapter extends RecyclerView.Adapter<ComplejosAdapter.ComplejosViewHolder> {
    private List<Complejo> items;
    private OnClickListener onClickListener;

    public static class ComplejosViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView direccion;
        public TextView cantidad;
        public TextView distancia;


        public Button eliminar;



        public ComplejosViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen_complejo);
            nombre = (TextView) v.findViewById(R.id.nombre_complejo);
            direccion = (TextView) v.findViewById(R.id.direccion_complejo);
            cantidad = (TextView) v.findViewById(R.id.cant_canchas);
            distancia = (TextView) v.findViewById(R.id.tv_distancia);
            eliminar = (Button)  v.findViewById(R.id.bt_eliminar_complejo);



        }
    }

    public ComplejosAdapter(List<Complejo> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ComplejosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.complejos_cardview, viewGroup, false);
        return new ComplejosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ComplejosViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageResource(items.get(i).getImagenComplejo());
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.direccion.setText(items.get(i).getDireccion());

        viewHolder.cantidad.setText(items.get(i).getCanchasDisponibles() + " canchas disponibles");
        viewHolder.distancia.setText(items.get(i).getDistancia());

        Boolean propietario = items.get(i).getPropietario();
        if (propietario) {
            viewHolder.eliminar.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(i, items.get(i));
                }
            }
        });

    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Complejo model);
    }
}
