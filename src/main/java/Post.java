
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nprince on 3/13/16.
 */
public class Post {
    public int id;
    public Timestamp timestamp;
    public String content;
    public int sentiment;
    public float latitude;
    public float longitude;
    public String source;

    public int getId() {
        return id;
    }

    public static Post Get(int id) {
        // TODO: Grab post from mysql
        return null;
    }

    public static Post Insert(Timestamp timestamp,
                                  String content,
                                  int sentiment,
                                  float latitude,
                                  float longitude,
                                  String source) {
        MysqlConnect mysqlConnect = new MysqlConnect();

        String insert = "INSERT INTO Post (timestamp, content, sentiment, latitude, longitude, source)" +
                "VALUES (?,?,?,?,?,?)";

        int id = 0;

        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(insert);
            statement.setString(1, timestamp.toString());
            statement.setString(2, content);
            statement.setString(3, "" + sentiment);
            statement.setString(4, "" + latitude);
            statement.setString(5, "" + longitude);
            statement.setString(6, "" + source);

            statement.execute();

            PreparedStatement getId = mysqlConnect.connect().prepareStatement("SELECT id FROM Post WHERE timestamp=? AND content=? AND sentiment=? AND latitude=? AND longitude=? AND source=?");
            getId.setString(1, timestamp.toString());
            getId.setString(2, content);
            getId.setString(3, "" + sentiment);
            getId.setString(4, "" + latitude);
            getId.setString(5, "" + longitude);
            getId.setString(6, "" + source);

            ResultSet rs = getId.executeQuery();
            rs.first();
            id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }

        return new Post(id, timestamp, content, sentiment, latitude, longitude, source);
    }

    public boolean delete() {
        MysqlConnect mysqlConnect = new MysqlConnect();

        boolean ret = true;
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement("DELETE FROM Post " +
                    "WHERE id=?");
            statement.setString(1, "" + id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            ret = false;
        } finally {
            mysqlConnect.disconnect();
        }

        return ret;
    }

    public boolean update() {
        MysqlConnect mysqlConnect = new MysqlConnect();

        String update = "UPDATE Post " +
                "SET timestamp=?, content=?, sentiment=?, latitude=?, longitude=?, source=?" +
                "WHERE id=?";

        boolean ret = true;
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(update);
            statement.setString(1, timestamp.toString());
            statement.setString(2, content);
            statement.setString(3, "" + sentiment);
            statement.setString(4, "" + latitude);
            statement.setString(5, "" + longitude);
            statement.setString(6, "" + source);
            statement.setString(7, "" + id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            ret = false;
        } finally {
            mysqlConnect.disconnect();
        }

        return ret;
    }

    // Do not use this constructor to create a new post; rather to mimic an existing one in the database.
    // Use Post.Insert to create new posts.
    public Post(int id, Timestamp timestamp, String content, int sentiment, float latitude, float longitude, String source) {
        // Everything is locally stored. Use post.update() to update this post in the database with current settings
        this.id = id;
        this.timestamp = timestamp;
        this.content = content;
        this.sentiment = sentiment;
        this.latitude = latitude;
        this.longitude = longitude;
        this.source = source;
    }
}
