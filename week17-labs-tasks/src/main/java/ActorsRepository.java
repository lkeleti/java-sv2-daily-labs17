import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorsRepository {
    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveActor(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO actors (actor_name) VALUES(?);", Statement.RETURN_GENERATED_KEYS);
        ) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            return getIdByStatement(pstmt);
        } catch (SQLException se) {
            throw new IllegalStateException("Can't connect to database", se);
        }
    }

    private long getIdByStatement(PreparedStatement pstmt) throws SQLException {
        try (
                ResultSet rs = pstmt.getGeneratedKeys();
        ) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new IllegalStateException("Can't get id");
        }
    }

    public List<String> findActorsWithPrefix(String prefix) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT actor_name FROM actors WHERE actor_name like ?")
        ) {
            pstmt.setString(1, prefix + "%");
            return getNamesByStatement(pstmt);

        } catch (SQLException se) {
            throw new IllegalStateException("Can't execute SELECT", se);
        }
    }

    private List<String> getNamesByStatement(PreparedStatement pstmt) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            List<String> names = new ArrayList<>();
            while (rs.next()) {
                names.add(rs.getString("actor_name"));
            }
            return names;
        }
    }
}
