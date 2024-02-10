package telran.students;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.students.dto.Mark;
import telran.students.dto.Student;
import telran.students.exception.StudentIllegalStateException;
import telran.students.model.StudentDoc;
import telran.students.repo.StudentRepo;
import telran.students.service.StudentsService;

    @SpringBootTest
    class StudentsMarksServiceTests {
	
	@Autowired
	StudentsService studentsService;
	
	@Autowired
	StudentRepo studentRepo;
	
	private static final Long ID1 = 456l;
	private static final Long ID2 = 123l;
	private static final Long ID3 = 789l;
	
	private static final String PHONE1 = "050-90-888-60";
	private static final String PHONE2 = "063-42-456-12";
	private static final String PHONE3 = "068-12-566-34";
	
	private static final String SUBJECT1 = "Math";
	private static final String SUBJECT2 = "English";
	private static final String SUBJECT3 = "History";
	 
	private static final int SCORE1 = 100;
	private static final int SCORE2 = 80;
	private static final int SCORE3 = 60;
	
	private static final LocalDate DATE1 = LocalDate.of(2023, 1, 15);
	private static final LocalDate DATE2 = LocalDate.of(2023, 11, 10);
	private static final LocalDate DATE3 = LocalDate.of(2023, 7, 21);
	
	Student student1 = new Student (ID1,PHONE1);
	Student student2 = new Student (ID2,PHONE2);
	Student student3 = new Student (ID3,PHONE3);
	
	Mark mark1 = new Mark(SUBJECT1,SCORE1,DATE1);
	Mark mark2 = new Mark(SUBJECT2,SCORE2,DATE2);
	Mark mark3 = new Mark(SUBJECT3,SCORE3,DATE3);
	List<Mark> listOfMarks = new ArrayList();
	  
	@BeforeAll
	    static void setup(@Autowired StudentRepo studentRepo) {
	    studentRepo.deleteAll();
	    
	    }
	
	@Test
	void testAddStudent() {
		studentsService.addStudent(student1);
		assertTrue(studentRepo.existsById(ID1));
		assertFalse(studentRepo.existsById(ID2));
		assertThrowsExactly(StudentIllegalStateException.class,
				() -> 	studentsService.addStudent(student1));
	}
	
	@Test
	void testUpdatePhoneNumber() {
		studentsService.addStudent(student2);
		StudentDoc newStudent = studentRepo.findById(ID2).orElseThrow(() -> new RuntimeException("Student not found"));
		assertEquals(PHONE2, newStudent.getPhone());
		studentsService.updatePhoneNumber(ID2, PHONE3);
		StudentDoc updateStudent = studentRepo.findById(ID2).orElseThrow(() -> new RuntimeException("Student not found"));
		assertEquals(PHONE3, updateStudent.getPhone());
	}
	
	@Test
	void testAddMark() {
		studentsService.addStudent(student3);
		studentsService.addMark(ID3,mark1);
		StudentDoc newStudent = studentRepo.findById(ID3).orElseThrow(() -> new RuntimeException("Student not found"));
		listOfMarks.add(mark1);
		assertEquals(listOfMarks, newStudent.getMarks());
		studentsService.addMark(ID3,mark2);
		StudentDoc updateStudent = studentRepo.findById(ID3).orElseThrow(() -> new RuntimeException("Student not found"));
		listOfMarks.add(mark2);
		assertEquals(listOfMarks, updateStudent.getMarks());
		
	}
	
	
}