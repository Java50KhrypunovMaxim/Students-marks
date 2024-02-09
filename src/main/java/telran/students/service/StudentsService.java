package telran.students.service;

import telran.students.dto.Mark;
import telran.students.dto.Student;

public interface StudentsService {
	Student addStudent(Student student);
	Mark addMark(long id, Mark mark);
	Student updatePhoneNumber(long id, String phoneNumber);
}
