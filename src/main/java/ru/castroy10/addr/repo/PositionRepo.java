package ru.castroy10.addr.repo;

import ru.castroy10.addr.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepo extends JpaRepository<Position, Integer> {
}
