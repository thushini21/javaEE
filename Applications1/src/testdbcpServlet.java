import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/testdbcp")
public class testdbcpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
    }

