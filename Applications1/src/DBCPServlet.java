import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = "/dbcp")
public class DBCPServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        BasicDataSource ds= new BasicDataSource();
//        ds.setDriverClassName("com.mysql.jdbc.Driver");
//        ds.setUrl("jdbc:mysql://localhost:3306/company");
//        ds.setUsername("root");
//        ds.setPassword("Ijse@1234");
//        ds.setMaxTotal(5);
//        ds.setInitialSize(5);

        ServletContext servletContext = req.getServletContext();
        BasicDataSource ds = (BasicDataSource) servletContext.getAttribute("dataSource");


        try (Connection connection = ds.getConnection();
             ResultSet resultSet = connection.prepareStatement("SELECT * FROM Customer").executeQuery()) {


            while (resultSet.next()) {
                // Read data from the result set
                String id = resultSet.getString("id");  // Assuming id is stored as a string
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                System.out.println(id + "" + name + "" + address);

            }
  connection.close();
            // Set response content type to JSON and send the JSON response
            resp.setContentType("application/json");

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("basic data source");

       }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletContext servletContext = req.getServletContext();
        BasicDataSource ds = (BasicDataSource) servletContext.getAttribute("dataSource");

        try (Connection connection = ds.getConnection();
             ResultSet resultSet = connection.prepareStatement("SELECT * FROM Customer").executeQuery()) {


            while (resultSet.next()) {
                // Read data from the result set
                String id = resultSet.getString("id");  // Assuming id is stored as a string
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                System.out.println(id + "" + name + "" + address);

            }

            // Set response content type to JSON and send the JSON response
            resp.setContentType("application/json");

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("basic data source");

    }
    }
