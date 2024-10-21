package com.example.hotelcielo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etCorreo, etContrasena;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String SERVER_URL = "http://192.168.18.119:80/hotel/registro.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Aqui inicializamos el Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Aqui inicializamos Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        //Inicializamos los campos de texto
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);

        // Configurar Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Configurar botones
        findViewById(R.id.btnIniciarSesionGoogle).setOnClickListener(view -> signInWithGoogle());
        findViewById(R.id.btnIngresar).setOnClickListener(view -> loginWithEmail());
        findViewById(R.id.btnCrearCuenta).setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        findViewById(R.id.btnAnonymous).setOnClickListener(view -> loginAsGuest());
    }

    // Google Sign-In
    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                String nombre = account.getDisplayName(); // Nombre del usuario
                String correo = account.getEmail();       // Correo del usuario
                // Guardar el nombre y correo en Firestore o cualquier otro almacenamiento
                guardarDatosEnServidor(nombre, correo);
                Log.d(TAG, "Nombre: " + nombre + ", Email: " + correo);
            }
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            Log.w(TAG, "Google sign-in failed: " + e.getStatusCode());
            Toast.makeText(this, "Google sign-in failed: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Log.d(TAG, "Google sign-in successful: " + user.getUid());
                            navigateToSearch();
                        }
                    } else {
                        Log.w(TAG, "Firebase sign-in failed", task.getException());
                    }
                });
    }

    // Email and Password Authentication
    private void loginWithEmail() {
        String email = etCorreo.getText().toString();
        String password = etContrasena.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete ambos campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // Verificar si el correo está verificado
                            if (user.isEmailVerified()) {
                                Log.d(TAG, "Email sign-in successful: " + user.getUid());
                                navigateToSearch();
                            } else {
                                Log.w(TAG, "El correo electrónico no está verificado");
                                Toast.makeText(LoginActivity.this, "Por favor, verifica tu correo electrónico antes de iniciar sesión.", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Log.w(TAG, "Email sign-in failed", task.getException());
                        Toast.makeText(LoginActivity.this, "Inicio de sesión fallido: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Anonymous Login
    private void loginAsGuest() {
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Log.d(TAG, "Guest sign-in successful: " + user.getUid());
                            navigateToSearch();
                        }
                    } else {
                        Log.w(TAG, "Guest sign-in failed", task.getException());
                        Toast.makeText(LoginActivity.this, "Inicio de sesión como invitado fallido.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToSearch() {
        CardView cardViewLogin = findViewById(R.id.cardViewLogin);
        cardViewLogin.setVisibility(View.GONE);
        BuscarHabitacionesFragment buscarHabitacionesFragment = new BuscarHabitacionesFragment();
        FrameLayout contenedorFragment = findViewById(R.id.contenedorFragment);
        contenedorFragment.setVisibility(View.VISIBLE);
        // Usar el FragmentManager para iniciar el fragmento
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedorFragment, buscarHabitacionesFragment)
                .addToBackStack(null)
                .commit();
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
                            Toast.makeText(LoginActivity.this, "Datos guardados en el servidor.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VOLLEY", "Error en la solicitud: " + error.getMessage());
                    Toast.makeText(LoginActivity.this, "Error al conectar con el servidor.", Toast.LENGTH_SHORT).show();
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
}
