package ru.castroy10.addr.services;

import ru.castroy10.addr.model.Usr;
import ru.castroy10.addr.repo.UsrRepo;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsrService {

    private final UsrRepo usrRepo;
    private final PasswordEncoder passwordEncoder;

    public UsrService(UsrRepo usrRepo, PasswordEncoder passwordEncoder) {
        this.usrRepo = usrRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(String username, String password) {
        Usr user = new Usr();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        usrRepo.save(user);
    }

    public boolean findUserByUsername(String username) {
        return usrRepo.findByUsername(username).isPresent();
    }
}
