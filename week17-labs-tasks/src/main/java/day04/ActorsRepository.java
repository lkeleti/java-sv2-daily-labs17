package day04;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

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

    public Optional<Actor> findActorByName(String actorName) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM actors WHERE actor_name = ?;");
        ) {
            pstmt.setString(1, actorName);
            return findActorByStatement(pstmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can't Select actors by name", sqle);
        }
    }

    private Optional<Actor> findActorByStatement(PreparedStatement pstmt) throws SQLException {
        try (ResultSet rs =  pstmt.executeQuery();)
        {
            if (rs.next()) {
                return Optional.of(new Actor(rs.getLong("id"), rs.getString("actor_name")));
            }
            else {
                return Optional.empty();
            }
        }
    }
}
