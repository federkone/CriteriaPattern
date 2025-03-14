package Repository;

import Criteria.CriteriaMysqlConverter;
import Criteria.Filter;
import Repository.HandleConections.ConnectionDbSqlNative;
import Models.Producto;
import Criteria.Criteria;
//import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

//Aqui doy uso de SQL para realizar consultas a la base de datos con filtros, orden, límite y desplazamiento
//segun mi clase Criteria/criterios para ello doy uso del CriteriaMysqlConverter para convertir a una consulta sql
public class RepositorySql implements IRepository {
    private final Connection connection;

    public RepositorySql() throws SQLException {
        this.connection = new ConnectionDbSqlNative().getConnection(); //conectar a la base de datos
    }

    @Override
    public List<Producto> all() {
        String querySql = "SELECT * FROM products";
        return getProductos(querySql, Criteria.create());
    }

    @Override
    public List<Producto> matching(Criteria criteria) {
        String CriteriaQuery = new CriteriaMysqlConverter("products").convert(criteria); //aqui se forma el formato de consulta
        return getProductos(CriteriaQuery, criteria);
    }

    private List<Producto> getProductos(String querySql, Criteria criteria) {
        List<Producto> productos = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(querySql)) {
            int index = 1;  // El índice de los parámetros es 1-based
            for (Filter filter : criteria.getFilters()) {
                statement.setObject(index++, filter.getValue());
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    productos.add(new Producto(
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getBoolean("available")
                    ));
                }
            }
        } catch (SQLException e) {
            // Manejo de excepciones de SQL
            e.printStackTrace();
        }

        return productos;
    }


    public void insertData(Producto producto) {
        String sql = "INSERT INTO products (name, category, price, available) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, producto.getName());
            statement.setString(2, producto.getCategory());
            statement.setDouble(3, producto.getPrice());
            statement.setBoolean(4, producto.isAvailable());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Producto insertado exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el producto: " + e.getMessage());
        }
    }

    @Override
    public void deleteData(Producto producto) {
        String sql = "DELETE FROM products WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, producto.getName());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Producto eliminado exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el producto: " + e.getMessage());
        }
    }

    @Override
    public void updateData(Producto producto) {
        String sql = "UPDATE products SET category = ?, price = ?, available = ? WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, producto.getCategory());
            statement.setDouble(2, producto.getPrice());
            statement.setBoolean(3, producto.isAvailable());
            statement.setString(4, producto.getName());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Producto actualizado exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
        }
    }
}