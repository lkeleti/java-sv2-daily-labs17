package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

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

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        System.out.println(actorsRepository.saveActor("Jane Doe"));

        System.out.println(actorsRepository.findActorsWithPrefix("Ja"));
    }
}
