package com.example.hotelcielo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.io.Serializable;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class BuscarHabitacionesFragment extends Fragment {
    private int año, mes, dia;
    private Button buttonIniciarCerrarSesion;
    private EditText editTextCheckIn, editTextCheckOut, editTextNoches, editTextAdultos, editTextNinos;
    private RadioGroup radioGroupReservas;
    private CheckBox checkBoxPresidenciales, checkBoxMatrimoniales, checkBoxFamiliar, checkBoxPequena;
    private Button buttonBuscar;
    private RecyclerView recyclerView;
    private HabitacionAdapter habitacionAdapter;

    // Variables para almacenar las fechas seleccionadas
    private Calendar checkInDate;
    private Calendar checkOutDate;

    // URL del script PHP en tu servidor
    private static final String URL_BUSCAR_HABITACIONES = "http://192.168.18.119:80/hotel/buscar_habitaciones.php";

    // RequestQueue de Volley
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscar_habitaciones, container, false);

        // Inicializa la fecha actual
        Calendar calendar = Calendar.getInstance();
        año = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        //Aqui inicializamos las variables de fecha, por defecto le pusimos null
        checkInDate = null;
        checkOutDate = null;

        //Aqui inicializamos la RequestQueue de Volley
        requestQueue = Volley.newRequestQueue(requireContext());

        // aqui estamos referenciando el botón de iniciar/cerrar sesión
        buttonIniciarCerrarSesion = view.findViewById(R.id.buttonIniciarCerrarSesion);

        //Aqui es donde verificamos si el usuario está autenticado, si no lo esta es porque significa que entro como visitante obviamente
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            // El usuario está autenticado, cambia el texto del botón a "Cerrar sesión"
            buttonIniciarCerrarSesion.setText("Cerrar sesión");
        } else {
            // El usuario no está autenticado, cambia el texto del botón a "Iniciar sesión"
            buttonIniciarCerrarSesion.setText("Iniciar sesión");
        }

        // Agui es donde cerramos la sesion de usuario gracias a signout
        buttonIniciarCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // El usuario está autenticado, cierra la sesión
                    firebaseAuth.signOut();
                    // Cambia el texto del botón a "Iniciar sesión"
                    buttonIniciarCerrarSesion.setText("Iniciar sesión");
                } else {
                    // El usuario no está autenticado, inicia la sesión
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Inicializa los elementos del layout
        editTextCheckIn = view.findViewById(R.id.editTextCheckIn);
        editTextCheckOut = view.findViewById(R.id.editTextCheckOut);
        editTextNoches = view.findViewById(R.id.editTextNoches);
        editTextAdultos = view.findViewById(R.id.editTextAdultos);
        editTextNinos = view.findViewById(R.id.editTextNinos);
        radioGroupReservas = view.findViewById(R.id.radioGroupReservas);
        checkBoxPresidenciales = view.findViewById(R.id.checkBoxPresidenciales);
        checkBoxMatrimoniales = view.findViewById(R.id.checkBoxMatrimoniales);
        checkBoxFamiliar = view.findViewById(R.id.checkBoxFamiliar);
        checkBoxPequena = view.findViewById(R.id.checkBoxPequena);
        buttonBuscar = view.findViewById(R.id.buttonBuscar);

        // Configura el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerHabitaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(habitacionAdapter);

        // Listener para el botón de búsqueda
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarHabitaciones();
            }
        });

        // Agrega un listener a los campos de fecha
        editTextCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerCheckIn();
            }
        });

        editTextCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerCheckOut();
            }
        });

        return view;
    }

    private void buscarHabitaciones() {
        int selectedId = radioGroupReservas.getCheckedRadioButtonId();
        // Variables para los filtros
        List<String> tiposHabitacion = new ArrayList<>();

        if (selectedId == R.id.radioSi) {
            // Si el RadioButton "Sí" está activado, permite la selección múltiple
            tiposHabitacion = obtenerTiposHabitacion();
        } else if (selectedId == R.id.radioNo) {
            // Si el RadioButton "No" está activado, selecciona un solo tipo de habitación
            tiposHabitacion = obtenerTiposHabitacion();

            // Verificar que sólo se haya seleccionado una categoría
            if (tiposHabitacion.size() > 1) {
                Toast.makeText(getContext(), "Solo puedes seleccionar una categoría de habitación", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        // Obtener los filtros seleccionados
        String checkIn = editTextCheckIn.getText().toString().trim();
        String checkOut = editTextCheckOut.getText().toString().trim();
        String noches = editTextNoches.getText().toString().trim();
        String adultos = editTextAdultos.getText().toString().trim();
        String ninos = editTextNinos.getText().toString().trim();

        // Validar los filtros obligatorios
        if (tiposHabitacion.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
            Toast.makeText(getContext(), "Por favor completa todos los filtros obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Toast.makeText(getContext(), "Tipos de habitación seleccionados: " + tiposHabitacion.toString(), Toast.LENGTH_SHORT).show();
        }

        // Obtener capacidad máxima basada en el número de adultos y niños
        int capacidadMaxima = 0;
        try {
            int numAdultos = Integer.parseInt(adultos);
            int numNinos = Integer.parseInt(ninos);
            capacidadMaxima = numAdultos + numNinos;
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Por favor ingresa números válidos para adultos y niños", Toast.LENGTH_SHORT).show();
            return;
        }
        String tiposHabitacionStr = tiposHabitacion.toString().replace("[", "").replace("]", "").replace(" ", "");
        // Preparar los parámetros para la solicitud
        String url = URL_BUSCAR_HABITACIONES
                + "?tiposHabitacion=" + tiposHabitacionStr
                + "&capacidadMaxima=" + capacidadMaxima
                + "&checkIn=" + checkIn
                + "&checkOut=" + checkOut;

        // Crear la solicitud de cadena (StringRequest)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsear la respuesta JSON
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                JSONArray habitacionesArray = jsonObject.getJSONArray("habitaciones");
                                List<Habitacion> habitaciones = new ArrayList<>();

                                for (int i = 0; i < habitacionesArray.length(); i++) {
                                    JSONObject habitacionObj = habitacionesArray.getJSONObject(i);
                                    Habitacion habitacion = new Habitacion(
                                            habitacionObj.getInt("id"),
                                            habitacionObj.getString("numeroHabitacion"),
                                            habitacionObj.getInt("capacidadMaxima"),
                                            habitacionObj.getString("categoria"),
                                            habitacionObj.getBoolean("disponibilidad"),
                                            habitacionObj.getDouble("precioPorNoche"),
                                            habitacionObj.getString("urlimagen")
                                    );
                                    habitaciones.add(habitacion);
                                }

                                // Navegar a ResultadosFragment pasando la lista de habitaciones
                                ResultadosFragment resultadosFragment = new ResultadosFragment();
                                Bundle bundle = new Bundle();
                                bundle.putParcelableArrayList("habitaciones", new ArrayList<>(habitaciones));
                                resultadosFragment.setArguments(bundle);

                                FragmentManager fragmentManager = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.contenedorFragment, resultadosFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                // Limpiar la lista si no se encontraron habitaciones
                                habitacionAdapter.setHabitaciones(new ArrayList<>());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        Toast.makeText(getContext(), "Error al realizar la búsqueda: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola
        requestQueue.add(stringRequest);
    }

    // Método para obtener el tipo de habitación basado en los checkboxes
    private List<String> obtenerTiposHabitacion() {
        List<String> tipos = new ArrayList<>();
        if (checkBoxPresidenciales.isChecked()) tipos.add("Presidencial");
        if (checkBoxMatrimoniales.isChecked()) tipos.add("Matrimonial");
        if (checkBoxFamiliar.isChecked()) tipos.add("Familiar");
        if (checkBoxPequena.isChecked()) tipos.add("Pequeña");
        return tipos;
    }

    // Método para mostrar el DatePicker para el campo de fecha de entrada
    private void showDatePickerCheckIn() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        checkInDate = Calendar.getInstance();
                        checkInDate.set(year, month, dayOfMonth, 0, 0, 0);
                        checkInDate.set(Calendar.MILLISECOND, 0);
                        editTextCheckIn.setText(formatDate(checkInDate));

                        // Si ya se seleccionó una fecha de salida, verifica que sea válida
                        if (checkOutDate != null) {
                            if (!checkOutDate.after(checkInDate)) {
                                editTextCheckOut.setText("");
                                editTextNoches.setText("");
                                checkOutDate = null;
                                Toast.makeText(getContext(), "La fecha de salida debe ser después de la fecha de entrada", Toast.LENGTH_SHORT).show();
                            } else {
                                calcularNoches();
                            }
                        }
                    }
                }, año, mes, dia);
        // Opcional: puedes establecer límites en el DatePicker si lo deseas
        datePickerDialog.show();
    }

    // Método para mostrar el DatePicker para el campo de fecha de salida
    private void showDatePickerCheckOut() {
        if (checkInDate == null) {
            Toast.makeText(getContext(), "Por favor selecciona primero la fecha de entrada", Toast.LENGTH_SHORT).show();
            return;
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedCheckOut = Calendar.getInstance();
                        selectedCheckOut.set(year, month, dayOfMonth, 0, 0, 0);
                        selectedCheckOut.set(Calendar.MILLISECOND, 0);

                        if (!selectedCheckOut.after(checkInDate)) {
                            Toast.makeText(getContext(), "La fecha de salida debe ser después de la fecha de entrada", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        checkOutDate = selectedCheckOut;
                        editTextCheckOut.setText(formatDate(checkOutDate));
                        calcularNoches();
                    }
                }, año, mes, dia);

        // Establece la fecha mínima como un día después de la fecha de entrada
        Calendar minDate = (Calendar) checkInDate.clone();
        minDate.add(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        // Opcional: puedes establecer un límite superior de fechas si lo deseas
        datePickerDialog.show();
    }

    // Método para formatear la fecha a String
    private String formatDate(Calendar date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date.getTime());
    }

    // Método para calcular el número de noches
    private void calcularNoches() {
        if (checkInDate != null && checkOutDate != null) {
            long diffInMillis = checkOutDate.getTimeInMillis() - checkInDate.getTimeInMillis();
            long noches = TimeUnit.MILLISECONDS.toDays(diffInMillis);
            editTextNoches.setText(String.valueOf(noches));
        }
    }
}
