package com.pancho.contactomovil2077.Vista;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pancho.contactomovil2077.Modelo.Contacto;
import com.pancho.contactomovil2077.R;

import java.util.List;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ContactoViewHolder> {

    private List<Contacto> listaDeContactos;
    private OnContactoClickListener onContactoClickListener;

    public ContactosAdapter(List<Contacto> listaDeContactos, OnContactoClickListener onContactoClickListener) {
        this.listaDeContactos = listaDeContactos;
        this.onContactoClickListener = onContactoClickListener;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.view.View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        Contacto contacto = listaDeContactos.get(position);
        holder.textNombre.setText(contacto.getNombre());
        holder.textCorreo.setText(contacto.getCorreo());

        // Manejar clics en elementos de la lista
        holder.itemView.setOnClickListener(v -> {
            if (onContactoClickListener != null) {
                onContactoClickListener.onContactoClick(contacto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDeContactos.size();
    }

    // Interfaz para manejar clics en elementos de la lista
    public interface OnContactoClickListener {
        void onContactoClick(Contacto contacto);
    }

    // ViewHolder y otros métodos permanecen sin cambios
    public static class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textCorreo;

        public ContactoViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombre);
            textCorreo = itemView.findViewById(R.id.textCorreo);
        }
    }
}
