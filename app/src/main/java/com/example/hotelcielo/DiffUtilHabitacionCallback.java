// DiffUtilHabitacionCallback.java
package com.example.hotelcielo;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffUtilHabitacionCallback extends DiffUtil.Callback {

    private final List<Habitacion> oldList;
    private final List<Habitacion> newList;

    public DiffUtilHabitacionCallback(List<Habitacion> oldList, List<Habitacion> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Define cómo identificar si dos items son el mismo
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Define cómo comparar el contenido de los items
        Habitacion oldHabitacion = oldList.get(oldItemPosition);
        Habitacion newHabitacion = newList.get(newItemPosition);
        return oldHabitacion.equals(newHabitacion);
    }
}
