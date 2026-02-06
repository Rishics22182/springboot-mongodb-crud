package com.app.analytics.service;

import com.app.analytics.dto.GetStudentDto;
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
    public List<GetStudentDto> getStudents(){
        Aggregation aggregation = Aggregation.newAggregation(
                group("city").count().as("total_student"), sort(Sort.Direction.DESC, "total_student"), project("total_student").and("_id").as("city")
        );
        AggregationResults<GetStudentDto> results = mongoTemplate.aggregate(aggregation, "student", GetStudentDto.class);
        return results.getMappedResults();
    }

}
