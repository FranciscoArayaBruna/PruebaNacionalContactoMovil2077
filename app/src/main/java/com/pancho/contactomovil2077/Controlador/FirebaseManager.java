package com.pancho.contactomovil2077.Controlador;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseManager {

    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase;

    FirebaseManager() {
        // Constructor privado para evitar la creación de instancias
    }

    public static FirebaseAuth getFirebaseAuthInstance() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    public static DatabaseReference getDatabaseReference() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return mDatabase;
    }

    public static void guardarUsuarioEnFirebase(String uid, String nombre, String apellido, String correo, String nombreUsuario) {
        DatabaseReference usuarioRef = getDatabaseReference().child("Usuario").child(uid);
        usuarioRef.child("nombre").setValue(nombre);
        usuarioRef.child("apellido").setValue(apellido);
        usuarioRef.child("correo").setValue(correo);
        usuarioRef.child("nombreUsuario").setValue(nombreUsuario);
    }

    public static void cargarUsuarioDesdeFirebase(String uid, ValueEventListener valueEventListener) {
        DatabaseReference usuariosRef = getDatabaseReference().child("Usuario").child(uid);
        usuariosRef.addListenerForSingleValueEvent(valueEventListener);
    }
}
