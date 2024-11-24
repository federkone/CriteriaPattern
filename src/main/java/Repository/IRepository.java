package Repository;
import Criteria.Criteria;
import Models.Producto;

import java.util.List;

public interface IRepository {
    List<Producto> all(); // Retorna todos los productos
    List<Producto> matching(Criteria criteria); // Filtra productos según los criterios
    void insertData(Producto producto); // Inserta un producto en la base de datos
    void updateData (Producto producto); // Actualiza un producto en la base de datos
    void deleteData(Producto producto); // Elimina un producto de la base de datos
}