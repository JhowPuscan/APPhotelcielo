package com.example.hotelcielo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Importaciones de Volley
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

// Importaciones de Firebase y Google Sign-In
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 9001;
    private static final String SERVER_URL = "http://192.168.18.119:80/hotel/registro.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Configurar Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Asegúrate de tener configurado esto en tu strings.xml
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Obtener referencias a los elementos de la interfaz
        final EditText etNombre = findViewById(R.id.etNombre);
        final EditText etCorreo = findViewById(R.id.etCorreo);
        final EditText etContrasena = findViewById(R.id.etContrasena);
        final EditText etConfirmContrasena = findViewById(R.id.etConfirmContrasena);
        Button btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        Button btnIniciarSesionGoogle = findViewById(R.id.btnIniciarSesionGoogle);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Establecer listener para el botón de crear cuenta
        btnCrearCuenta.setOnClickListener(view -> crearCuenta(etNombre, etCorreo, etContrasena, etConfirmContrasena));

        // Establecer listener para el botón de iniciar sesión con Google
        btnIniciarSesionGoogle.setOnClickListener(view -> signIn());

        // Establecer listener para el botón de login
        btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void crearCuenta(EditText etNombre, EditText etCorreo, EditText etContrasena, EditText etConfirmContrasena) {
        String nombre = etNombre.getText().toString().trim();
        String email = etCorreo.getText().toString().trim();
        String password = etContrasena.getText().toString().trim();
        String confirmPassword = etConfirmContrasena.getText().toString().trim();

        // Validaciones
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Log.w("TAG", "Todos los campos deben ser llenados");
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Log.w("TAG", "Las contraseñas no coinciden");
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear usuario con Firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "createUserWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        // Enviar correo de verificación
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Se ha enviado un correo de verificación. Por favor, verifícalo.", Toast.LENGTH_SHORT).show();

                                            // Guardar nombre y email en la base de datos
                                            guardarDatosEnServidor(nombre, email);

                                            // Navegar a la pantalla de inicio de sesión
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish(); // Cerrar esta actividad
                                        } else {
                                            Log.w("TAG", "Error al enviar el correo de verificación", task1.getException());
                                            Toast.makeText(RegisterActivity.this, "Error al enviar el correo de verificación.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarDatosEnServidor(String nombre, String email) {
        // Crear una nueva cola de solicitudes
        RequestQueue queue = Volley.newRequestQueue(this);

        // Crear la solicitud POST
        StringRequest postRequest = new StringRequest(Request.Method.POST, SERVER_URL,
                response -> {
                    Log.d("VOLLEY", "Respuesta del servidor: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        if (success) {
                            Toast.makeText(RegisterActivity.this, "Datos guardados en el servidor.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VOLLEY", "Error en la solicitud: " + error.getMessage());
                    Toast.makeText(RegisterActivity.this, "Error al conectar con el servidor.", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Parámetros que se enviarán al script PHP
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("email", email);
                return params;
            }
        };

        // Agregar la solicitud a la cola
        queue.add(postRequest);
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Iniciar sesión con Firebase
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Error al iniciar sesión con Google.", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Log.d("TAG", "signInWithCredential:success:" + user.getUid());

                        if (user != null) {
                            String nombre = user.getDisplayName();
                            String email = user.getEmail();
                            if (nombre != null && email != null) {
                                guardarDatosEnServidor(nombre, email);
                            }
                        }

                        // Navegar a la siguiente actividad
                        Intent intent = new Intent(RegisterActivity.this, BuscarHabitacionesFragment.class);
                        startActivity(intent);
                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Error al iniciar sesión con Google.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
