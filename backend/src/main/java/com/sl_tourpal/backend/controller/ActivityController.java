package com.sl_tourpal.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sl_tourpal.backend.domain.Activity;
import com.sl_tourpal.backend.dto.ActivityDTO;
import com.sl_tourpal.backend.service.ActivityService;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/getAllActivity")
    public List<ActivityDTO> getAllActivities() {
        return activityService.getAllActivities()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/getActivity/{id}")
    public ResponseEntity<ActivityDTO> getActivityById(@PathVariable Long id) {
        return activityService.getActivityById(id)
                .map(activity -> ResponseEntity.ok(toDTO(activity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/addActivity")
    public ActivityDTO createActivity(@RequestBody ActivityDTO activityDTO) {
        Activity activity = toEntity(activityDTO);
        Activity savedActivity = activityService.createActivity(activity);
        return toDTO(savedActivity);
    }

    @PutMapping("/updateActivity/{id}")
    public ResponseEntity<ActivityDTO> updateActivity(@PathVariable Long id, @RequestBody ActivityDTO activityDTO) {
        try {
            Activity updatedActivity = activityService.updateActivity(id, toEntity(activityDTO));
            return ResponseEntity.ok(toDTO(updatedActivity));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteActivity/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getByRegion")
    public List<ActivityDTO> getActivitiesByRegion(@RequestParam List<String> regions) {
        return activityService.getActivitiesByRegions(regions)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Helper methods for mapping
    private ActivityDTO toDTO(Activity activity) {
        ActivityDTO dto = new ActivityDTO();
        dto.setId(activity.getId());
        dto.setName(activity.getName());
        dto.setRegion(activity.getRegion());
        // Add more fields if needed
        return dto;
    }

    private Activity toEntity(ActivityDTO dto) {
        Activity activity = new Activity();
        activity.setId(dto.getId());
        activity.setName(dto.getName());
        activity.setRegion(dto.getRegion());
        // Add more fields if needed
        return activity;
    }
}
