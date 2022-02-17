package day04;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ActorsMoviesService {
    private ActorsRepository actorsRepository;
    private MoviesRepository moviesRepository;
    private ActorsMoviesRepository actorsMoviesRepository;

    public ActorsMoviesService(ActorsRepository actorsRepository, MoviesRepository moviesRepository, ActorsMoviesRepository actorsMoviesRepository) {
        this.actorsRepository = actorsRepository;
        this.moviesRepository = moviesRepository;
        this.actorsMoviesRepository = actorsMoviesRepository;
    }

    public void insertMovieWithActors(String title, LocalDate releaseDate, List<String> actorNames) {
        long movieId = moviesRepository.saveMovie(title, releaseDate);
        for (String actorName : actorNames) {
            Optional<Actor> found = actorsRepository.findActorByName(actorName);
            long actorId;
            if (found.isPresent()) {
                actorId = found.get().getId();

            } else {
                actorId = actorsRepository.saveActor(actorName);
            }
            actorsMoviesRepository.insertActorAndMovieID(actorId, movieId);
        }
    }
}
