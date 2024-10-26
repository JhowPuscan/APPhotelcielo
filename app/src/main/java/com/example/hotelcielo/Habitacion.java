// Habitacion.java
package com.example.hotelcielo;

import android.os.Parcel;
import android.os.Parcelable;

public class Habitacion implements Parcelable {
     private int id;
     private String numeroHabitacion;
     private int capacidadMaxima;
     private String categoria;
     private boolean disponibilidad;
     private double precioPorNoche;
     private String urlimagen;

     public Habitacion(int id, String numeroHabitacion, int capacidadMaxima, String categoria, boolean disponibilidad, double precioPorNoche, String urlimagen) {
          this.id = id;
          this.numeroHabitacion = numeroHabitacion;
          this.capacidadMaxima = capacidadMaxima;
          this.categoria = categoria;
          this.disponibilidad = disponibilidad;
          this.precioPorNoche = precioPorNoche;
          this.urlimagen = urlimagen;
     }

     public Habitacion() {
     }

     // Constructor que recibe un Parcel
     protected Habitacion(Parcel in) {
          id = in.readInt();
          numeroHabitacion = in.readString();
          capacidadMaxima = in.readInt();
          categoria = in.readString();
          disponibilidad = in.readByte() != 0;
          precioPorNoche = in.readDouble();
          urlimagen = in.readString();
     }

     public static final Creator<Habitacion> CREATOR = new Creator<Habitacion>() {
          @Override
          public Habitacion createFromParcel(Parcel in) {
               return new Habitacion(in);
          }

          @Override
          public Habitacion[] newArray(int size) {
               return new Habitacion[size];
          }
     };

     // Getters y Setters
     public int getId() {
          return id;
     }

     public String getNumeroHabitacion() {
          return numeroHabitacion;
     }

     public int getCapacidadMaxima() {
          return capacidadMaxima;
     }

     public String getCategoria() {
          return categoria;
     }

     public boolean isDisponibilidad() {
          return disponibilidad;
     }

     public double getPrecioPorNoche() {
          return precioPorNoche;
     }

     public void setId(int id) {
          this.id = id;
     }

     public void setNumeroHabitacion(String numeroHabitacion) {
          this.numeroHabitacion = numeroHabitacion;
     }

     public void setCapacidadMaxima(int capacidadMaxima) {
          this.capacidadMaxima = capacidadMaxima;
     }

     public void setCategoria(String categoria) {
          this.categoria = categoria;
     }

     public void setDisponibilidad(boolean disponibilidad) {
          this.disponibilidad = disponibilidad;
     }

     public void setPrecioPorNoche(double precioPorNoche) {
          this.precioPorNoche = precioPorNoche;
     }

     public String getUrlimagen() {
          return urlimagen;
     }

     public void setUrlimagen(String urlimagen) {
          this.urlimagen = urlimagen;
     }

     @Override
     public int describeContents() {
          return 0;
     }

     @Override
     public void writeToParcel(Parcel parcel, int flags) {
          parcel.writeInt(id);
          parcel.writeString(numeroHabitacion);
          parcel.writeInt(capacidadMaxima);
          parcel.writeString(categoria);
          parcel.writeByte((byte) (disponibilidad ? 1 : 0));
          parcel.writeDouble(precioPorNoche);
          parcel.writeString(urlimagen);
     }

     @Override
     public boolean equals(Object obj) {
          if (this == obj) return true;
          if (!(obj instanceof Habitacion)) return false;
          Habitacion other = (Habitacion) obj;
          return id == other.id &&
                  capacidadMaxima == other.capacidadMaxima &&
                  disponibilidad == other.disponibilidad &&
                  Double.compare(other.precioPorNoche, precioPorNoche) == 0 &&
                  numeroHabitacion.equals(other.numeroHabitacion) &&
                  categoria.equals(other.categoria) &&
                  urlimagen.equals(other.urlimagen);
     }

     @Override
     public int hashCode() {
          // Implementa un hashCode adecuado
          int result;
          long temp;
          result = id;
          result = 31 * result + numeroHabitacion.hashCode();
          result = 31 * result + capacidadMaxima;
          result = 31 * result + categoria.hashCode();
          result = 31 * result + (disponibilidad ? 1 : 0);
          temp = Double.doubleToLongBits(precioPorNoche);
          result = 31 * result + (int) (temp ^ (temp >>> 32));
          result = 31 * result + urlimagen.hashCode();
          return result;
     }
}
