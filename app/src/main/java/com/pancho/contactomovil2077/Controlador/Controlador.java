package com.pancho.contactomovil2077.Controlador;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ValueEventListener;
import com.pancho.contactomovil2077.Modelo.UsuarioModel;

public class Controlador {

    private FirebaseAuth mAuth;
    private FirebaseManager firebaseManager;

    public interface RegistroListener {
        void onRegistroExitoso();
        void onRegistroFallido(String mensaje);
    }

    public Controlador() {
        mAuth = FirebaseManager.getFirebaseAuthInstance();
        firebaseManager = new FirebaseManager();
    }
    public interface AuthListener {
        void onAuthSuccess(FirebaseUser user);
        void onAuthFailure(String message);
    }



    public void registrarUsuario(String correo, String contrasena, final String nombre, final String apellido, final String nombreUsuario, final RegistroListener registroListener) {
        // Validar que el correo y la contraseña no estén vacíos
        if (TextUtils.isEmpty(correo) || TextUtils.isEmpty(contrasena)) {
            // Notificar al listener que el registro falló
            registroListener.onRegistroFallido("Correo o contraseña vacíos");
            return;
        }

        mAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso, actualiza el perfil del usuario con el nombre
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Utiliza push() para generar una clave única
                            String usuarioId = FirebaseManager.getDatabaseReference().child("Usuario").push().getKey();

                            // Crea un objeto UsuarioModel y guárdalo en la base de datos con la clave generada
                            UsuarioModel nuevoUsuario = new UsuarioModel(nombre, apellido, correo, "", nombreUsuario);
                            FirebaseManager.guardarUsuarioEnFirebase(usuarioId, nuevoUsuario);

                            // Notificar al listener que el registro fue exitoso
                            registroListener.onRegistroExitoso();
                        }
                    } else {
                        // Fallo en el registro, notificar al listener con el mensaje de error
                        Exception exception = task.getException();
                        if (exception != null) {
                            registroListener.onRegistroFallido("Error en el registro: " + exception.getMessage());
                        }
                    }
                });
    }


    public void iniciarSesion(String nombreUsuario, String contrasena, final AuthListener authListener) {
        mAuth.signInWithEmailAndPassword(nombreUsuario, contrasena)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (authListener != null) {
                            authListener.onAuthSuccess(user);
                        }
                    } else {
                        // Fallo en el inicio de sesión, notificar al listener con el mensaje de error
                        if (authListener != null) {
                            authListener.onAuthFailure(task.getException().getMessage());
                        }
                    }
                });
    }

    public void cargarUsuario(String uid, ValueEventListener valueEventListener) {
        firebaseManager.cargarUsuarioDesdeFirebase(uid, valueEventListener);
    }
}
