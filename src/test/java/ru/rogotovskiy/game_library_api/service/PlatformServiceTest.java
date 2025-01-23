package ru.rogotovskiy.game_library_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rogotovskiy.game_library_api.dto.PlatformBasicDTO;
import ru.rogotovskiy.game_library_api.dto.PlatformDTO;
import ru.rogotovskiy.game_library_api.entity.Platform;
import ru.rogotovskiy.game_library_api.exceptions.DuplicatePlatformException;
import ru.rogotovskiy.game_library_api.exceptions.PlatformNotFoundException;
import ru.rogotovskiy.game_library_api.mapper.FacadeMapper;
import ru.rogotovskiy.game_library_api.mapper.PlatformMapper;
import ru.rogotovskiy.game_library_api.repository.PlatformRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatformServiceTest {
    @Mock
    private PlatformRepository platformRepository;

    @Mock
    private PlatformMapper platformMapper;

    @Mock
    private FacadeMapper facadeMapper;

    @InjectMocks
    private PlatformService platformService;

    private static final String PLATFORM_NOT_FOUND_TEMPLATE = "Платформа с id = %d не найдена!";
    private static final String DUPLICATE_PLATFORM_TEMPLATE = "Платформа с именем %s уже существует!";

    private static final Integer PLATFORM_ID = 1;
    private static final String PLATFORM_NAME = "PC";
    private static final String PLATFORM_DESCRIPTION = "Description";

    private Platform platform;
    private PlatformDTO platformDTO;
    private PlatformBasicDTO platformBasicDTO;

    @BeforeEach
    void setUp() {
        platform = new Platform(PLATFORM_ID, PLATFORM_NAME, PLATFORM_DESCRIPTION, Collections.emptyList());
        platformDTO = new PlatformDTO(PLATFORM_NAME, PLATFORM_DESCRIPTION, Collections.emptyList());
        platformBasicDTO = new PlatformBasicDTO(PLATFORM_NAME, PLATFORM_DESCRIPTION);
    }

    @Test
    void getAll_ShouldReturnListOfPlatforms() {
        when(platformRepository.findAll()).thenReturn(List.of(platform));

        List<Platform> result = platformService.getAll();

        assertEquals(1, result.size());
        assertEquals(platform, result.get(0));
        verify(platformRepository).findAll();
    }

    @Test
    void getPlatformById_ShouldReturnPlatform_WhenExists() {
        when(platformRepository.findById(PLATFORM_ID)).thenReturn(Optional.of(platform));

        Platform result = platformService.getPlatformById(PLATFORM_ID);

        assertEquals(platform, result);
        verify(platformRepository).findById(PLATFORM_ID);
    }

    @Test
    void getPlatformById_ShouldThrowException_WhenNotExists() {
        when(platformRepository.findById(PLATFORM_ID)).thenReturn(Optional.empty());

        PlatformNotFoundException e = assertThrows(
                PlatformNotFoundException.class,
                () -> platformService.getPlatformById(PLATFORM_ID)
        );

        assertEquals(String.format(PLATFORM_NOT_FOUND_TEMPLATE, PLATFORM_ID), e.getMessage());
        verify(platformRepository).findById(PLATFORM_ID);
    }

    @Test
    void getById_ShouldReturnPlatformDTO_WhenExists() {
        when(platformRepository.findById(PLATFORM_ID)).thenReturn(Optional.of(platform));
        when(facadeMapper.toDTO(platform)).thenReturn(platformDTO);

        PlatformDTO result = platformService.getById(PLATFORM_ID);

        assertEquals(platformDTO, result);
        verify(platformRepository).findById(PLATFORM_ID);
        verify(facadeMapper).toDTO(platform);
    }

    @Test
    void getById_ShouldThrowException_WhenNotExists() {
        when(platformRepository.findById(PLATFORM_ID)).thenReturn(Optional.empty());

        PlatformNotFoundException e = assertThrows(
                PlatformNotFoundException.class,
                () -> platformService.getById(PLATFORM_ID)
        );

        assertEquals(String.format(PLATFORM_NOT_FOUND_TEMPLATE, PLATFORM_ID), e.getMessage());
        verify(platformRepository).findById(PLATFORM_ID);
        verify(facadeMapper, never()).toDTO(platform);
    }

    @Test
    void createPlatform_ShouldSaveAndReturnPlatform() {
        when(platformRepository.existsByName(PLATFORM_NAME)).thenReturn(false);
        when(platformMapper.toEntity(platformBasicDTO)).thenReturn(platform);
        when(platformRepository.save(platform)).thenReturn(platform);

        Platform result = platformService.createPlatform(platformBasicDTO);

        assertEquals(platform, result);
        verify(platformRepository).existsByName(PLATFORM_NAME);
        verify(platformMapper).toEntity(platformBasicDTO);
        verify(platformRepository).save(platform);
    }

    @Test
    void createPlatform_ShouldThrowException_WhenNameExists() {
        when(platformRepository.existsByName(PLATFORM_NAME)).thenReturn(true);

        DuplicatePlatformException e = assertThrows(
                DuplicatePlatformException.class,
                () -> platformService.createPlatform(platformBasicDTO)
        );

        assertEquals(String.format(DUPLICATE_PLATFORM_TEMPLATE, PLATFORM_NAME), e.getMessage());
        verify(platformRepository).existsByName(PLATFORM_NAME);
        verify(platformMapper, never()).toEntity(platformBasicDTO);
        verify(platformRepository, never()).save(platform);
    }

    @Test
    void deletePlatform_ShouldDeletePlatform_WhenExists() {
        when(platformRepository.findById(PLATFORM_ID)).thenReturn(Optional.of(platform));

        platformService.deletePlatform(PLATFORM_ID);

        verify(platformRepository).findById(PLATFORM_ID);
        verify(platformRepository).delete(platform);
    }

    @Test
    void deletePlatform_ShouldThrowException_WhenNotExists() {
        when(platformRepository.findById(PLATFORM_ID)).thenReturn(Optional.empty());

        PlatformNotFoundException e = assertThrows(
                PlatformNotFoundException.class,
                () -> platformService.deletePlatform(PLATFORM_ID)
        );

        assertEquals(String.format(PLATFORM_NOT_FOUND_TEMPLATE, PLATFORM_ID), e.getMessage());
        verify(platformRepository).findById(PLATFORM_ID);
        verify(platformRepository, never()).delete(platform);
    }

    @Test
    void updatePlatform_ShouldUpdateAndReturnPlatform() {
        Platform updatedPlatform = new Platform(
                PLATFORM_ID,
                "PlayStation (PS)",
                "Updated description",
                Collections.emptyList()
        );

        when(platformRepository.findById(PLATFORM_ID)).thenReturn(Optional.of(platform));
        when(platformRepository.existsByName("PlayStation (PS)")).thenReturn(false);
        when(platformRepository.save(platform)).thenReturn(updatedPlatform);

        Platform result = platformService.updatePlatform(
                PLATFORM_ID,
                new PlatformBasicDTO("PlayStation (PS)", "Updated description")
        );

        assertEquals(updatedPlatform.getName(), result.getName());
        assertEquals(updatedPlatform.getDescription(), result.getDescription());
        verify(platformRepository).findById(PLATFORM_ID);
        verify(platformRepository).existsByName("PlayStation (PS)");
        verify(platformRepository).save(platform);
    }

    @Test
    void updatePlatform_ShouldThrowException_WhenNotExists() {
        when(platformRepository.findById(PLATFORM_ID)).thenReturn(Optional.empty());

        PlatformNotFoundException e = assertThrows(
                PlatformNotFoundException.class,
                () -> platformService.updatePlatform(
                        PLATFORM_ID,
                        new PlatformBasicDTO("PlayStation (PS)", "Updated description")
                )
        );

        assertEquals(String.format(PLATFORM_NOT_FOUND_TEMPLATE, PLATFORM_ID), e.getMessage());
        verify(platformRepository).findById(PLATFORM_ID);
        verify(platformRepository, never()).existsByName("PlayStation (PS)");
        verify(platformRepository, never()).save(platform);
    }

    @Test
    void updatePlatform_ShouldThrowException_WhenNameExists() {
        when(platformRepository.findById(PLATFORM_ID)).thenReturn(Optional.of(platform));
        when(platformRepository.existsByName("PlayStation (PS)")).thenReturn(true);

        DuplicatePlatformException e = assertThrows(
                DuplicatePlatformException.class,
                () -> platformService.updatePlatform(
                        PLATFORM_ID,
                        new PlatformBasicDTO("PlayStation (PS)", "Updated description")
                )
        );

        assertEquals(String.format(DUPLICATE_PLATFORM_TEMPLATE, "PlayStation (PS)"), e.getMessage());
        verify(platformRepository).findById(PLATFORM_ID);
        verify(platformRepository).existsByName("PlayStation (PS)");
        verify(platformRepository, never()).save(platform);
    }
}