package com.esantamarina.assignmentscalableweb.controller;

import com.esantamarina.assignmentscalableweb.domain.BinaryData;
import com.esantamarina.assignmentscalableweb.domain.Diff;
import com.esantamarina.assignmentscalableweb.service.DiffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/diff")
public class DiffController {

    private static final Logger log = LoggerFactory.getLogger(DiffController.class);

    @Autowired
    private DiffService diffService;


    @PutMapping(value = "/{id}/left", produces = "application/json", consumes = "application/json")
    public void saveLeftValue(@PathVariable("id") String id, @RequestBody BinaryData data) {
        log.debug("Calling saveLeftValue with id {} and content {}", id, data);
        diffService.putLeftValue(id, data);
        log.info("Left input for diff with id {} successfully saved/updated", id);
    }

    @PutMapping(value = "/{id}/right", produces = "application/json", consumes = "application/json")
    public void saveRightValue(@PathVariable("id") String id, @RequestBody BinaryData data) {
        log.debug("Calling saveRightValue with id {} and content {}", id, data);
        diffService.putRightValue(id, data);
        log.info("Right input for diff with id {} successfully saved/updated", id);
    }

    @GetMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public Diff getDiff(@PathVariable("id") String id) throws ApiException {
        log.debug("Calling getDiff with id {}", id);
        Diff diff = diffService.calculateDiff(id);
        log.info("Diff with id {} successfully computed", id);
        return diff;
    }
}
