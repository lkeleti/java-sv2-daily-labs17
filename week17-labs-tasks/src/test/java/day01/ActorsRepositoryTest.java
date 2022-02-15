package day01;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActorsRepositoryTest {
    ActorsRepository actorsRepository;
    MoviesRepository moviesRepository;

    @BeforeEach
    void init() {
        MariaDbDataSource dataSource = new  MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies_actors_test?useUnicode=true");
            dataSource.setUserName("movies");
            dataSource.setPassword("movies");
        } catch (SQLException se) {
            throw new IllegalStateException("Can't connect to database", se);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();
        actorsRepository = new ActorsRepository(dataSource);
        moviesRepository = new MoviesRepository(dataSource);
    }

    @Test
    void testInsert() {
        actorsRepository.saveActor("john Doe");
        moviesRepository.saveMovie("Titanic", LocalDate.of(1997,12,31));
    }

    @Test
    void testFindAllMovies() {
        moviesRepository.saveMovie("Titanic", LocalDate.of(1997,12,31));
        moviesRepository.saveMovie("Gladiators", LocalDate.of(1996,1,12));
        List<Movie> movies = moviesRepository.findAllMovies();
        assertEquals(2, movies.size());
    }
}