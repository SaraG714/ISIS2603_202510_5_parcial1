package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.StudentService;
import uniandes.dse.examen1.services.RecordService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class })
public class RecordServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;


    private PodamFactory factory = new PodamFactoryImpl();

    private String login;
    private String courseCode;

    @BeforeEach
    void setUp() throws RepeatedCourseException, RepeatedStudentException {
        CourseEntity newCourse = factory.manufacturePojo(CourseEntity.class);
        newCourse = courseService.createCourse(newCourse);
        courseCode = newCourse.getCourseCode();

        StudentEntity newStudent = factory.manufacturePojo(StudentEntity.class);
        newStudent = studentService.createStudent(newStudent);
        login = newStudent.getLogin();
    }

    /**
     * Tests the normal creation of a record for a student in a course
     */
    @Test
    void testCreateRecord() throws InvalidRecordException{
        // TODO

        RecordEntity recordCreado = recordService.createRecord(login, courseCode, 2.0, "4");

        assertNotNull(recordCreado, "No debe ser nulo.");
    }
        
            /**
     * Tests the creation of a record when the login of the student is wrong
     */
    @Test
    void testCreateRecordMissingStudent() {
        // TODO
        StudentEntity studentMissing = factory.manufacturePojo(StudentEntity.class);


        assertThrows(InvalidRecordException.class, ()->{
            recordService.createRecord(studentMissing.getLogin(), courseCode, 2.0, "4");
        }); 
    }

    /**
     * Tests the creation of a record when the course code is wrong
     */
    @Test
    void testCreateInscripcionMissingCourse() {
        // TODO
        CourseEntity missingCourse = factory.manufacturePojo(CourseEntity.class);

        assertThrows(InvalidRecordException.class, ()->{
            recordService.createRecord(login, missingCourse.getCourseCode(), 2.0, "4");
        });

    }

    /**
     * Tests the creation of a record when the grade is not valid
     */
    @Test
    void testCreateInscripcionWrongGrade() {
        // TODO

        assertThrows(InvalidRecordException.class, ()->{
            recordService.createRecord(login, courseCode, 1.0, "4");
        });
    }

    /**
     * Tests the creation of a record when the student already has a passing grade
     * for the course
          * @throws InvalidRecordException 
          */
         @Test
         void testCreateInscripcionRepetida1() throws InvalidRecordException {
        // TODO

        recordService.createRecord(login, courseCode, 4.0, "4");

        assertThrows(InvalidRecordException.class, ()->{
            recordService.createRecord(login, courseCode, 3.0, "4");
        });


    }

    /**
     * Tests the creation of a record when the student already has a record for the
     * course, but he has not passed the course yet.
          * @throws InvalidRecordException 
          */
         @Test
         void testCreateInscripcionRepetida2() throws InvalidRecordException {
        // TODO

        RecordEntity record1 = recordService.createRecord(login, courseCode, 2.0, "4");
        RecordEntity record2 = recordService.createRecord(login, courseCode, 4.0, "5");

 
        assertEquals(1, studentRepository.findByLogin(login).get().getCourses().size());

        assertNotNull(record1);

        assertNotNull(record2);

    }
}
