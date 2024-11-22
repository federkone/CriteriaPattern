package modelos;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Producto {
    @Id
    @Column(name = "name")
    private String nombre;

    @Column(name = "category")
    private String categoria;

    @Column(name = "price")
    private double precio;

    @Column(name = "available")
    private boolean disponible;

    public  Producto(){}

    public Producto(String nombre, String categoria, double precio, boolean disponible) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.disponible = disponible;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", precio=" + precio +
                ", disponible=" + disponible +
                '}';
    }
}