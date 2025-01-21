package ru.rogotovskiy.game_library_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovskiy.game_library_api.dto.PlatformBasicDTO;
import ru.rogotovskiy.game_library_api.dto.PlatformDTO;
import ru.rogotovskiy.game_library_api.entity.Platform;
import ru.rogotovskiy.game_library_api.exceptions.DuplicatePlatformException;
import ru.rogotovskiy.game_library_api.exceptions.PlatformNotFoundException;
import ru.rogotovskiy.game_library_api.mapper.FacadeMapper;
import ru.rogotovskiy.game_library_api.mapper.PlatformMapper;
import ru.rogotovskiy.game_library_api.repository.PlatformRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlatformService {
    private final String PLATFORM_NOT_FOUND_TEMPLATE = "Платформа с id = %d не найдена!";
    private final String DUPLICATE_PLATFORM_TEMPLATE = "Платформа с именем %s уже существует!";
    private final PlatformRepository platformRepository;
    private final FacadeMapper facadeMapper;
    private final PlatformMapper platformMapper;

    public List<Platform> getAll() {
        return platformRepository.findAll();
    }

    public Platform getPlatformById(Integer id) {
        return platformRepository.findById(id).orElseThrow(
                () -> new PlatformNotFoundException(
                        String.format(PLATFORM_NOT_FOUND_TEMPLATE, id)
                )
        );
    }


    public PlatformDTO getById(Integer id) {
        Platform platform = getPlatformById(id);
        return facadeMapper.toDTO(platform);
    }

    public Platform createPlatform(PlatformBasicDTO platformDTO) {
        if (platformRepository.existsByName(platformDTO.name())) {
            throw new DuplicatePlatformException(
                    String.format(DUPLICATE_PLATFORM_TEMPLATE, platformDTO.name())
            );
        }

        return platformRepository.save(platformMapper.toEntity(platformDTO));
    }

    public void deletePlatform(Integer id) {
        Platform platform = getPlatformById(id);
        platformRepository.delete(platform);
    }

    public Platform updatePlatform(Integer id, PlatformBasicDTO platformDTO) {
        Platform platform = getPlatformById(id);
        if (platformDTO.name() != null && !platformDTO.name().equals(platform.getName())) {
            if (platformRepository.existsByName(platformDTO.name())) {
                throw new DuplicatePlatformException(
                        String.format(DUPLICATE_PLATFORM_TEMPLATE, platformDTO.name())
                );
            }
            platform.setName(platformDTO.name());
        }

        if (platformDTO.description() != null) {
            platform.setDescription(platformDTO.description());
        }
        return platformRepository.save(platform);
    }
}
