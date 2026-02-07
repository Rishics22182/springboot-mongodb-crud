package com.app.analytics.service;

import com.app.analytics.dto.GetStudentDto;
import com.app.analytics.dto.StudentResponseDTO;
import com.app.analytics.model.Student;
import com.app.analytics.repository.StudentRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class StudentService {
    @Autowired
    private final MongoTemplate mongoTemplate;
    private final StudentRepo studentRepo;
    public void addStudent(Student student){
        studentRepo.save(student);
        System.out.println(student.toString());
    }
    public List<StudentResponseDTO> getStudents(){
//        Aggregation aggregation = Aggregation.newAggregation(
//                group("city").count().as("total_student"), sort(Sort.Direction.DESC, "total_student"), project("total_student").and("_id").as("city")
//        );
//        AggregationResults<GetStudentDto> results = mongoTemplate.aggregate(aggregation, "student", GetStudentDto.class);
//        return results.getMappedResults();
        String baseUrl = "file:///" + System.getProperty("user.dir")
                .replace("\\", "/") + "/uploads/";
        List<Student> students = studentRepo.findAll();
        List<StudentResponseDTO> dtoList = new ArrayList<>();
        for (Student student : students) {
            StudentResponseDTO dto = new StudentResponseDTO();
            dto.setName(student.getName());
            dto.setAge(student.getAge());
            dto.setCity(student.getCity());
            dto.setProfileImageKey(student.getProfileImageKey());
            if (student.getProfileImageKey() != null && !student.getProfileImageKey().isEmpty()) {
                dto.setProfileImageUrl(baseUrl + student.getProfileImageKey());
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    public void saveProfileImageKey(String id, String fileName) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setProfileImageKey(fileName);
        studentRepo.save(student);
    }

    public String getProfileImageKey(String id) {

        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return student.getProfileImageKey();
    }


}
