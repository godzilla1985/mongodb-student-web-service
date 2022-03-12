package telran.java41.students.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import telran.java41.students.model.Student;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student, Integer> {

    List<Student> findFirst10ByNameIgnoreCase(String name);

    List<Student> findFirst10ByNameInIgnoreCase(List<String> names);

    @Query("{'scores.?0':{$gt:?1}}")
    List<Student> findStudentsByScores(String exam, Integer score);

}
