import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringJoiner;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
@WebServlet("/doctor-register")
public class DoctorRegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String specialization = request.getParameter("specialization");
        int experience = Integer.parseInt(request.getParameter("experience"));
        String gender = request.getParameter("gender");
        String availableDays = joinValues(request.getParameterValues("days"));
        String availableTime = request.getParameter("time");
        String address = request.getParameter("address");
        String certificate = getSubmittedFileName(request.getPart("certificate"));

        String sql = "INSERT INTO doctors "
                + "(name, password, email, phone, specialization, experience, gender, available_days, available_time, address, certificate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, specialization);
            ps.setInt(6, experience);
            ps.setString(7, gender);
            ps.setString(8, availableDays);
            ps.setString(9, availableTime);
            ps.setString(10, address);
            ps.setString(11, certificate);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                out.println("<h2>Doctor registered successfully.</h2>");
                out.println("<a href='DoctorLogin.html'>Go to Doctor Login</a>");
            } else {
                out.println("<h2>Doctor registration failed.</h2>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
        }
    }

    private String joinValues(String[] values) {
        if (values == null || values.length == 0) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(",");
        for (String value : values) {
            joiner.add(value);
        }
        return joiner.toString();
    }

    private String getSubmittedFileName(Part part) {
        return part == null ? "" : part.getSubmittedFileName();
    }
}
