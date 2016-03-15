
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nprince on 3/13/16.
 */
public class Post {
    private int id;

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

        return new Post(id);
    }

    public Post(int id) {
        // TODO: getters and setters should read directly from the database, or be locally stored
        // We'll have to decide on this. Locally stored means only one read to the DB, but it's
        // data could have changed.
        return;
    }
}
