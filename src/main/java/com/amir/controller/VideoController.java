package com.amir.controller;

import com.amir.domain.Video;
import com.amir.service.VideoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("video")
public class VideoController {

    @Autowired
    private VideoService videoService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadVideo(@RequestParam("video")MultipartFile file, String title, String categoryId){
        return ResponseEntity.ok(videoService.uploadVideo(file, title, categoryId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<Video> videos() {
        return videoService.findAllVideo();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Video findById(@PathVariable("id") String id, HttpServletRequest request) {
        return videoService.findById(id, request);
    }

    @GetMapping("/download/{id:.+}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id, HttpServletRequest request) {
        return videoService.downloadFile(id, request);
    }

    // TODO: 2/13/24 response
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteVideoById(@PathVariable String id) {
        videoService.deleteVideoById(id);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Video>> findVideoByCategoryId(@PathVariable("id") String id) {
        return new ResponseEntity<>(videoService.findVideoByCategoryId(id), HttpStatus.OK);
    }

}
