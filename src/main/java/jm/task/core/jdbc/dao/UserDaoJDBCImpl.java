package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() { }

    public void createUsersTable() throws SQLException {
        try {
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS table_user" +
                                        "(id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                                        " name VARCHAR(100), " +
                                        " lastName VARCHAR(100), " +
                                        " age INTEGER)");
            System.out.println("Таблица создана");
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }

    public void dropUsersTable() throws SQLException {
        try {
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            statement.executeUpdate("DROP TABLE IF EXISTS table_user");
            System.out.println("Таблица удалена");
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO table_user(name, lastName, age) VALUES(?, ?, ?)");
            connection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            connection.rollback();
        }
    }

    public void removeUserById(long id) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM table_user WHERE id");
            System.out.println("User " + id + " удален");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> allUser = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT id, name, lastName, age FROM table_user");
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                allUser.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUser;
    }

    public void cleanUsersTable() throws SQLException {
        try {
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            statement.executeUpdate("TRUNCATE table_user");
            connection.commit();
            System.out.println("Таблица очищена");
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("Таблица не очищена");
        }
    }
}
