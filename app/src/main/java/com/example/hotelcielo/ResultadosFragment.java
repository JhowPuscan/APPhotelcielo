// ResultadosFragment.java
package com.example.hotelcielo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelcielo.databinding.FragmentResultadosBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ResultadosFragment extends Fragment {
    private Button buttonCerrarSesion;
    private RecyclerView recyclerView;
    private HabitacionAdapter habitacionAdapter;
    private ArrayList<Habitacion> habitaciones;
    private static final String TAG = "ResultadosFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resultados, container, false);
        // Inicializa el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerHabitacionesResultados);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        habitacionAdapter = new HabitacionAdapter(new ArrayList<>(), habitacion -> {
            // Muestra el fragmento de confirmación de datos
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            // Crear una nueva instancia del fragmento de confirmación
            ConfirmacionDatosFragment confirmacionDatosFragment = new ConfirmacionDatosFragment();
            Bundle args = new Bundle();
            args.putString("numeroHabitacion", habitacion.getNumeroHabitacion());
            args.putString("urlImagen", habitacion.getUrlimagen());
            confirmacionDatosFragment.setArguments(args);
            // Reemplazar el fragment actual con el de confirmación de datos
            transaction.replace(R.id.contenedorFragment, confirmacionDatosFragment);
            transaction.addToBackStack(null); // Permite volver atrás
            transaction.commit();
        });
        recyclerView.setAdapter(habitacionAdapter);

        // Añadir divisores entre items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Configurar el botón de cierre de sesión
        buttonCerrarSesion = view.findViewById(R.id.buttonCerrarSesion);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.isAnonymous()) {
            // Mostrar el botón solo si el usuario es anónimo
            buttonCerrarSesion.setVisibility(View.VISIBLE);
            buttonCerrarSesion.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                // Regresar a LoginActivity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish(); // Cierra la actividad actual para evitar regresar
            });
        }
        // Obtén la lista de habitaciones desde los argumentos
        if (getArguments() != null) {
            habitaciones = getArguments().getParcelableArrayList("habitaciones");
            if (habitaciones != null) {
                habitacionAdapter.setHabitaciones(habitaciones);
            }
        }

        return view;
    }
}
