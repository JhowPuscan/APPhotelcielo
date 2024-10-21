package com.example.hotelcielo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ReservaActivity extends AppCompatActivity {

    private EditText editTextFechaEntrada, editTextFechaSalida, editTextNoches, editTextAdultos, editTextNinos;
    private Button buttonReservar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        editTextFechaEntrada = findViewById(R.id.editTextFechaEntrada);
        editTextFechaSalida = findViewById(R.id.editTextFechaSalida);
        editTextNoches = findViewById(R.id.editTextNoches);
        editTextAdultos = findViewById(R.id.editTextAdultos);
        editTextNinos = findViewById(R.id.editTextNinos);
        buttonReservar = findViewById(R.id.buttonReservar);

        db = FirebaseFirestore.getInstance();

        buttonReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fechaEntrada = editTextFechaEntrada.getText().toString();
                String fechaSalida = editTextFechaSalida.getText().toString();
                String noches = editTextNoches.getText().toString();
                String adultos = editTextAdultos.getText().toString();
                String ninos = editTextNinos.getText().toString();

                // Lógica para reservar habitación
                Toast.makeText(ReservaActivity.this, "Reservando habitación...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}