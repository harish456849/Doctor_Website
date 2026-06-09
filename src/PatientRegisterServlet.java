import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringJoiner;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
public class PatientRegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String dob = request.getParameter("dob");
        String appointment = request.getParameter("appointment");
        String gender = request.getParameter("gender");
        String disease = joinValues(request.getParameterValues("disease"));
        String bloodGroup = request.getParameter("blood_group");
        String address = request.getParameter("address");
        String certificate = getSubmittedFileName(request.getPart("certificate"));

        String sql = "INSERT INTO patients "
                + "(name, password, email, phone, dob, appointment, gender, disease, blood_group, address, certificate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, emptyToNull(dob));
            ps.setString(6, normalizeDateTime(appointment));
            ps.setString(7, gender);
            ps.setString(8, disease);
            ps.setString(9, bloodGroup);
            ps.setString(10, address);
            ps.setString(11, certificate);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                out.println("<h2>Patient registered successfully.</h2>");
                out.println("<a href='PatientLogin.html'>Go to Patient Login</a>");
            } else {
                out.println("<h2>Patient registration failed.</h2>");
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

    private String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }

    private String normalizeDateTime(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value.replace("T", " ") + ":00";
    }

    private String getSubmittedFileName(Part part) {
        return part == null ? "" : part.getSubmittedFileName();
    }
}
