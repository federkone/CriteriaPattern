package Repository;
import Criterios.Criteria;
import modelos.Producto;

import java.util.List;

public interface IRepository {
    List<Producto> all(); // Retorna todos los productos
    List<Producto> matching(Criteria criteria); // Filtra productos según los criterios
}