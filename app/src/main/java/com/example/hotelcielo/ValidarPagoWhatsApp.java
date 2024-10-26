package com.example.hotelcielo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ValidarPagoWhatsApp extends AppCompatActivity {

    private Button buttonValidarPago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_pago);

        buttonValidarPago = findViewById(R.id.buttonValidarPago);

        buttonValidarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para validar pago en WhatsApp
                Toast.makeText(ValidarPagoWhatsApp.this, "Validando pago...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}