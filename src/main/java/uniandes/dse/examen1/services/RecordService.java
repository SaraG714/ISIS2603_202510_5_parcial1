package uniandes.dse.examen1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class RecordService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RecordRepository recordRepository;

    public RecordEntity createRecord(String loginStudent, String courseCode, Double grade, String semester)
            throws InvalidRecordException {
        // TODO

        RecordEntity newRecord = new RecordEntity();

        if (studentRepository.findByLogin(loginStudent).isEmpty())
            throw new InvalidRecordException("Estudiante debe existir");

        if (courseRepository.findByCourseCode(courseCode).isEmpty())
            throw new InvalidRecordException("Curso debe existir");


        StudentEntity student = studentRepository.findByLogin(loginStudent).get();
        CourseEntity course = courseRepository.findByCourseCode(courseCode).get();
            
        List<RecordEntity> recordsStudent = student.getRecords();

        for (RecordEntity record: recordsStudent){
            if (record.getCourse().getCourseCode() == courseCode && record.getFinalGrade()>= 3)
                throw new InvalidRecordException("El curso ya se había aprobado.");
        }

        if (grade < 1.5 || grade > 5)
            throw new InvalidRecordException("Nota debe estar entre 1.5 y 5.");


        newRecord.setStudent(student);
        newRecord.setCourse(course);
        newRecord.setFinalGrade(grade);
        newRecord.setSemester(semester);

        student.getRecords().add(newRecord);

        if (!student.getCourses().contains(course)){

            student.getCourses().add(course);
        }

        return recordRepository.save(newRecord);
    }
}
