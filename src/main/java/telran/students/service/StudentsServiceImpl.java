package telran.students.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.students.dto.Mark;
import telran.students.dto.Student;
import telran.students.exception.StudentIllegalStateException;
import telran.students.exception.StudentNotFoundException;
import telran.students.model.StudentDoc;
import telran.students.repo.Id;
import telran.students.repo.IdPhone;
import telran.students.repo.StudentRepo;
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentsServiceImpl implements StudentsService {
	
	final StudentRepo studentRepo;
	
	@Override
	@Transactional
	public Student addStudent(Student student) {
		long id = student.id();
		if(studentRepo.existsById(id)) {
			log.error("student with id {} already exists", id);
			throw new StudentIllegalStateException();
		}
		StudentDoc studentDoc = new StudentDoc(student);
		studentRepo.save(studentDoc);
		log.debug("student {} has been saved", student);
		return student;
	}

	@Override
	public Mark addMark(long id, Mark mark) {
		StudentDoc studentDoc = studentRepo.findById(id)
				.orElseThrow(() -> new StudentNotFoundException());
		List<Mark> marks = studentDoc.getMarks();
		log.debug("student with id {}, has marks {} before adding new one",
				id, marks);
		marks.add(mark);
		StudentDoc savedStudent = studentRepo.save(studentDoc);
		log.debug("new marks after saving are {}", savedStudent.getMarks());
		return mark;
	}

	@Override
	@Transactional
	public Student updatePhoneNumber(long id, String phoneNumber) {
		StudentDoc studentDoc = studentRepo.findById(id)
				.orElseThrow(() -> new StudentNotFoundException());
		log.debug("student with id {}, old phone number {}, new phone number {}",
				id,studentDoc.getPhone(), phoneNumber);
		studentDoc.setPhone(phoneNumber);
		Student res = studentRepo.save(studentDoc).build();
		log.debug("Student {} has been saved ", res);
		return res;
	}

	@Override
	public Student removeStudent(long id) {
		StudentDoc studentDoc = studentRepo.findById(id)
				.orElseThrow(() -> new StudentNotFoundException());
		studentRepo.deleteById(id);
		log.debug("student {} has been deleted", studentDoc);
		return studentDoc.build();
	}

	@Override
	public Student getStudent(long id) {
		StudentDoc studentDoc = studentRepo.findStudentNoMarks(id);
		if(studentDoc == null) {
			throw new StudentNotFoundException();
		}
		log.debug("marks of found student {}", studentDoc.getMarks());	
		Student student = studentDoc.build();
		log.debug("found student {}", student);
		return student;
	}

	@Override
	public List<Mark> getMarks(long id) {
		StudentDoc studentDoc = studentRepo.findStudentOnlyMarks(id);
		if(studentDoc == null) {
			throw new StudentNotFoundException();
		}
		List<Mark> res = studentDoc.getMarks();
		log.debug("phone: {}, id: {}", studentDoc.getPhone(), studentDoc.getId());
		log.debug("marks of found student {}", res);	
		
		return res;
	}

	@Override
	public List<Student> getStudentsAllGoodMarks(int markThreshold) {
		// Will be implemented on the CW #72
		return null;
	}

	@Override
	public List<Student> getStudentsFewMarks(int nMarks) {
		// Will be implemented on the CW #72
		return null;
	}

	@Override
	public Student getStudentByPhoneNumber(String phoneNumber) {
		IdPhone idPhone = studentRepo.findByPhone(phoneNumber);
		
		Student res = null;
		if(idPhone != null) {
			res = new Student(idPhone.getId(), idPhone.getPhone());
		}
		   if (res == null) {
		        throw new NotFoundException("Student not found with phone number: " + phoneNumber);
		    }
		log.debug("student {}", res);
		return res;
	}

	@Override
	public List<Student> getStudentsByPhonePrefix(String prefix) {
		List<IdPhone> idPhones = studentRepo.findByPhoneRegex(prefix + ".+");
		List<Student> res = idPhones.stream()
				.map(ip -> new Student(ip.getId(), ip.getPhone())).toList();
		log.debug("students {}", res);
		return res;
	}

	@Override
	public List<Student> getStudentsMarksDate(LocalDate date) {
		List<IdPhone> students = studentRepo.findByLocalDate(date);
		List<Student> res = students.stream()
				.map(ip -> new Student(ip.getId(), ip.getPhone())).toList();
		log.debug("students {}", res);
		return res;
	}

	@Override
	public List<Student> getStudentsMarksMonthYear(int month, int year) {
		
		List<IdPhone> students = studentRepo.findByStudentsMarksMonthYear(month,year);
		List<Student> res = students.stream()
				.map(ip -> new Student(ip.getId(), ip.getPhone())).toList();
	    log.debug("Mapped students: {}", res);
		return res;
	}

	@Override
	public List<Student> getStudentsGoodSubjectMark(String subject, int markThreshold) {
		List<IdPhone> students = studentRepo.findByGoodMarksSubject(subject, markThreshold);
		List<Student> res = students.stream()
				.map(ip -> new Student(ip.getId(), ip.getPhone())).toList();
		log.debug("students {}", res);
		return res;
	}

}