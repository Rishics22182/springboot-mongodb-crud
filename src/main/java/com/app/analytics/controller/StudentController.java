package com.app.analytics.controller;

import com.app.analytics.dto.GetStudentDto;
import com.app.analytics.model.Student;
import com.app.analytics.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<String> createStudent(@RequestBody Student student) {
        try {
            studentService.addStudent(student);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Student created successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<GetStudentDto>> getAllStudent() {
        return ResponseEntity.ok(studentService.getStudents());
    }

    // Single file upload
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file) {

        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        System.out.println(file.getContentType());
        System.out.println(file.isEmpty());
        System.out.println(file.getResource());
        System.out.println(file.getName());

        String uploadDir = System.getProperty("user.dir") + "\\uploads\\";
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String path = uploadDir + file.getOriginalFilename();

        try {
            file.transferTo(new File(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(file.getOriginalFilename());
    }

    // Multiple file upload
    @Operation(summary = "uploads multiple files", description = "You can upload multiple format file like pdf, word, text and many more")
    @PostMapping(value = "/upload-multiple", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadMultiFile(
            @RequestPart("files") List<MultipartFile> files)
            throws IOException {

        String uploadDir = System.getProperty("user.dir") + "\\uploads\\";
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        files.forEach(file -> {
            try {
                String path = uploadDir + file.getOriginalFilename();
                file.transferTo(new File(path));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return ResponseEntity.ok("Files uploaded successfully");
    }

    // Upload two files with different keys: file & student
    @PostMapping(value = "/upload-two", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadTwoFiles(
            @RequestParam("file") MultipartFile file,
            @RequestParam("student") MultipartFile student) {

        String uploadDir = System.getProperty("user.dir") + "\\uploads\\";
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            String path1 = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(path1));

            String path2 = uploadDir + student.getOriginalFilename();
            student.transferTo(new File(path2));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("Both files uploaded successfully");
    }

    // Upload profile image and save key in DB
    @PostMapping(value = "/{id}/upload-profile", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadProfileImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        String uploadDir = System.getProperty("user.dir") + "\\uploads\\";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            String fileName = id + "_" + file.getOriginalFilename();
            String path = uploadDir + fileName;
            file.transferTo(new File(path));
            studentService.saveProfileImageKey(id, fileName);
            return ResponseEntity.ok("Profile image uploaded");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
