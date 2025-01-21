package ru.rogotovskiy.game_library_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rogotovskiy.game_library_api.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    boolean existsByName(String name);
}
