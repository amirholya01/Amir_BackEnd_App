package com.amir.service;

import com.amir.exception.FileStorageException;
import com.amir.exception.VideoNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Service
public class FileStorageService {

    // Location to store files
    private final Path fileStorageLocation;

    // Allowed file extensions
    @Value("${amir.file.extension}")
    private String[] fileExtensions;

    // Constructor to set up file storage location
    public FileStorageService(){
        this.fileStorageLocation = Paths.get("uploads")
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        }catch (Exception exception){
            // If directory creation fails, throw an exception
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", exception);
        }
    }



    /**
     * Method to store a file.
     * @param file The file to be stored.
     * @return The filename.
     */
    public String storeFile(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")){
                // Check if the file name contains invalid characters
                throw new FileStorageException("Sorry! Filename contains invalid path seq" + fileName);
            }
            // Define the target location to save the file
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            // Copy the file to the target location
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch (IOException exception){
            // If file storage fails, throw an exception
            throw new FileStorageException("could not store file " + fileName + ". Pls try again!", exception);
        }
    }


    /**
     * Method to load a file as a resource.
     * @param filename The name of the file to be loaded.
     * @return The resource representing the file.
     */
    public Resource loadFileAsResource(String filename){
        try {
            // Resolve the file path
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            // Create a resource from the file path
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                // If file does not exist, throw an exception
                throw new VideoNotFoundException("Video not found, video name: " + filename);
            }
        } catch (MalformedURLException ex) {
            // If URL is malformed, throw an exception
            throw new VideoNotFoundException("Video not found, video name: " + filename);
        }
    }


    /**
     * Method to check if file format is supported.
     * @param fileContentType The content type of the file.
     * @return True if file format is supported, false otherwise.
     */
    public boolean checkFormat(String fileContentType) {
        // Split the content type to get the file format
        String[] fileFormat = fileContentType.split("/");
        // Check if the file format is video and if it is supported
        if (fileFormat[0].equals("video")) {
            if (Arrays.asList(fileExtensions).contains(fileFormat[1])) {
                return true;
            }
        }
        return false;
    }

}
