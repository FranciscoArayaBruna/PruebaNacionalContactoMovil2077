package com.pancho.contactomovil2077.Vista;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pancho.contactomovil2077.Modelo.Contacto;
import com.pancho.contactomovil2077.R;

import java.util.ArrayList;
import java.util.List;

public class ListaContactosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        // Obtener la lista de contactos (puedes obtenerla de Firebase)
        List<Contacto> listaDeContactos = obtenerListaDeContactos();

        // Configurar el RecyclerView y asignar el adaptador
        configurarRecyclerView(listaDeContactos);
    }

    private List<Contacto> obtenerListaDeContactos() {
        // Ejemplo: Devolver una lista estática de contactos
        List<Contacto> contactos = new ArrayList<>();
        contactos.add(new Contacto("Nombre1", "correo1@example.com"));
        contactos.add(new Contacto("Nombre2", "correo2@example.com"));
        // ... Agrega más contactos según sea necesario
        return contactos;
    }

    private void configurarRecyclerView(List<Contacto> listaDeContactos) {
        RecyclerView recyclerView = findViewById(R.id.rvLista);
        ContactosAdapter adapter = new ContactosAdapter(listaDeContactos, new ContactosAdapter.OnContactoClickListener() {
            @Override
            public void onContactoClick(Contacto contacto) {
                // Manejar clic en un contacto (puede abrir un perfil de contacto)
                abrirPerfilContacto(contacto);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void abrirPerfilContacto(Contacto contacto) {
        // Implementa esta función para abrir el perfil del contacto
        // Puede ser una nueva actividad o fragmento que muestra detalles del contacto
        // También puedes implementar aquí la opción de agregar nuevos contactos
    }
}
