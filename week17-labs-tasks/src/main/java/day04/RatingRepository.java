package day04;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RatingRepository {
    private DataSource dataSource;

    public RatingRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRating(long movieId, List<Integer> ratings) {
        try (
                Connection conn = dataSource.getConnection();
        ) {
            conn.setAutoCommit(false);
            insertRatingByStatement(movieId, ratings, conn);
            conn.setAutoCommit(true);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can't insert rating", sqle);
        }
    }

    private void insertRatingByStatement(long movieId, List<Integer> ratings, Connection conn) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ratings (movie_id, rating) VALUES(?,?)")) {
            for (int rating : ratings) {
                if (rating >= 1 && rating <= 5) {
                    pstmt.setLong(1, movieId);
                    pstmt.setLong(2, rating);
                    pstmt.executeUpdate();
                } else {
                    throw new IllegalArgumentException("Invalid rating");
                }
            }
            conn.commit();
        } catch (IllegalArgumentException iae) {
            conn.rollback();
        }
    }
}
