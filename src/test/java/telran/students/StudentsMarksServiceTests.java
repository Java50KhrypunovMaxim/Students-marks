package telran.students;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import static telran.students.TestDb.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import telran.students.dto.*;
import telran.students.exception.StudentIllegalStateException;
import telran.students.exception.StudentNotFoundException;
import telran.students.model.StudentDoc;
import telran.students.repo.StudentRepo;
import telran.students.service.StudentsService;

@SpringBootTest

class StudentsMarksServiceTests {
	@Autowired
	StudentsService studentsService;
	@Autowired
	StudentRepo studentRepo;
	@Autowired
	TestDb testDb;
	@BeforeEach
	void setUp() {
		testDb.createDb();
		
	}

	@Test
	
	void addStudentTest() {
		Student valera = new Student(ID8, PHONE8);
		studentsService.addStudent(valera);
		assertEquals(valera, studentRepo.findById(ID8).orElseThrow().build());
		assertThrowsExactly(StudentIllegalStateException.class, ()->studentsService.addStudent(valera));
	}
	@Test

	void updatePhoneNumberTest() {
		studentsService.updatePhoneNumber(ID1, PHONE9);
		assertEquals(studentsService.getStudent(ID1).phone(),PHONE9);
		assertThrows(telran.exceptions.NotFoundException.class, ()->studentsService.getStudentByPhoneNumber(PHONE1));
	}
	
	@Test
	void addMarkTest() {
		Mark newMark = new Mark(SUBJECT1, 80, DATE1);
	    studentsService.addStudent(new Student(ID8, PHONE8));
	    studentsService.addMark(ID8, newMark);
	    StudentDoc newStudent = studentRepo.findById(ID8)
	            .orElseThrow(() -> new RuntimeException("Student not found"));
	    assertEquals(Collections.singletonList(newMark), newStudent.getMarks());
	}
	@Test
	void getStudentTest() {
		assertEquals(students[0], studentsService.getStudent(ID1));
		assertThrowsExactly(StudentNotFoundException.class, ()->studentsService.getStudent(100000));
	}
	@Test
	void getMarksTest() {
		assertArrayEquals(marks[0], studentsService.getMarks(ID1).toArray(Mark[]::new));
		assertThrowsExactly(StudentNotFoundException.class, ()->studentsService.getMarks(100000));
	}
	
	@Test
	void getStudentByPhoneNumberTest() {
		assertEquals(students[0], studentsService.getStudentByPhoneNumber(PHONE1));
	}
	@Test
	void getStudentsByPhonePrefixTest() {
		List<Student> expected = List.of(students[0], students[6]);
		assertIterableEquals(expected, studentsService.getStudentsByPhonePrefix("051"));
	}
	
	@Test
	void getStudentsMarksDateTest() {
		List<Student> expected = List.of(students[0], students[1], students[2], students[5]);
		assertIterableEquals(expected, studentsService.getStudentsMarksDate(DATE1));
		List<Student> expected1 = List.of(students[2], students[3], students[5]);
		assertIterableEquals(expected1, studentsService.getStudentsMarksDate(DATE4));
	}
	
	@Test
	void getStudentsMarksMonthYearTest() {
		List<Student> expected = List.of(students[0], students[1], students[2], students[5]);
		assertIterableEquals(expected, studentsService.getStudentsMarksMonthYear(1, 2024));	
	}
	
	@Test
	void getStudentsGoodSubjectMark() {
		List<Student> expected = List.of(students[5]);
		assertIterableEquals(expected, studentsService.getStudentsGoodSubjectMark(SUBJECT1, 100));
		List<Student> expected1 = List.of(students[0],students[1],students[5]);
		assertIterableEquals(expected1, studentsService.getStudentsGoodSubjectMark(SUBJECT2, 70));
	}
	

}