package com.nexus.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final RestockSystem restockSystem;

    public NotificationController(RestockSystem restockSystem) {
        this.restockSystem = restockSystem;
    }

    @GetMapping("/notifications")
    public List<String> getNotifications() {
        return restockSystem.getNotificationLogs();
    }

    @PostMapping("/request-restock")
    public ResponseEntity<String> requestRestock(
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "item", required = false) String item,
            @RequestBody(required = false) RestockRequest request) {

        String resolvedBranch = branch;
        String resolvedItem = item;

        if ((resolvedBranch == null || resolvedBranch.isBlank()) && request != null) {
            resolvedBranch = request.branch;
        }
        if ((resolvedItem == null || resolvedItem.isBlank()) && request != null) {
            resolvedItem = request.item;
        }

        if (resolvedBranch == null || resolvedBranch.isBlank() || resolvedItem == null || resolvedItem.isBlank()) {
            return ResponseEntity.badRequest().body("branch and item are required");
        }

        restockSystem.requestRestock(resolvedBranch, resolvedItem);
        return ResponseEntity.ok("OK");
    }

    public static class RestockRequest {
        public String branch;
        public String item;
    }
}
