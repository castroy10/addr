package ru.castroy10.addr.services;

import ru.castroy10.addr.model.Usr;
import ru.castroy10.addr.repo.UsrRepo;
import ru.castroy10.addr.security.UsrDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsrDetailsService implements UserDetailsService {
    private final UsrRepo usrRepo;

    public UsrDetailsService(UsrRepo usrRepo) {
        this.usrRepo = usrRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usr> user = usrRepo.findByUsername(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found!");
        return new UsrDetails(user.get());
    }
}
