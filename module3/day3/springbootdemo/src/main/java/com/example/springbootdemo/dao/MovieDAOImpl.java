package com.example.springbootdemo.dao;

import com.example.springbootdemo.model.Movie;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MovieDAOImpl implements MovieDAO {

    private Map<Integer, Movie> movies;

    @PostConstruct
    public void init() {
        movies = new LinkedHashMap<>();
        movies.put(1, new Movie(1, "Inception", "Christopher Nolan", 2010, "Sci-Fi", 8.8));
        movies.put(2, new Movie(2, "Interstellar", "Christopher Nolan", 2014, "Sci-Fi", 8.7));
        movies.put(3, new Movie(3, "The Dark Knight", "Christopher Nolan", 2008, "Action", 9.0));
        movies.put(4, new Movie(4, "Avatar", "James Cameron", 2009, "Sci-Fi", 7.9));
        movies.put(5, new Movie(5, "Titanic", "James Cameron", 1997, "Romance", 7.9));
    }

    @Override
    public void save(Movie movie) {
        movies.put(movie.getId(), movie);
    }

    @Override
    public void updateById(int id, Movie movie) {
        if (movies.containsKey(id)) {
            movie.setId(id);
            movies.put(id, movie);
        }
    }

    @Override
    public void deleteById(int id) {
        movies.remove(id);
    }

    @Override
    public Movie getById(int id) {
        return movies.get(id);
    }

    @Override
    public List<Movie> getAll() {
        return new ArrayList<>(movies.values());
    }

    @Override
    public Movie getByTitle(String title) {
        return movies.values()
                .stream()
                .filter(movie -> movie.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Movie> getByDirector(String director) {
        return movies.values()
                .stream()
                .filter(movie -> movie.getDirector().equalsIgnoreCase(director))
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> sortByYear() {
        return movies.values()
                .stream()
                .sorted(Comparator.comparingInt(Movie::getYear))
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> sortByRating() {
        return movies.values()
                .stream()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .collect(Collectors.toList());
    }

    @PreDestroy
    public void destroy() {
        movies.clear();
    }
}
