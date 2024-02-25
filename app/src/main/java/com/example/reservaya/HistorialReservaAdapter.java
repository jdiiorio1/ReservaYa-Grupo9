package com.example.reservaya;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorialReservaAdapter extends RecyclerView.Adapter<HistorialReservaAdapter.HistorialReservaViewHolder> {
    private List<HistorialReserva> items;
    private HistorialReservaAdapter.OnClickListener onClickListener;


    public static class HistorialReservaViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        //public ImageView imagenCancha;
        //public TextView nombreCancha;
        public TextView nombreComplejo;
        public TextView capacidad;
        public TextView techada;
        public TextView fechaReserva;

        public TextView costo;



        public Button reservar;



        public HistorialReservaViewHolder(View v) {
            super(v);
            //imagen = (ImageView) v.findViewById(R.id.imagen_complejo);

            nombreComplejo = (TextView) v.findViewById(R.id.tv_nombre_complejo);
            capacidad = (TextView) v.findViewById(R.id.tv_capacidad);
            techada = (TextView) v.findViewById(R.id.tv_techada);
            fechaReserva = (TextView) v.findViewById(R.id.tv_fecha_reserva);
            costo = (TextView) v.findViewById(R.id.tv_costo);


        }
    }

    public HistorialReservaAdapter(List<HistorialReserva> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public HistorialReservaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.historial_aficionado_carview, viewGroup, false);
        return new HistorialReservaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistorialReservaViewHolder viewHolder, int i) {
 //       viewHolder.imagen.setImageResource(items.get(i).getImagen_complejo());
        String techo = "";
        if (items.get(i).getTechada().equals("1")) {
            techo = "Si";
        } else {
            techo = "No";
        }
        Log.i("logReserva", "llegue al adapter");
        viewHolder.nombreComplejo.setText(items.get(i).getNombreComplejo());
        viewHolder.capacidad.setText("Cancha de " + items.get(i).getCapacidad());
        viewHolder.techada.setText("Techada: " + techo);
        viewHolder.fechaReserva.setText("Reservada el " +items.get(i).getFechaReserva() + " a las " + items.get(i).getHoraReserva() + " hs.");
        viewHolder.costo.setText("$" + items.get(i).getCosto());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(i, items.get(i));
                }
            }
        });

    }

    public void setOnClickListener(HistorialReservaAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, HistorialReserva model);
    }


}
