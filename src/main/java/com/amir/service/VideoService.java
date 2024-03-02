package com.amir.service;

import com.amir.domain.Category;
import com.amir.domain.User;
import com.amir.domain.Video;
import com.amir.repository.CategoryRepository;
import com.amir.repository.UserRepository;
import com.amir.repository.VideoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private UserRepository userRepository;
    public ResponseEntity<?> uploadVideo(MultipartFile file, String title, String categoryId) {
        Video video = new Video();
        Category category = new Category();

        Optional<Category> categoryData = categoryRepository.findById(categoryId);
        if(categoryData.isPresent()){
            video.setCategory(categoryData.get());
            category = categoryData.get();
        }

        Optional<List<Video>> videoData = videoRepository.findByUrl(file.getOriginalFilename());
        if (videoData.isPresent()) {
            if(videoData.get().size() > 0) {
                // todo refactoring message for format not valid
                return new ResponseEntity<>("video name duplicate", HttpStatus.BAD_REQUEST);
            }
        }

        // 1- todo: additional video name + date system for uniq
        // 2- add category
        if (fileStorageService.checkFormat(file.getContentType())) {
            String fileName = fileStorageService.storeFile(file);
            User user = new User();
            Optional<User> userOpt = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (userOpt.isPresent()) {
                user = userOpt.get();
            }
//            Video video = new Video(user, title, file.getSize(), fileName);
            video.setUser(user);
            video.setTitle(title);
            video.setSize(file.getSize());
            video.setUrl(fileName);

            Video videoCreated = videoRepository.save(video);
//            category.getVideos().add(videoCreated);
//            categoryRepository.save(category);
            return ResponseEntity.ok(video);
        }
        // todo refactoring message for format not valid
        return new ResponseEntity<>("format not valid", HttpStatus.BAD_REQUEST);
    }

    public List<Video> findAllVideo() {
        return videoRepository.findByUserUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public Video findById(String id, HttpServletRequest request) {
        Optional<Video> video = videoRepository.findById(id);
        return video.isPresent() ? video.get() : null;
    }

    public ResponseEntity<Resource> downloadFile(String id, HttpServletRequest request) {
        Optional<Video> video = videoRepository.findById(id);
        Resource resource = fileStorageService.loadFileAsResource(video.get().getUrl());

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // TODO: 2/7/24 logging

        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"", resource.getFilename() + "\"")
                .body(resource);

    }

    public void deleteVideoById(String id) {
        videoRepository.deleteById(id);
    }


    public List<Video> findVideoByCategoryId(String categoryId) {
        return videoRepository.findByCategoryId(categoryId);
    }
}
