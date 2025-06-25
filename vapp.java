import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class VulnerableApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userInput = request.getParameter("username");
        String passwordInput = request.getParameter("password");

        // 🔥 OWASP A1:2021 - Broken Access Control (Missing role check)
        if (request.getParameter("admin").equals("true")) {
            response.getWriter().println("Welcome, admin user!");
        }

        // 🔥 OWASP A3:2021 - Injection (SQL Injection)
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "password");
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM users WHERE username = '" + userInput + "' AND password = '" + passwordInput + "'";
            ResultSet rs = stmt.executeQuery(query);

            // 🔥 OWASP A9:2021 - Security Logging and Monitoring Failures
            System.out.println("User tried to login: " + userInput); // Logging sensitive data

            if (rs.next()) {
                response.getWriter().println("Login successful!");
            } else {
                response.getWriter().println("Login failed.");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 🔥 OWASP A5:2021 - Security Misconfiguration
        response.setHeader("X-Powered-By", "VulnerableApp/1.0");

        // 🔥 OWASP A6:2021 - Vulnerable and Outdated Components
        // (Assuming this runs on an old servlet API version with known bugs)
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 🔥 OWASP A7:2021 - Identification and Authentication Failures
        HttpSession session = request.getSession(true); // Creates a new session every time
        response.getWriter().println("Session ID: " + session.getId());
    }
}
