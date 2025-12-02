package com.example.proyecto.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.R;
import com.example.proyecto.clases.Heroe;

import java.util.List;

public class HeroeAdaptador extends RecyclerView.Adapter<HeroeAdaptador.ViewHolder> {

    private Context context;
    private List<Heroe> datos;
    private RequestQueue queue;

    public HeroeAdaptador(Context context, List<Heroe> datos) {
        this.context = context;
        this.datos = datos;
        this.queue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public HeroeAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_character, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroeAdaptador.ViewHolder holder, int position) {
        Heroe h = datos.get(position);

        holder.characterName.setText(h.getNombre());
        holder.characterPower.setText("Poder: " + h.getPoder());
        holder.characterPublisher.setText("Publicador: " + h.getPublicador());
        holder.characterOrigin.setText("Origen: " + h.getOrigen());
        holder.characterComics.setText("Ocupacion: " + h.getComics());

        cargarImagenVolley(h.getImagen(), holder.characterThumbnail);
    }

    private void cargarImagenVolley(String url, ImageView imageView) {

        if (url == null || url.trim().isEmpty()) {
            return;
        }

        ImageRequest request = new ImageRequest(
                url,
                bitmap -> imageView.setImageBitmap(bitmap),
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.ARGB_8888,
                error -> {

                }
        );

        queue.add(request);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView characterThumbnail;
        TextView characterName, characterDescription;
        TextView characterPower, characterPublisher, characterOrigin, characterComics;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            characterThumbnail = itemView.findViewById(R.id.characterThumbnail);
            characterName = itemView.findViewById(R.id.characterName);
            characterPower = itemView.findViewById(R.id.characterPower);
            characterPublisher = itemView.findViewById(R.id.characterPublisher);
            characterOrigin = itemView.findViewById(R.id.characterOrigin);
            characterComics = itemView.findViewById(R.id.characterComics);
        }
    }
}
