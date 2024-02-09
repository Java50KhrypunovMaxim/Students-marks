package telran.students.exception;

import telran.students.service.ServiceErrorMessages;

@SuppressWarnings("serial")
public class StudentIllegalStateException extends IllegalStateException {
   public StudentIllegalStateException() {
	   super(ServiceErrorMessages.STUDENT_ALREADY_EXISTS);
   }
}