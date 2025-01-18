package ru.rogotovskiy.game_library_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovskiy.game_library_api.dto.GenreBasicDTO;
import ru.rogotovskiy.game_library_api.entity.Genre;
import ru.rogotovskiy.game_library_api.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> getAll() {
        return null;
    }

    public GenreBasicDTO getById(Integer id) {
        return null;
    }

    public void createGenre(GenreBasicDTO genreDTO) {

    }

    public void deleteGenre(Integer id) {

    }

    public void updateGenre(Integer id, GenreBasicDTO genreDTO) {

    }
}
