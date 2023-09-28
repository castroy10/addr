package ru.castroy10.addr.repo;

import ru.castroy10.addr.model.Usr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsrRepo extends JpaRepository <Usr, Integer>{
    Optional<Usr> findByUsername (String username);
}
