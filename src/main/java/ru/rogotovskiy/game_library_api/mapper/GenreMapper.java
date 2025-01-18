package ru.rogotovskiy.game_library_api.mapper;

import org.springframework.stereotype.Component;
import ru.rogotovskiy.game_library_api.dto.GenreBasicDTO;
import ru.rogotovskiy.game_library_api.entity.Genre;

@Component
public class GenreMapper {

    public GenreBasicDTO toBasicDTO(Genre genre) {
        return new GenreBasicDTO(
                genre.getName(),
                genre.getDescription()
        );
    }

    public Genre toEntity(GenreBasicDTO genreDTO) {
        return new Genre(null, genreDTO.name(), genreDTO.description());
    }
}
