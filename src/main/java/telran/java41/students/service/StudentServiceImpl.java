package telran.java41.students.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import telran.java41.students.dao.StudentRepository;
import telran.java41.students.dto.ScoreDto;
import telran.java41.students.dto.StudentCredentialsDto;
import telran.java41.students.dto.StudentDto;
import telran.java41.students.dto.UpdateStudentDto;
import telran.java41.students.dto.exceptions.StudentNotFoundException;
import telran.java41.students.model.Student;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    StudentRepository studentRepository;

    @Override
    public boolean addStudent(StudentCredentialsDto studentCredentialsDto) {
        Student student = studentRepository.findById(studentCredentialsDto.getId()).orElse(null);
        if (student != null) {
            return false;
        }
        student = new Student(studentCredentialsDto.getId(), studentCredentialsDto.getName(),
                studentCredentialsDto.getPassword());
        studentRepository.save(student);
        return true;
    }

    @Override
    public StudentDto findStudent(Integer id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        return StudentDto.builder()
                .id(id)
                .name(student.getName())
                .scores(student.getScores())
                .build();
    }

    @Override
    public StudentDto deleteStudent(Integer id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.deleteById(id);
        return StudentDto.builder()
                .id(id)
                .name(student.getName())
                .scores(student.getScores())
                .build();
    }

    @Override
    public StudentCredentialsDto updateStudent(Integer id, UpdateStudentDto updateStudentDto) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        if (updateStudentDto.getName() != null) {
            student.setName(updateStudentDto.getName());
        }
        if (updateStudentDto.getPassword() != null) {
            student.setPassword(updateStudentDto.getPassword());
        }
        studentRepository.save(student);
        return StudentCredentialsDto.builder()
                .id(id)
                .name(student.getName())
                .password(student.getPassword())
                .build();
    }

    @Override
    public boolean addScore(Integer id, ScoreDto scoreDto) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        boolean res = student.addScore(scoreDto.getExamName(), scoreDto.getScore());
        if (res) {
            studentRepository.save(student);
        }
        return res;
    }

    @Override
    public List<StudentDto> findStudentsByName(String name) {
        return studentRepository.findFirst10ByNameIgnoreCase(name).stream()
                .map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
                .collect(Collectors.toList());
    }

    @Override
    public long getStudentsNamesQuantity(List<String> names) {
        return studentRepository.findFirst10ByNameInIgnoreCase(names).size();
    }

    @Override
    public List<StudentDto> getStudentsByExamScore(String exam, int score) {
        return studentRepository.findStudentsByScores(exam, score).stream()
                .map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
                .collect(Collectors.toList());
    }

}
