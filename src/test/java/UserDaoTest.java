import user.management.User;
import user.management.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:h2:mem:testdb";
        try (Connection connection = DriverManager.getConnection(url)) {
            String createTableSQL = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = connection.createStatement()) {
                statement.execute(createTableSQL);
            }

            UserDAO userDAO = new UserDAO(connection);

            User newUser = new User("John Doe", "+123456789");
            userDAO.save(newUser);
            System.out.println("Saved user ID: " +newUser.getId());

            User foundUser = userDAO.find(newUser.getId()).orElse(null);
            if (foundUser != null) {
                System.out.println("Found user: " + foundUser.getName() + ", Phone: " + foundUser.getPhone());
            }

            userDAO.delete(newUser.getId());
            if (userDAO.find(newUser.getId()).isEmpty()) {
                System.out.println("User successfully deleted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
