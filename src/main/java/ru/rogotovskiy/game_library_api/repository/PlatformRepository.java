package ru.rogotovskiy.game_library_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rogotovskiy.game_library_api.entity.Platform;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Integer> {
    boolean existsByName(String name);
}
