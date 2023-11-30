package com.pancho.contactomovil2077.Vista;

import static com.pancho.contactomovil2077.Controlador.FirebaseManager.mAuth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.pancho.contactomovil2077.Controlador.Controlador;
import com.pancho.contactomovil2077.Modelo.UsuarioModel;
import com.pancho.contactomovil2077.R;

public class Usuario extends AppCompatActivity {

    private EditText txtNombre, txtApellido, txtCorreo, txtPass, txtUsuario;
    private Button btnRegistro, btnLogin;

    private Controlador controlador;
    public Usuario() {
        // Puedes dejarlo vacío o inicializar algunos valores aquí si es necesario
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        // Obtener referencias a los componentes de la interfaz de usuario
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtPass = findViewById(R.id.txtPass);
        txtUsuario = findViewById(R.id.txtUsuario);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnLogin = findViewById(R.id.btnLogin);

        // Inicializar el controlador
        controlador = new Controlador();

        // Asignar listeners a los botones
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });
    }

    private void registrarUsuario() {
        // Obtener valores de los campos de texto
        String nombre = txtNombre.getText().toString();
        String apellido = txtApellido.getText().toString();
        String correo = txtCorreo.getText().toString().trim(); // Trim para eliminar espacios adicionales
        String contrasena = txtPass.getText().toString();
        String nombreUsuario = txtUsuario.getText().toString();

        // Validar el formato del correo electrónico
        if (TextUtils.isEmpty(correo) || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            // El correo electrónico no es válido, muestra un mensaje de error
            Toast.makeText(Usuario.this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
            return;
        }
        // Crear instancia de UsuarioModel
        UsuarioModel nuevoUsuario = new UsuarioModel(nombre, apellido, correo, "", nombreUsuario);

        // Registrar nuevo usuario en Firebase
        controlador.registrarUsuario(correo, contrasena, nombre, apellido, nombreUsuario, new Controlador.RegistroListener() {
            @Override
            public void onRegistroExitoso() {
                // Manejar el registro exitoso, si es necesario
                Toast.makeText(Usuario.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRegistroFallido(String mensaje) {
                // Manejar el fallo en el registro, si es necesario
                Log.e("RegistroFallido", mensaje);
                if (mensaje.contains("email address is already in use")) {
                    Toast.makeText(Usuario.this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Usuario.this, "Error en el registro. Consulta el LogCat para más detalles.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void iniciarSesion() {
        // Obtener valores de los campos de texto
        String nombreUsuario = txtUsuario.getText().toString().trim();
        String contrasena = txtPass.getText().toString().trim();

        // Validar que se ingresaron datos para iniciar sesión
        if (TextUtils.isEmpty(nombreUsuario) || TextUtils.isEmpty(contrasena)) {
            // Mostrar un mensaje de error
            Toast.makeText(Usuario.this, "Ingresa nombre de usuario y contraseña para iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el formato del correo electrónico
        if (!Patterns.EMAIL_ADDRESS.matcher(nombreUsuario).matches()) {
            // El correo electrónico no es válido, muestra un mensaje de error
            Toast.makeText(Usuario.this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Autenticar al usuario en Firebase
        mAuth.signInWithEmailAndPassword(nombreUsuario, contrasena)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso, abrir MainActivity
                        abrirMainActivity();
                    } else {
                        // Fallo en el inicio de sesión, mostrar mensaje de error
                        Toast.makeText(Usuario.this, "Error en el inicio de sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

