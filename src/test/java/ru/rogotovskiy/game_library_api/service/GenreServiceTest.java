package ru.rogotovskiy.game_library_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rogotovskiy.game_library_api.dto.GenreBasicDTO;
import ru.rogotovskiy.game_library_api.entity.Genre;
import ru.rogotovskiy.game_library_api.exceptions.DuplicateGenreException;
import ru.rogotovskiy.game_library_api.exceptions.GenreNotFoundException;
import ru.rogotovskiy.game_library_api.mapper.GenreMapper;
import ru.rogotovskiy.game_library_api.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreService genreService;

    private static final Integer GENRE_ID = 1;
    private static final String GENRE_NAME = "Action";
    private static final String GENRE_DESCRIPTION = "Some description";

    private Genre genre;
    private GenreBasicDTO genreDTO;

    @BeforeEach
    void setUp() {
        genre = new Genre(GENRE_ID, GENRE_NAME, GENRE_DESCRIPTION);
        genreDTO = new GenreBasicDTO(GENRE_NAME, GENRE_DESCRIPTION);
    }

    @Test
    void testGetAll() {
        when(genreRepository.findAll()).thenReturn(List.of(genre));

        List<Genre> result = genreService.getAll();

        assertEquals(1, result.size());
        assertEquals(GENRE_NAME, result.get(0).getName());
        verify(genreRepository).findAll();
    }

    @Test
    void testGetGenreByIdFound() {
        when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));

        Genre result = genreService.getGenreById(GENRE_ID);

        assertEquals(GENRE_NAME, result.getName());
        verify(genreRepository).findById(GENRE_ID);
    }

    @Test
    void testGetGenreByIdNotFound() {
        when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> genreService.getGenreById(GENRE_ID));
        verify(genreRepository).findById(GENRE_ID);
    }

    @Test
    void getById() {
    }

    @Test
    void testCreateGenreSuccess() {
        when(genreRepository.existsByName(GENRE_NAME)).thenReturn(false);
        when(genreMapper.toEntity(genreDTO)).thenReturn(genre);

        genreService.createGenre(genreDTO);

        verify(genreRepository).existsByName(GENRE_NAME);
        verify(genreRepository).save(genre);
    }

    @Test
    void testCreateGenreDuplicate() {
        when(genreRepository.existsByName(GENRE_NAME)).thenReturn(true);
        assertThrows(DuplicateGenreException.class, () -> genreService.createGenre(genreDTO));
        verify(genreRepository).existsByName(GENRE_NAME);
        verify(genreRepository, never()).save(any(Genre.class));
    }

    @Test
    void testDeleteGenreSuccess() {
        when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));

        genreService.deleteGenre(GENRE_ID);

        verify(genreRepository).findById(GENRE_ID);
        verify(genreRepository).delete(genre);
    }

    @Test
    void testDeleteGenreNotFound() {
        when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.empty());

        assertThrows(GenreNotFoundException.class, () -> genreService.deleteGenre(GENRE_ID));
        verify(genreRepository).findById(GENRE_ID);
        verify(genreRepository, never()).delete(any(Genre.class));
    }

    @Test
    void updateGenre() {
    }
}