package com.example.springbootdemo.controller;

import com.example.springbootdemo.dao.MovieDAO;
import com.example.springbootdemo.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieRestController {

    @Autowired
    private MovieDAO movieDAO;

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable int id) {
        return movieDAO.getById(id);
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieDAO.getAll();
    }

    @PostMapping
    public String addMovie(@RequestBody Movie movie) {
        movieDAO.save(movie);
        return "Movie added: " + movie.getId();
    }

    @PutMapping("/{id}")
    public Movie updateMovie(@PathVariable int id,
                             @RequestBody Movie movie) {
        movieDAO.updateById(id, movie);
        return movieDAO.getById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable int id) {
        movieDAO.deleteById(id);
        return "Movie deleted: " + id;
    }

    @GetMapping("/director/{director}")
    public List<Movie> getMoviesByDirector(
            @PathVariable String director) {
        return movieDAO.getByDirector(director);
    }

    @GetMapping("/title")
    public Movie getMovieByTitle(
            @RequestParam String title) {
        return movieDAO.getByTitle(title);
    }

    @GetMapping("/sort/year")
    public List<Movie> sortByYear() {
        return movieDAO.sortByYear();
    }

    @GetMapping("/sort/rating")
    public List<Movie> sortByRating() {
        return movieDAO.sortByRating();
    }
}
