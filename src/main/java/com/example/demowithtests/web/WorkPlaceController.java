package com.example.demowithtests.web;

import com.example.demowithtests.domain.WorkPlace;
import com.example.demowithtests.service.work_place.WorkPlaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "WorkPlace", description = "WorkPlace API")
public class WorkPlaceController {
    private final WorkPlaceService workPlaceService;

    @PostMapping("/place")
    public WorkPlace createWorkPlace(@RequestBody @Valid WorkPlace workPlace) {
        return workPlaceService.create(workPlace);
    }

    @GetMapping("/place/{id}")
    public ResponseEntity<WorkPlace> getWorkPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(workPlaceService.getWorkPlaceById(id));
    }

    @DeleteMapping("/place/{id}")
    public ResponseEntity<String> deleteWorkPlaceById(@PathVariable Long id) {
        String massage = String.format("Successfully! Work place by id: %s was deleted!", id);
        workPlaceService.deleteWorkplaceById(id);
        return ResponseEntity.ok().body(massage);
    }

    @GetMapping("/place")
    public ResponseEntity<List<WorkPlace>> getAllFreeWorkplace() {
        return ResponseEntity.ok().body(workPlaceService.getAllFreeWorkPlaces());
    }
}
