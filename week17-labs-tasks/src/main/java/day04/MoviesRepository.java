package day04;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {

    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveMovie(String title, LocalDate releaseDate) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO movies (title, release_date) VALUES(?,?)",
                        Statement.RETURN_GENERATED_KEYS);

        ) {
            pstmt.setString(1, title);
            pstmt.setDate(2, Date.valueOf(releaseDate));
            pstmt.executeUpdate();
            return readBackId(pstmt);
        } catch (SQLException se) {
            throw new IllegalStateException("Can't save movie.", se);
        }
    }

    private long readBackId(PreparedStatement pstmt) throws SQLException {
        try (
                ResultSet rs = pstmt.getGeneratedKeys();
        ) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new IllegalStateException("Can't retrieve generated Id");
        }
    }

    public List<Movie> findAllMovies() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM movies;");
        ) {
            List<Movie> result = new ArrayList<>();
            while (rs.next()) {
                result.add( new Movie(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getDate("release_date").toLocalDate()
                ));
            }
            return result;
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can't fid movies", sqle);
        }
    }
}
