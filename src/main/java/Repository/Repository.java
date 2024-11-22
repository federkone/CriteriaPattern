package Repository;

import Criterios.CriteriaMysqlConverter;
import modelos.Producto;
import Criterios.Criteria;
//import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Repository implements IRepository {
    private List<Producto> productos;
    private Connection connection;

    public Repository() {
        this.connection = DriverManager.getConnection(dbUrl, username, password);
        productos = new ArrayList<>();
        productos.add(new Producto("Laptop", "Electrónics", 1000, true));
        productos.add(new Producto("Mouse", "Electrónics", 50, true));
        productos.add(new Producto("Mesa", "Muebles", 200, false));
        productos.add(new Producto("Silla", "Muebles", 100, true));
        productos.add(new Producto("Teléfono", "Electrónics", 800, true));
    }

    @Override
    public List<Producto> all() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE 1=1")) {
            while (rs.next()) {
                productos.add(new Producto(rs.getString("name"), rs.getString("category"), rs.getInt("price"), rs.getBoolean("available")));
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
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(new CriteriaMysqlConverter().convert(criteria))) {
            while (rs.next()) {
                productos.add(new Producto(rs.getString("name"), rs.getString("category"), rs.getInt("price"), rs.getBoolean("available")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.productos;
    }
}