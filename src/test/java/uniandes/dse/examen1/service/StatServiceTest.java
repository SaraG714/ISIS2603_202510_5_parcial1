package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.RecordService;
import uniandes.dse.examen1.services.StatsService;
import uniandes.dse.examen1.services.StudentService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class, StatsService.class})
public class StatServiceTest {

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

    @Autowired 
    StatsService statsService;


    private String login;
    private String courseCode;


    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() throws RepeatedCourseException, RepeatedStudentException, InvalidRecordException {
        CourseEntity course1 = factory.manufacturePojo(CourseEntity.class);
        course1 = courseService.createCourse(course1);
        courseCode = course1.getCourseCode();
        CourseEntity course2 = factory.manufacturePojo(CourseEntity.class);
        course2 = courseService.createCourse(course2);

        StudentEntity student1 = factory.manufacturePojo(StudentEntity.class);
        student1 = studentService.createStudent(student1);
        login = student1.getLogin();
        StudentEntity student2 = factory.manufacturePojo(StudentEntity.class);
        student2 = studentService.createStudent(student2);

        recordService.createRecord(student1.getLogin(), course1.getCourseCode(), 2.0, "4");
        recordService.createRecord(student1.getLogin(), course1.getCourseCode(), 4.0, "5");
        recordService.createRecord(student1.getLogin(), course2.getCourseCode(), 3.0, "5");

        recordService.createRecord(student2.getLogin(), course1.getCourseCode(), 4.5, "2");



    }

    @Test
    void testStudentAverage(){

        Double avg = statsService.calculateStudentAverage(login);

        assertEquals(3.0, avg);

    }


    @Test
    void testCourseAverage(){

        Double avg = statsService.calculateCourseAverage(courseCode);

        assertEquals(3.5, avg);

    }
}
