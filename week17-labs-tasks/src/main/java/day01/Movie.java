package day01;

import java.time.LocalDate;

public class Movie {
    private Long id;
    private String title;
    private LocalDate releaseDate;

    public Movie(Long id, String title, LocalDate releaseDate) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
