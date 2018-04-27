package ru.mail.park.cherkov.db.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.park.cherkov.db.models.api.Status;
import ru.mail.park.cherkov.db.models.managers.ServiceManager;

@RestController
public class ServiceController {

    public ServiceController(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    private ServiceManager serviceManager;

    @PostMapping(value = "/api/service/clear", produces = "application/json")
    public ResponseEntity<Object> clean() {
        serviceManager.cleear();
        return new ResponseEntity<Object>(
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/api/service/status", produces = "application/json")
    public ResponseEntity<Status> getStatus() {
        return new ResponseEntity<Status>(
                serviceManager.getStatus(),
                HttpStatus.OK
        );
    }


}
