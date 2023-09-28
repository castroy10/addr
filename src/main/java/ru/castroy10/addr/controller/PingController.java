package ru.castroy10.addr.controller;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Route("ping")
@AnonymousAllowed
@RestController
public class PingController extends VerticalLayout {

    private final DataSource dataSource;

    public PingController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/ping")
    public <T> ResponseEntity<?> getPing() {
        Map<String, Boolean> mapAnswer = new HashMap<>();
        mapAnswer.put("app", true);
        try (Connection connection = dataSource.getConnection()) {
            mapAnswer.put("bd", connection.isValid(3));
        } catch (SQLException e) {
            mapAnswer.put("bd", false);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(mapAnswer);
    }
}
