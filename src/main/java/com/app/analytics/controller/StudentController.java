package com.app.analytics.controller;

import com.app.analytics.dto.GetStudentDto;
import com.app.analytics.model.Student;
import com.app.analytics.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<String> createStudent(@RequestBody Student student){
        try{
            studentService.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student created successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<GetStudentDto>> getAllStudent(){
        return ResponseEntity.ok(studentService.getStudents());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadfile(@RequestParam("file") MultipartFile file){
        System.out.println(file.getOriginalFilename());
        return ResponseEntity.ok(file.getOriginalFilename());
    }
}