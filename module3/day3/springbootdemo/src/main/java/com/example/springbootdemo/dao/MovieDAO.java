package com.example.springbootdemo.dao;

import com.example.springbootdemo.model.Movie;

import java.util.List;

public interface MovieDAO {
    public void save(Movie movie);
    public void updateById(int id, Movie movie);
    public void deleteById(int id);
    public Movie getById(int id);
    public List<Movie> getAll();
    public Movie getByTitle(String title);
    public List<Movie> getByDirector(String director);
    public List<Movie> sortByYear();
    public List<Movie> sortByRating();
}
