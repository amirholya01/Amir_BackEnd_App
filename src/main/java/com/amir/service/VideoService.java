package com.amir.service;

import com.amir.domain.Category;
import com.amir.domain.User;
import com.amir.domain.Video;
import com.amir.repository.CategoryRepository;
import com.amir.repository.UserRepository;
import com.amir.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> uploadVideo(MultipartFile file, String title, String categoryId){
        Video video = new Video();
        Category category = new Category();
        Optional<Category> categoryData = categoryRepository.findById(categoryId);
        if (categoryData.isPresent()){
            video.setCategory(categoryData.get());
            category = categoryData.get();
        }

        Optional<List<Video>> videoData = videoRepository.findByUrl(file.getOriginalFilename());
        if (videoData.isPresent()) {
            if (videoData.get().size() > 0) {
                // todo refactoring message for format not valid
                return new ResponseEntity<>("video name duplicate", HttpStatus.BAD_REQUEST);
            }
        }
            if (fileStorageService.checkFormat(file.getContentType())) {
                String fileName = fileStorageService.storeFile(file);
                User user = new User();
                Optional<User> userOpt = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                if (userOpt.isPresent()) {
                    user = userOpt.get();
                }

                video.setUser(user);
                video.setTitle(title);
                video.setSize(file.getSize());
                video.setUrl(fileName);

                Video videoCreated = videoRepository.save(video);

                return ResponseEntity.ok(video);
            }

        return new ResponseEntity<>("format not valid", HttpStatus.BAD_REQUEST);
    }
}
