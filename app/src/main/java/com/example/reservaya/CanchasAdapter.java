package com.example.reservaya;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CanchasAdapter extends RecyclerView.Adapter<CanchasAdapter.CanchasViewHolder> {
    private List<Cancha> items;
    private CanchasAdapter.OnClickListener onClickListener;


    public static class CanchasViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        //public ImageView imagenCancha;
        //public TextView nombreCancha;

        public TextView capacidad;
        public TextView techada;

        public Button reservar;



        public CanchasViewHolder(View v) {
            super(v);
            //imagen = (ImageView) v.findViewById(R.id.imagen_complejo);
           // nombreCancha = (TextView) v.findViewById(R.id.nombre_cancha);

            capacidad = (TextView) v.findViewById(R.id.capacidad);
            techada = (TextView) v.findViewById(R.id.techada);
           // reservar = (Button) v.findViewById(R.id.reservar);



        }
    }

    public CanchasAdapter(List<Cancha> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public CanchasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.canchas_carview, viewGroup, false);
        return new CanchasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CanchasViewHolder viewHolder, int i) {
 //       viewHolder.imagen.setImageResource(items.get(i).getImagen_complejo());
        String techo = "";
        if (items.get(i).getTechada().equals("1")) {
            techo = "Si";
        } else {
            techo = "No";
        }

        viewHolder.capacidad.setText("Cancha de " + items.get(i).getCapacidad());
        viewHolder.techada.setText("Techada: " + techo);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(i, items.get(i));
                }
            }
        });

    }

    public void setOnClickListener(CanchasAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Cancha model);
    }


}
