package uniandes.dse.examen1.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class StatsService {

    @Autowired
    StudentRepository estudianteRepository;

    @Autowired
    CourseRepository cursoRepository;

    @Autowired
    RecordRepository inscripcionRepository;

    public Double calculateStudentAverage(String login) {
        // TODO
        StudentEntity student = estudianteRepository.findByLogin(login).get();

        List<RecordEntity> records =  student.getRecords();

        Double suma = 0.0;
        int cant = 0;
        for(RecordEntity record: records){
            suma += record.getFinalGrade();
            cant += 1;
        }

        Double avg = suma/cant;

        return avg;
    }

    public Double calculateCourseAverage(String courseCode) {
        // TODO

        List<RecordEntity> records = inscripcionRepository.findAll();

        Double suma = 0.0;
        int cant = 0;
        for (RecordEntity record: records){
            if(record.getCourse().getCourseCode() == courseCode){
                suma += record.getFinalGrade();
                cant += 1;
            }
        }

        Double avg = suma/cant;

        return avg;

    }

}
