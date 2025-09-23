package org.eoeqs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataController {

    @GetMapping("/data")
    public ResponseEntity<List<Map<String, String>>> getData() {
        List<Map<String, String>> data = List.of(
                Map.of("id", "1", "content", HtmlUtils.htmlEscape("Sample data 1 <script>alert('xss')</script>")),
                Map.of("id", "2", "content", HtmlUtils.htmlEscape("Sample data 2")),
                Map.of("id", "3", "content", HtmlUtils.htmlEscape("Sample data 3"))
        );
        return ResponseEntity.ok(data);
    }

    @PostMapping("/data")
    public ResponseEntity<String> createData(@RequestBody String data) {
        String sanitizedData = HtmlUtils.htmlEscape(data);
        return ResponseEntity.ok("Data created: " + sanitizedData);
    }

    @GetMapping("/user-info")
    public ResponseEntity<Map<String, String>> getUserInfo() {
        return ResponseEntity.ok(Map.of(
                "message", "This is protected user info",
                "status", "authenticated"
        ));
    }
}