import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Use the updated MySQL driver class
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company ", "root", "Ijse@1234");
                 ResultSet resultSet = connection.prepareStatement("SELECT * FROM Customer").executeQuery()) {

                // Create a JSON Array Builder to hold all customer objects
                JsonArrayBuilder allCustomers = Json.createArrayBuilder();

                while (resultSet.next()) {
                    // Read data from the result set
                    String id = resultSet.getString("id");  // Assuming id is stored as a string
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");

                    // Create a JSON object for each customer
                    JsonObjectBuilder customer = Json.createObjectBuilder();
                    customer.add("id", id);           // Add id as a string
                    customer.add("name", name);       // Add name
                    customer.add("address", address); // Add address

                    // Add this customer to the JSON array
                    allCustomers.add(customer);
                }

                // Set response content type to JSON and send the JSON response
                resp.setContentType("application/json");
                resp.getWriter().write(allCustomers.build().toString());

            }

//            resp.setContentType("application/json");
//            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
////                  resp.sendError(HttpServletResponse.SC_BAD_GATEWAY);
////                resp.addHeader("IP-ADDRESS","123.123.12.1"); // header ejt ip address ekk dnwa , cash ek clear krl ai req ek ennth mek use krnn plwn
//            resp.getWriter().write(allCustomers.build().toString());

        } catch (ClassNotFoundException | SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Error
            resp.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@1234")) {
            String sql = "INSERT INTO customer (id, name, address) VALUES (?, ?, ?)";
            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.setString(1, id);
                pst.setString(2, name);
                pst.setString(3, address);

                int rowsAffected = pst.executeUpdate();
                resp.setContentType("text/plain");
                if (rowsAffected > 0) {
                    resp.getWriter().write("Customer added successfully.");
                } else {
                    resp.getWriter().write("Failed to add customer.");
                }
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Error: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@1234")) {

            String query = "UPDATE customer SET name = ?, address = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, address);
                stmt.setString(3, id);
                int rowsAffected = stmt.executeUpdate();
                resp.setContentType("text/plain");
                if (rowsAffected > 0) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Customer not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Database error occurred.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String query = "DELETE FROM customer WHERE id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "Ijse@1234")) {

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, id);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write("Customer deleted successfully!");
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Customer not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
