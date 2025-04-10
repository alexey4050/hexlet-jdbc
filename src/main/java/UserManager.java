import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UserManager {
    public static void main(String[] args) throws SQLException {
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:users_db")) {
            var createTableSQL = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(createTableSQL);
            }

            var insertSQL = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                String[][] users = {{"Sarah", "333333333"}, {"John", "444444444"}, {"Alisa", "555555555"}};

                for (String[] user : users) {
                    preparedStatement.setString(1, user[0]);
                    preparedStatement.setString(2, user[1]);
                    preparedStatement.executeUpdate();

                    var generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        System.out.println("Added user id: " + generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("DB has not returned an id after saving the entity");
                    }
                }
            }

            String deleteUsername = "Sarah";
            var deleteSQL = "DELETE FROM users WHERE username = ?";
            try (var preparedStatement = conn.prepareStatement(deleteSQL)) {
                preparedStatement.setString(1, deleteUsername);
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("User " + deleteUsername + " was deleted.");
                } else {
                    System.out.println("User " + deleteUsername + " not found.");
                }
            }
        }
    }
}
