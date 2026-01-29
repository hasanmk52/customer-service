package com.netflux.dto;

import com.netflux.domain.Genre;

import java.util.List;

public record CustomerDto(Integer id,
                          String name,
                          Genre favoriteGenre,
                          List<MovieDto> recommendedMovies) {
}
