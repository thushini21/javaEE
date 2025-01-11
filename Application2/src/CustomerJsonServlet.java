import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerJsonServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("inside doget");
        //get all data from db
        try {
            Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement("select * from customer").executeQuery();

            //create customer data json array
            JsonArrayBuilder allCustomer = Json.createArrayBuilder();


            while ((resultSet.next())) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");

                JsonObjectBuilder customer = Json.createObjectBuilder();

                customer.add("id", id);
                customer.add("name", name);
                customer.add("address", address);

                allCustomer.add(customer);

            }

            //build response object
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status",HttpServletResponse.SC_CREATED);
            response.add("massage","Created");
            response.add("data",allCustomer);

            //send response
            resp.setContentType("application/json");
            resp.getWriter().print(response.build());

        } catch (SQLException e) {

            //handle exeption
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.add("massage","Error");
            response.add("data","");


            resp.setContentType("application/json");
            resp.getWriter().print(response.build());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //read JSON Object
        try{
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonObject = reader.readObject();

            //e ewana object eka read karanne mehema (use karana library eka javax json)
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String address = jsonObject.getString("address");

            //data save on database
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO customer (id, name, address) VALUES (?, ?, ?)";
            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.setString(1, id);
                pst.setString(2, name);
                pst.setString(3, address);

                int rowsAffected = pst.executeUpdate();

                //response
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status",HttpServletResponse.SC_CREATED);
                response.add("massage","Created");
                response.add("data",jsonObject);

                System.out.println(jsonObject);
                resp.setContentType("application/json");
                resp.getWriter().print(response.build());

            }catch(Exception e){
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.add("massage","Error");
                response.add("data","");


                resp.setContentType("application/json");
                resp.getWriter().print(response.build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonObject = reader.readObject();

            //e ewana object eka read karanne mehema (use karana library eka javax json)
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String address = jsonObject.getString("address");

            //data save on database
            Connection connection = dataSource.getConnection();
            String query = "UPDATE customer SET name = ?, address = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, address);
                stmt.setString(3, id);
                int rowsAffected = stmt.executeUpdate();
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status",HttpServletResponse.SC_CREATED);
                response.add("massage","Created");
                response.add("data",jsonObject);

                System.out.println(jsonObject);
                resp.setContentType("application/json");
                resp.getWriter().print(response.build());

            } catch (SQLException e) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.add("massage","Error");
                response.add("data","");


                resp.setContentType("application/json");
                resp.getWriter().print(response.build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


