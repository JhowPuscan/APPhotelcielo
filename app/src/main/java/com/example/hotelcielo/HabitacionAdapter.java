// HabitacionAdapter.java
package com.example.hotelcielo;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelcielo.databinding.ItemHabitacionBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HabitacionAdapter extends RecyclerView.Adapter<HabitacionAdapter.HabitacionViewHolder> {

    // Definimos una interfaz para manejar los clics
    public interface OnItemClickListener {
        void onItemClick(Habitacion habitacion);
    }

    private List<Habitacion> habitaciones;
    private OnItemClickListener listener;

    public HabitacionAdapter(List<Habitacion> habitaciones, OnItemClickListener listener) {
        this.habitaciones = habitaciones;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HabitacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemHabitacionBinding binding = ItemHabitacionBinding.inflate(inflater, parent, false);
        return new HabitacionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitacionViewHolder holder, int position) {
        Habitacion habitacion = habitaciones.get(position);
        holder.bind(habitacion, listener);
    }

    @Override
    public int getItemCount() {
        return habitaciones.size();
    }

    public void setHabitaciones(List<Habitacion> nuevasHabitaciones) {
        DiffUtilHabitacionCallback diffCallback = new DiffUtilHabitacionCallback(this.habitaciones, nuevasHabitaciones);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.habitaciones.clear();
        this.habitaciones.addAll(nuevasHabitaciones);
        diffResult.dispatchUpdatesTo(this);
    }

    // Clase ViewHolder
    static class HabitacionViewHolder extends RecyclerView.ViewHolder {
        private final ItemHabitacionBinding binding;

        public HabitacionViewHolder(ItemHabitacionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Habitacion habitacion, OnItemClickListener listener) {
            binding.textViewNumeroHabitacion.setText("Habitación: " + habitacion.getNumeroHabitacion());
            binding.textViewCategoria.setText("Categoría: " + habitacion.getCategoria());
            binding.textViewCapacidad.setText("Capacidad: " + habitacion.getCapacidadMaxima());
            binding.textViewPrecio.setText("Precio por Noche: S/" + habitacion.getPrecioPorNoche());
            Glide.with(binding.imageViewHabitacion.getContext())
                    .load(habitacion.getUrlimagen())
                    .into(binding.imageViewHabitacion);
            if (habitacion.isDisponibilidad()) {
                binding.textViewDisponibilidad.setText("Disponible");
                binding.textViewDisponibilidad.setTextColor(binding.getRoot().getResources().getColor(R.color.green));
            } else {
                binding.textViewDisponibilidad.setText("No Disponible");
                binding.textViewDisponibilidad.setTextColor(binding.getRoot().getResources().getColor(R.color.red));
            }

            // Para Manejar el clic en el item completo
            binding.getRoot().setOnClickListener(v -> listener.onItemClick(habitacion));

            // Para manejar el clic en el botón de reservar
            binding.buttonReservar.setOnClickListener(v -> {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null && !currentUser.isAnonymous()) {
                    // Usuario registrado, permitir la reserva
                    listener.onItemClick(habitacion);
                } else {
                    // Usuario no registrado o anónimo, mostrar mensaje de error
                    Toast.makeText(binding.getRoot().getContext(), "Debes iniciar sesión con una cuenta para reservar", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
