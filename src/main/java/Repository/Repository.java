package Repository;

import Criterios.CriteriaMysqlConverter;
import modelos.Producto;
import Criterios.Criteria;
//import repository.ProductRepository;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Repository implements IRepository {
    private List<Producto> productos;
    private Connection connection;

    public Repository() throws SQLException {
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        this.connection = DriverManager.getConnection(url, user, password);
        productos = new ArrayList<>();
    }

    @Override
    public List<Producto> all() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE 1=1")) {
            while (rs.next()) {
                productos.add(new Producto(rs.getString("name"), rs.getString("category"), rs.getDouble("price"), rs.getBoolean("available")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //todos los productos de una base de datos
        return this.productos;
    }

    @Override
    public List<Producto> matching(Criteria criteria) {
        // String querySql = new CriteriaMysqlConverter().convert(criteria); //aqui se forma la consulta segun la infraestructura de la base de datos
        // hacer la consulta sql y meter los productos a una lista
        //retornar la lista
        String querySql = new CriteriaMysqlConverter().convert(criteria); //aqui se forma la consulta segun la infraestructura de la base de datos
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySql)) {   //envio el query entregado por criteria
            while (rs.next()) {
                productos.add(new Producto(rs.getString("name"), rs.getString("category"), rs.getDouble("price"), rs.getBoolean("available")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.productos;
    }
}