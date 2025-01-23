package ru.rogotovskiy.game_library_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rogotovskiy.game_library_api.dto.GenreBasicDTO;
import ru.rogotovskiy.game_library_api.dto.GenreDTO;
import ru.rogotovskiy.game_library_api.entity.Genre;
import ru.rogotovskiy.game_library_api.exceptions.DuplicateGenreException;
import ru.rogotovskiy.game_library_api.exceptions.GenreNotFoundException;
import ru.rogotovskiy.game_library_api.mapper.FacadeMapper;
import ru.rogotovskiy.game_library_api.mapper.GenreMapper;
import ru.rogotovskiy.game_library_api.repository.GenreRepository;

import java.util.Collections;
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

    @Mock
    private FacadeMapper facadeMapper;

    @InjectMocks
    private GenreService genreService;

    private static final Integer GENRE_ID = 1;
    private static final String GENRE_NAME = "Action";
    private static final String GENRE_DESCRIPTION = "Some description";

    private Genre genre;
    private GenreBasicDTO genreBasicDTO;
    private GenreDTO genreDTO;

    @BeforeEach
    void setUp() {
        genre = new Genre(GENRE_ID, GENRE_NAME, GENRE_DESCRIPTION, null);
        genreBasicDTO = new GenreBasicDTO(GENRE_NAME, GENRE_DESCRIPTION);
        genreDTO = new GenreDTO(GENRE_NAME, GENRE_DESCRIPTION, Collections.emptyList());
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

        GenreNotFoundException e = assertThrows(GenreNotFoundException.class, () -> genreService.getGenreById(GENRE_ID));

        assertEquals(String.format("Жанр с id = %d не найден!", GENRE_ID), e.getMessage());
        verify(genreRepository).findById(GENRE_ID);
    }

    @Test
    void getById() {
        when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        when(facadeMapper.toDTO(genre)).thenReturn(genreDTO);

        GenreDTO result = genreService.getById(GENRE_ID);

        assertEquals(genreDTO, result);
        verify(genreRepository).findById(GENRE_ID);
        verify(facadeMapper).toDTO(genre);

    }

    @Test
    void testCreateGenreSuccess() {
        when(genreRepository.existsByName(GENRE_NAME)).thenReturn(false);
        when(genreMapper.toEntity(genreBasicDTO)).thenReturn(genre);
        when(genreRepository.save(genre)).thenReturn(genre);

        Genre result = genreService.createGenre(genreBasicDTO);

        assertEquals(genre, result);
        verify(genreRepository).existsByName(GENRE_NAME);
        verify(genreMapper).toEntity(genreBasicDTO);
        verify(genreRepository).save(genre);
    }

    @Test
    void testCreateGenreDuplicate() {
        when(genreRepository.existsByName(GENRE_NAME)).thenReturn(true);

        DuplicateGenreException e = assertThrows(DuplicateGenreException.class, () -> genreService.createGenre(genreBasicDTO));

        assertEquals(String.format("Жанр с именем %s уже существует!", GENRE_NAME), e.getMessage());
        verify(genreRepository).existsByName(GENRE_NAME);
        verify(genreMapper, never()).toEntity(genreBasicDTO);
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

        GenreNotFoundException e = assertThrows(GenreNotFoundException.class, () -> genreService.deleteGenre(GENRE_ID));

        assertEquals(String.format("Жанр с id = %d не найден!", GENRE_ID), e.getMessage());
        verify(genreRepository).findById(GENRE_ID);
        verify(genreRepository, never()).delete(any(Genre.class));
    }

    @Test
    void updateGenreSuccess() {
        Genre updatedGenre = new Genre(GENRE_ID, "Adventure", "Updated description", Collections.emptyList());

        when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        when(genreRepository.existsByName("Adventure")).thenReturn(false);
        when(genreRepository.save(genre)).thenReturn(updatedGenre);

        GenreBasicDTO updateGenre = new GenreBasicDTO("Adventure", "Updated description");
        Genre result = genreService.updateGenre(GENRE_ID, updateGenre);

        assertEquals(updatedGenre.getName(), result.getName());
        assertEquals(updatedGenre.getDescription(), result.getDescription());
        verify(genreRepository).findById(GENRE_ID);
        verify(genreRepository).existsByName("Adventure");
        verify(genreRepository).save(genre);
    }

    @Test
    void updateGenreFailed() {
        when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        when(genreRepository.existsByName("Adventure")).thenReturn(true);

        GenreBasicDTO updateGenre = new GenreBasicDTO("Adventure", "Exciting games");
        DuplicateGenreException exception = assertThrows(DuplicateGenreException.class, () -> genreService.updateGenre(1, updateGenre));

        assertEquals("Жанр с именем Adventure уже существует!", exception.getMessage());
        verify(genreRepository).findById(1);
        verify(genreRepository).existsByName("Adventure");
        verify(genreRepository, never()).save(genre);
    }
}