package com.pancho.contactomovil2077.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pancho.contactomovil2077.R;

public class Usuario extends AppCompatActivity {

    private EditText txtNombre, txtApellido, txtCorreo, txtPass, txtUsuario;
    private Button btnRegistro, btnLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        // Inicializar FirebaseAuth y DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Obtener referencias a los componentes de la interfaz de usuario
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtPass = findViewById(R.id.txtPass);
        txtUsuario = findViewById(R.id.txtUsuario);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnLogin = findViewById(R.id.btnLogin);

        // Asignar listeners a los botones
        btnRegistro.setOnClickListener(view -> registrarUsuario());

        btnLogin.setOnClickListener(view -> iniciarSesion());
    }

    private void registrarUsuario() {
        // Obtener valores de los campos de texto
        String nombre = txtNombre.getText().toString();
        String apellido = txtApellido.getText().toString();
        String correo = txtCorreo.getText().toString().trim();
        String contrasena = txtPass.getText().toString();
        String nombreUsuario = txtUsuario.getText().toString();

        // Validar los campos
        if (camposRegistroSonValidos(nombre, apellido, correo, contrasena, nombreUsuario)) {
            // Registrar nuevo usuario en Firebase
            mAuth.createUserWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Crear un nuevo nodo en la base de datos para el usuario
                                DatabaseReference usuarioRef = mDatabase.child("Usuario").child(user.getUid());

                                // Almacenar la información del usuario en la base de datos
                                usuarioRef.child("nombre").setValue(nombre);
                                usuarioRef.child("apellido").setValue(apellido);
                                usuarioRef.child("correo").setValue(correo);
                                usuarioRef.child("nombreUsuario").setValue(nombreUsuario);

                                // Manejar el registro exitoso, si es necesario
                                Toast.makeText(Usuario.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                Log.e("RegistroFallido", "Error en el registro: " + exception.getMessage());
                                if (exception.getMessage() != null && exception.getMessage().contains("email address is already in use")) {
                                    Toast.makeText(Usuario.this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Usuario.this, "Error en el registro. Consulta el LogCat para más detalles.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }

    private boolean camposRegistroSonValidos(String nombre, String apellido, String correo, String contrasena, String nombreUsuario) {
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(contrasena) || TextUtils.isEmpty(nombreUsuario)) {
            // Mostrar un mensaje de error si algún campo está vacío
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar el formato del correo electrónico
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            // El correo electrónico no es válido, muestra un mensaje de error
            Toast.makeText(this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void iniciarSesion() {
        // Obtener valores de los campos de texto
        String correo = txtCorreo.getText().toString().trim();
        String contrasena = txtPass.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(Usuario.this, ListaContactosActivity.class));
                            Toast.makeText(Usuario.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Usuario.this, "Perfil no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Usuario.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void abrirMainActivity() {
        // Aquí puedes iniciar la actividad principal (MainActivity)
        Intent intent = new Intent(Usuario.this, MainActivity.class);
        startActivity(intent);
        finish(); // Esto evita que el usuario regrese a la actividad de inicio de sesión presionando el botón "Atrás"
    }

}
