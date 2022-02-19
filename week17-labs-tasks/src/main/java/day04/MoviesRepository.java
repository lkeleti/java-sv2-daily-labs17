package day04;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                result.add(new Movie(
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

    public Optional<Movie> findMovieByTitle(String title) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM movies WHERE title=?");
        ) {
            pstmt.setString(1, title);
            return findMovieByTitleByStatement(pstmt);

        } catch (SQLException sqle) {
            throw new IllegalStateException("Can't select movie by title.", sqle);
        }
    }

    private Optional<Movie> findMovieByTitleByStatement(PreparedStatement pstmt) throws SQLException {
        try (
                ResultSet rs = pstmt.executeQuery();
        ) {
            if (rs.next()) {
                return Optional.of(new Movie(rs.getLong("id"), rs.getString("title"), rs.getDate("release_date").toLocalDate()));
            } else {
                return Optional.empty();
            }
        }
    }

    public void setAvgRaing(long movieId, double avgRating) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("UPDATE movies SET avg_rating = ? WHERE id = ?")
        ) {
            pstmt.setDouble(1,avgRating);
            pstmt.setLong(2,movieId);
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can't update movies by average rating", sqle);
        }
    }
}
