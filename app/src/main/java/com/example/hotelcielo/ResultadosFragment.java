// ResultadosFragment.java
package com.example.hotelcielo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        });
        recyclerView.setAdapter(habitacionAdapter);

        // Añadir divisores entre items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

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
