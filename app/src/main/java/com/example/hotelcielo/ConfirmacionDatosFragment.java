package com.example.hotelcielo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelcielo.databinding.FragmentConfirmacionDatosBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.HashMap;
import java.util.Map;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmacionDatosFragment extends Fragment {
    private TextView textViewNumeroHabitacion;
    private ImageView imageViewHabitacion;
    private FragmentConfirmacionDatosBinding binding;
    private String numeroHabitacion;
    private String urlImagen;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmacion_datos, container, false);

        // Inicializa los elementos de la vista
        textViewNumeroHabitacion = view.findViewById(R.id.lblreservandohabitacion);
        imageViewHabitacion = view.findViewById(R.id.imagenhabitacion);

        // Obtén los argumentos
        if (getArguments() != null) {
            numeroHabitacion = getArguments().getString("numeroHabitacion");
            urlImagen = getArguments().getString("urlImagen");

            // Mostrar el número de habitación y la imagen
            textViewNumeroHabitacion.setText("Reservando Habitación: " + numeroHabitacion);
            Glide.with(this).load(urlImagen).into(imageViewHabitacion);
        }
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Obtener referencias a los campos de texto
        EditText txtNombre = view.findViewById(R.id.txtnombre);
        EditText txtCorreo = view.findViewById(R.id.txtcorreo);
        EditText txtDireccion = view.findViewById(R.id.txtdireccion);
        EditText txtCodigoPostal = view.findViewById(R.id.txtcodigopostal);
        EditText txtPais = view.findViewById(R.id.txtpais);
        EditText txtTelefono = view.findViewById(R.id.txtnumerotelefonico);
        Button btnGuardarDatos = view.findViewById(R.id.btnguardardatos);
        // Obtener correo de los proveedores de autenticación
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = null;
            String nombreUsuario = user.getDisplayName();  // Obtener el nombre del usuario
            if (nombreUsuario != null) {
                txtNombre.setText(nombreUsuario);  // Completar el nombre en el campo
            }
            for (UserInfo profile : user.getProviderData()) {
                String providerId = profile.getProviderId();
                String providerEmail = profile.getEmail();

                // Priorizar el correo de Google si está disponible
                if (providerEmail != null) {
                    if (providerId.equals("google.com")) {
                        email = providerEmail;
                        break;  // Salir si es de Google
                    } else if (providerId.equals("firebase")) {
                        email = providerEmail;
                    }
                }
            }

            // Mostrar correo en el campo si se encuentra uno válido
            if (email != null) {
                txtCorreo.setText(email);  // Completar el correo en el campo
                Log.d("UserInfo", "Correo encontrado: " + email);
            } else {
                Log.d("UserInfo", "No se encontró correo electrónico.");
            }
        }
        btnGuardarDatos.setOnClickListener(v -> {
            // Recoger datos de los EditText
            String nombre = txtNombre.getText().toString().trim();
            String correo = txtCorreo.getText().toString().trim();
            String direccion = txtDireccion.getText().toString().trim();
            String codigoPostalStr = txtCodigoPostal.getText().toString().trim();
            String pais = txtPais.getText().toString().trim();
            String telefono = txtTelefono.getText().toString().trim();
            // Validar campos
            if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(direccion) ||
                    TextUtils.isEmpty(codigoPostalStr) || TextUtils.isEmpty(pais)) {
                Toast.makeText(getActivity(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            // Convertir el código postal a entero
            int codigoPostal = Integer.parseInt(codigoPostalStr);

            // Crear el objeto Cliente
            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setEmail(correo);
            cliente.setDireccion(direccion);
            cliente.setCodigoPostal(codigoPostal);
            cliente.setPais(pais);
            cliente.setTelefono(telefono);
            // Llamar a la función para guardar los datos
            guardarDatosEnBaseDeDatos(cliente);
        });
    }

    private void guardarDatosEnBaseDeDatos(Cliente cliente) {
        // URL de tu script PHP
        String url = "http://192.168.56.1:80/hotel/guardar_cliente.php";

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if (status.equals("success")) {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                VistaPreviaReservaFragment VistaPreviaReservaFragment = new VistaPreviaReservaFragment();
                                // Aquí puedes navegar a otro fragmento si es necesario
                                Bundle args = new Bundle();
                                args.putString("numeroHabitacion", numeroHabitacion);
                                args.putString("urlImagen", urlImagen);
                                args.putString("email", cliente.getEmail());
                                VistaPreviaReservaFragment.setArguments(args);
                                // Realiza la transacción de fragmento
                                getParentFragmentManager().beginTransaction()
                                        .replace(R.id.contenedorFragment, VistaPreviaReservaFragment)  // R.id.contenedorPrincipal es el contenedor donde cargas el fragmento
                                        .addToBackStack(null)  // Permite volver al fragmento anterior al presionar "Atrás"
                                        .commit();
                            } else {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", cliente.getNombre());
                params.put("email", cliente.getEmail());
                params.put("direccion", cliente.getDireccion());
                params.put("codigoPostal", String.valueOf(cliente.getCodigoPostal()));
                params.put("pais", cliente.getPais());
                params.put("telefono", cliente.getTelefono());
                return params;
            }
        };
        // Añadir la solicitud a la cola
        queue.add(stringRequest);
    }
}
