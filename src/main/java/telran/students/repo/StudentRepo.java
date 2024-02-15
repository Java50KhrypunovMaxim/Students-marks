package telran.students.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import telran.students.model.StudentDoc;

public interface StudentRepo extends MongoRepository<StudentDoc,Long>{
@Query(value="{id:?0}", fields = "{id:1, phone:1}")
	StudentDoc findStudentNoMarks(long id);
@Query(value="{id:?0}", fields = "{id:0, marks:1}")
StudentDoc findStudentOnlyMarks(long id);
/********************************/
IdPhone findByPhone(String phone);
List<IdPhone> findByPhoneRegex(String regex);

@Query(value="{$and:[{marks: {$elemMatch:{subject: ?0,score:{$gte:?1}}}},"
		+ " {marks: {$not:{$elemMatch:{subject: ?0,score:{$lt:?1}}}}}]}")
List<IdPhone> findByGoodMarksSubject(String subject, int markThreshold);

@Query(value="{marks: {$elemMatch: {date: {$eq: ?0}}}}")
List<IdPhone> findByLocalDate(LocalDate date);

@Query(value = "{ $expr: { $anyElementTrue: { $map: { input: '$marks', as: 'mark', in: { $and: [ " +
        "{ $eq: [ { $year: '$$mark.date' }, ?1 ] }, " +
        "{ $eq: [ { $month: '$$mark.date' }, ?0 ] } ] } } } } }", fields = "{id:1, phone:1}")
List<IdPhone> findByStudentsMarksMonthYear(int month, int year);
}
