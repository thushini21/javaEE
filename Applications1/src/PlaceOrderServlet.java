import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/placeOrder")
public class PlaceOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Retrieve parameters from the request
            String oid = req.getParameter("orderID");
            String customerID = req.getParameter("customerID");
            String itemCode = req.getParameter("itemCode");
            String date = req.getParameter("date");
            String qty = req.getParameter("quantity"); // Added this missing parameter
            String subTotal = req.getParameter("subTotal");

            // Connect to the database
            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ShopDB?useSSL=false&allowPublicKeyRetrieval=true",
                    "root", "Ijse@1234"
            )) {
                // SQL query to insert order details
                String sql = "INSERT INTO orderdetails (OrderID, CustomerID, ItemCode, OrderDate, Quantity, TotalPrice) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pst = connection.prepareStatement(sql)) {
                    pst.setString(1, oid);
                    pst.setString(2, customerID);
                    pst.setString(3, itemCode);
                    pst.setString(4, date);
                    pst.setString(5, qty);
                    pst.setString(6, subTotal);

                    // Execute the query and handle response
                    int rowsAffected = pst.executeUpdate();
                    resp.setContentType("text/plain");
                    if (rowsAffected > 0) {
                        resp.getWriter().write("Order placed successfully.");
                    } else {
                        resp.getWriter().write("Failed to place the order.");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Database driver not found: " + e.getMessage());
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("SQL Error: " + e.getMessage());
        }
    }
}
