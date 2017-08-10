package me.deejack.chatserver.database;

import me.deejack.chatserver.user.User;
import me.deejack.chatserver.utils.Date;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author DeeJack
 */
public class MySql {
    private Statement statement;

    public MySql(String username, String password) {
        try {
            DataSource dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/myDB");
            statement = dataSource.getConnection(username, password).createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(User u, String tab) {
        String s = String.format("INSERT INTO %s (username,password,key,creation,type) VALUES (%s,%s,%s,%s,%s);", tab, u.getUsername(), u.getPassword(), u.getKey(), u.getCreation(), u.getType());
        try {
            statement.executeQuery(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fillDatabase(String tab) {
        String s = String.format("SELECT * FROM %s", tab);
        try {
            ResultSet result = statement.executeQuery(s);
            String username = result.getString("username");
            String password = result.getString("password");
            String key = result.getString("key");
            String creation = result.getString("creation");
            int type = result.getInt("type");

            User u = new User(username, new Date(creation).toString(), type,null);
            u.setPassword(password);
            u.setKey(key);

            UserDatabase.getDatabase().add(u);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
