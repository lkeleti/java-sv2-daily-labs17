package day04;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MoviesActorsMain {
    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies_actors?useUnicode=true");
            dataSource.setUserName("movies");
            dataSource.setPassword("movies");
        } catch (SQLException se) {
            throw new IllegalStateException("Can't connect to database", se);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        //System.out.println(actorsRepository.saveActor("Jane Doe"));
        //System.out.println(actorsRepository.findActorsWithPrefix("Ja"));

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);

        ActorsMoviesService actorsMoviesService = new ActorsMoviesService(actorsRepository,moviesRepository,actorsMoviesRepository);

        actorsMoviesService.insertMovieWithActors("Titanic", LocalDate.of(1997,11,13), List.of("Leonardo DiCaprio", "Kate Winslet"));
        actorsMoviesService.insertMovieWithActors("Great Gatsby", LocalDate.of(2012,12,11), List.of("Leonardo DiCaprio", "Toby McGuyer"));
    }
}
