package ru.rogotovskiy.game_library_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovskiy.game_library_api.dto.GenreBasicDTO;
import ru.rogotovskiy.game_library_api.entity.Genre;
import ru.rogotovskiy.game_library_api.exceptions.DuplicateGenreException;
import ru.rogotovskiy.game_library_api.exceptions.GenreNotFoundException;
import ru.rogotovskiy.game_library_api.mapper.GenreMapper;
import ru.rogotovskiy.game_library_api.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final String GENRE_NOT_FOUND_TEMPLATE = "Жанр с id = %d не найден!";
    private final String DUPLICATE_GENRE_TEMPLATE = "Жанр с именем %s уже существует!";
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(Integer id) {
        return genreRepository.findById(id).orElseThrow(
                () -> new GenreNotFoundException(
                        String.format(GENRE_NOT_FOUND_TEMPLATE, id)
                )
        );
    }

    public GenreBasicDTO getById(Integer id) {
        Genre genre = getGenreById(id);
        return genreMapper.toBasicDTO(genre);
    }

    public void createGenre(GenreBasicDTO genreDTO) {
        if (genreRepository.existsByName(genreDTO.name())) {
            throw new DuplicateGenreException(
                    String.format(DUPLICATE_GENRE_TEMPLATE, genreDTO.name())
            );
        }
        genreRepository.save(genreMapper.toEntity(genreDTO));
    }

    public void deleteGenre(Integer id) {
        Genre genre = getGenreById(id);
        genreRepository.delete(genre);
    }

    public void updateGenre(Integer id, GenreBasicDTO genreDTO) {
        Genre genre = getGenreById(id);
        if (genreDTO.name() != null && !genreDTO.name().equals(genre.getName())) {
            if (genreRepository.existsByName(genreDTO.name())) {
                throw new DuplicateGenreException(
                        String.format(DUPLICATE_GENRE_TEMPLATE, genreDTO.name())
                );
            }
            genre.setName(genreDTO.name());
        }
        if (genreDTO.description() != null) {
            genre.setDescription(genreDTO.description());
        }
        genreRepository.save(genre);
    }
}
