package com.example.hotelcielo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class VistaPreviaReservaFragment extends Fragment {

    private TextView textViewFechaEntrada, textViewFechaSalida, textViewNoches, textViewAdultos, textViewNinos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_previa_reserva, container, false);

        textViewFechaEntrada = view.findViewById(R.id.textViewFechaEntrada);
        textViewFechaSalida = view.findViewById(R.id.textViewFechaSalida);
        textViewNoches = view.findViewById(R.id.textViewNoches);
        textViewAdultos = view.findViewById(R.id.textViewAdultos);
        textViewNinos = view.findViewById(R.id.textViewNinos);

        // Lógica para mostrar vista previa de reserva
        return view;
    }
}