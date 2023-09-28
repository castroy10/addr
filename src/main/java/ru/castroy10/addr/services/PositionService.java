package ru.castroy10.addr.services;

import ru.castroy10.addr.model.Position;
import ru.castroy10.addr.repo.PositionRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {
    private final PositionRepo positionRepo;

    public PositionService(PositionRepo positionRepo) {
        this.positionRepo = positionRepo;
    }

    public List<Position> findAll() {
        return positionRepo.findAll();
    }

    public String getPositionName(List<Position> list, int id) {
        return list.stream()
                .filter(p -> p.getId() == id)
                .map(Position::getPosition_name)
                .findAny().orElse("");
    }

    public Position save(Position position) {
        return positionRepo.save(position);
    }

    public Position getPositionById(List<Position> list, int id) {
        return list.stream()
                .filter(p -> p.getId() == id)
                .findAny().orElse(new Position());
    }
}
