package com.WebVipers.gemini.controller;

import com.WebVipers.gemini.service.GeminiApiService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.logging.Logger;

@RestController
@RequestMapping("/gemini")
public class GeminiApiController {

    private static final Logger LOG = Logger.getLogger(GeminiApiController.class.getName());

    @Autowired
    private GeminiApiService geminiApiService;

    @PostMapping("/process-image")
    public ResponseEntity<HashMap<String, Object>> processImage(@RequestParam("file") MultipartFile file,
                                                                @RequestParam("prompt") String prompt) {
        LOG.info("\n\nINSIDE CLASS == GeminiApiController, METHOD == processImage(); ");

        try {
            JsonNode result = geminiApiService.getResponse(file, prompt);

            if(result != null) {
                LOG.info("\nImage processed successfully.");
                LOG.info("\nEXITING METHOD == processImage() OF CLASS == GeminiApiController \n\n");
                return getResponseFormat(HttpStatus.OK, "Image processed successfully", result);
            } else {
                LOG.info("\nImage processing failed.");
                LOG.info("\nEXITING METHOD == processImage() OF CLASS == GeminiApiController \n\n");
                return getResponseFormat(HttpStatus.INTERNAL_SERVER_ERROR, "Image processing failed", null);
            }
        } catch (Exception e) {
            LOG.severe("\nError in processImage() method of GeminiApiController: " + e.getMessage());
            LOG.info("\nEXITING METHOD == processImage() OF CLASS == GeminiApiController \n\n");
            return getResponseFormat(HttpStatus.INTERNAL_SERVER_ERROR, "Critical Error: " + e.getLocalizedMessage(), null);
        }
    }

    public ResponseEntity<HashMap<String, Object>> getResponseFormat(HttpStatus status, String message, Object data) {
        int responseStatus = (status.equals(HttpStatus.OK)) ? 1 : 0;

        HashMap<String, Object> map = new HashMap<>();
        map.put("responseCode", responseStatus);
        map.put("message", message);
        map.put("data", data);
        return ResponseEntity.status(status).body(map);
    }
}
