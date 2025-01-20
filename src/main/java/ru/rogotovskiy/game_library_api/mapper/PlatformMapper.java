package ru.rogotovskiy.game_library_api.mapper;

import org.springframework.stereotype.Component;
import ru.rogotovskiy.game_library_api.dto.PlatformBasicDTO;
import ru.rogotovskiy.game_library_api.entity.Platform;

import java.util.Collections;

@Component
public class PlatformMapper {

    public PlatformBasicDTO toBasicDTO(Platform platform) {
        return new PlatformBasicDTO(platform.getName(), platform.getDescription());
    }

    public Platform toEntity(PlatformBasicDTO platformDTO) {
        return new Platform(null, platformDTO.name(), platformDTO.description(), Collections.emptyList());
    }
}
