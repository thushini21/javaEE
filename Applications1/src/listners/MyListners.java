package listners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebListener
public class MyListners implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        System.out.println("contextInitialized");  //application ek run weddima start wnw

        BasicDataSource ds= new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/company");
        ds.setUsername("root");
        ds.setPassword("Ijse@1234");
        ds.setMaxTotal(5);
        ds.setInitialSize(5);

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("dataSource",ds);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       //System.out.println("contextDestroyed"); //application ek close wnw

        ServletContext servletContext = sce.getServletContext();
        BasicDataSource ds = (BasicDataSource) servletContext.getAttribute("dataSource");

        try {
            ds.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
