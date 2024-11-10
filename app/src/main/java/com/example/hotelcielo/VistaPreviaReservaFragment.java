package com.example.hotelcielo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VistaPreviaReservaFragment extends Fragment {

    private ImageView imageViewHabitacion;
    private TextView lblNumeroHabitacion, lblCapacidadMaxima, lblCategoria, lblTotalPago, lblEmail, lblNombre, lblDireccion, lblTelefono, lblPais, lblCodigoPostal, lblFechaInicio, lblFechaFin, lblmonto_total;
    private int cantidadNoches;
    private String fechaInicio, fechaFin;
    private String Clienteid, habitacion_id;
    private double monto_total;
    private Button btnCoordinarPago;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_vista_previa_reserva, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("BusquedaPreferences", Context.MODE_PRIVATE);
        btnCoordinarPago = view.findViewById(R.id.btncoordinarpago);
        // Recupera los valores, proporcionando un valor predeterminado en caso de que no existan
        fechaInicio = sharedPreferences.getString("fechaInicio", null);
        fechaFin = sharedPreferences.getString("fechaFin", null);
        // Usa las fechas (por ejemplo, mostrando un mensaje si existen)
        if (fechaInicio != null && fechaFin != null) {
            // Muestra las fechas o realiza alguna acción
            Toast.makeText(requireContext(), "Fecha Inicio: " + fechaInicio + ", Fecha Fin: " + fechaFin, Toast.LENGTH_SHORT).show();
        } else {
            // No hay fechas guardadas
            Toast.makeText(requireContext(), "No hay fechas guardadas.", Toast.LENGTH_SHORT).show();
        }
        cantidadNoches = calcularCantidadNoches(fechaInicio, fechaFin);
        Log.d("cantidadnoches", "cantidadnoches: " + cantidadNoches);
        // Referenciar componentes del layout
        imageViewHabitacion = view.findViewById(R.id.imageView7);
        lblNumeroHabitacion = view.findViewById(R.id.lblnumerohabitacion);
        lblCapacidadMaxima = view.findViewById(R.id.lblcapacidadmaxima);
        lblCategoria = view.findViewById(R.id.lblcategoria);
        lblTotalPago = view.findViewById(R.id.lbltotalpago);
        lblEmail = view.findViewById(R.id.lblemail);
        lblNombre = view.findViewById(R.id.lblnombre);
        lblDireccion = view.findViewById(R.id.lbldireccion);
        lblTelefono = view.findViewById(R.id.lbltelefono);
        lblPais = view.findViewById(R.id.lblpais);
        lblCodigoPostal = view.findViewById(R.id.codigopostal);
        lblFechaInicio = view.findViewById(R.id.lblfechainicio);
        lblFechaFin = view.findViewById(R.id.lblfechafin);
        lblmonto_total = view.findViewById(R.id.lbltotalpago);
        // Obtener datos del Bundle
        if (getArguments() != null) {
            String numeroHabitacion = getArguments().getString("numeroHabitacion");
            String urlImagen = getArguments().getString("urlImagen");
            String email = getArguments().getString("email");

            // Asignar datos a los TextViews
            lblNumeroHabitacion.setText("Número de habitación: " + numeroHabitacion);
            lblEmail.setText("Email: " + email);

            // Cargar imagen con Glide
            Glide.with(this)
                    .load(urlImagen)
                    .into(imageViewHabitacion);
            obtenerDatos(numeroHabitacion, email);
            btnCoordinarPago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Construir el mensaje con los detalles de la reserva
                    String mensaje = "Hola, estoy interesado en coordinar el pago de mi reserva en Hotel Cielo.\n" +
                            "Detalles de la Reserva:\n" +
                            "Nombre: " + lblNombre.getText().toString() + "\n" +
                            "Habitación: " + lblNumeroHabitacion.getText().toString() + "\n" +
                            "Fecha de Inicio: " + fechaInicio + "\n" +
                            "Fecha de Fin: " + fechaFin + "\n" +
                            "Total a Pagar: S/" + monto_total + "\n" +
                            "Por favor, indíqueme los pasos para realizar el pago.";

                    // Llamar al método para enviar el mensaje a WhatsApp
                    enviarMensajeWhatsApp("+51930264826", mensaje);
                }
            });
        }
        return view;
    }
    private void obtenerDatos(String numero_Habitacion, String email) {
        // Asegúrate de agregar '?' antes de los parámetros
        String url = "http://192.168.56.1/hotel/obtenerdatos.php?numero_habitacion=" + numero_Habitacion + "&email=" + email;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // Comprobamos si hay un error
                            if (jsonObject.has("error")) {
                                Toast.makeText(getActivity(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Obtén los datos del cliente
                            JSONObject cliente = jsonObject.getJSONObject("cliente");
                            Clienteid = cliente.getString("id");
                            String nombre = cliente.getString("nombre");
                            String direccion = cliente.getString("direccion");
                            int codigoPostal = cliente.getInt("codigopostal");
                            String pais = cliente.getString("pais");
                            String telefono = cliente.getString("telefono");

                            // Obtén los datos de la habitación
                            JSONObject habitacion = jsonObject.getJSONObject("habitacion");
                            habitacion_id = habitacion.getString("id");
                            String categoria = habitacion.getString("categoria");
                            int capacidadMaxima = habitacion.getInt("capacidad_maxima");
                            double precioPorNoche = habitacion.getDouble("precio_por_noche");
                            String urlImagen = habitacion.getString("urlimagen");

                            // Llama al método para mostrar los datos en la UI
                            mostrarDatos(nombre, direccion, codigoPostal, pais, telefono, categoria, capacidadMaxima, precioPorNoche, urlImagen);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Añadir la solicitud a la cola
        requestQueue.add(stringRequest);
    }

    // Método para mostrar los datos en la UI
    private void mostrarDatos(String nombre, String direccion, int codigoPostal, String pais, String telefono,
                              String categoria, int capacidadMaxima, double precioPorNoche, String urlImagen) {
        // Actualiza tu UI con los datos obtenidos
        lblNombre.setText("Nombre: " + nombre);

        lblDireccion.setText("Dirección: " + direccion);

        lblCodigoPostal.setText("Código Postal: " + codigoPostal);

        lblPais.setText("País: " + pais);

        lblTelefono.setText("Teléfono: " + telefono);

        lblCategoria.setText("Categoría: " + categoria);

        lblCapacidadMaxima.setText("Capacidad máxima: " + capacidadMaxima);

        TextView lblmonto_total = getView().findViewById(R.id.lbltotalpago);
        monto_total=precioPorNoche * cantidadNoches;
        lblmonto_total.setText("Total a pagar: S/"+ monto_total); // Suponiendo que tienes la cantidad de noches
        lblFechaInicio.setText("Del: "+ fechaInicio);
        lblFechaFin.setText("hasta el: "+ fechaFin);
        ImageView imagenHabitacion = getView().findViewById(R.id.imageView7);
        Glide.with(getActivity()).load(urlImagen).into(imagenHabitacion);
    }
    private int calcularCantidadNoches(String fechaInicio, String fechaFin) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Convertir las fechas de String a Date
            Date inicio = sdf.parse(fechaInicio);
            Date fin = sdf.parse(fechaFin);

            // Calcular la diferencia en milisegundos
            long diferenciaEnMilisegundos = fin.getTime() - inicio.getTime();

            // Convertir la diferencia a días
            return (int) TimeUnit.DAYS.convert(diferenciaEnMilisegundos, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Retorna 0 en caso de error
        }
    }
    private void crearReserva() {
        // URL del archivo PHP
        String url = "http://192.168.56.1:80/hotel/crear_reserva.php";

        // Parámetros de la reserva a enviar
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "Reserva creada exitosamente.", Toast.LENGTH_SHORT).show();
                        // Aquí podrías redirigir a otra pantalla si es necesario
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error al crear reserva: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cliente_id", Clienteid);
                params.put("habitacion_id", habitacion_id);
                params.put("fecha_entrada", fechaInicio);
                params.put("fecha_salida", fechaFin);
                params.put("estado", "en proceso");
                params.put("monto_total", String.valueOf(monto_total));
                return params;
            }
        };

        // Añadir solicitud a la cola
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    // Llama a este método desde donde necesites, por ejemplo, al cargar el fragmento
    // obtenerDatos("numero_de_habitacion", "email_del_cliente");
    private void enviarMensajeWhatsApp(String numero, String mensaje) {
        try {
            String url = "https://wa.me/" + numero + "?text=" + Uri.encode(mensaje);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "WhatsApp no está instalado", Toast.LENGTH_SHORT).show();
        }
    }
}