package wabri.MMU_UniversityManager;

import java.util.ArrayList;
import java.util.List;

public class Student {

	private String name;
	private String surname;
	private String id;
	private String mail;
	private String idTutor;
	private List<Course> enrolledCourse;
	private MailService mailService;
	private UniversityDB universityDB;

	public Student(String name, String surname, String id, MailService mailService, UniversityDB universityDB) {
		this.setName(name);
		this.setSurname(surname);
		this.setId(id);
		this.mailService = mailService;
		this.universityDB = universityDB;
		enrolledCourse = new ArrayList<Course>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void requestMail() {
		if (mail == null) {
			setMail(mailService.getMail(this));
		}
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getIdTutor() {
		return idTutor;
	}

	public void setIdTutor(String idTutor) {
		this.idTutor = idTutor;
	}

	public List<Course> getEnrolledCourse() {
		return enrolledCourse;
	}

	public void addEnrolledCourse(Course course) {
		enrolledCourse.add(course);
	}

	public void removeEnrolledCourse(String idCourse) throws NoEnrolledCourseWithThisId, NoEnrolledCourseError {
		if (enrolledCourse.isEmpty()) {
			throw new NoEnrolledCourseError();
		} else {
			try {
				int index = 0;
				while (enrolledCourse.get(index) != null) {
					if (idCourse == enrolledCourse.get(index).getId()) {
						enrolledCourse.remove(index);
						break;
					}
					index++;
				}
			} catch (IndexOutOfBoundsException e) {
				throw new NoEnrolledCourseWithThisId();
			}
		}
	}

	public void sendTutorRequest(String idTeacher, String message) throws IllegalTutorRequest {
		if (this.getIdTutor() != null) {
			throw new IllegalTutorRequest();
		}
		try {
			universityDB.studentRequestTutor(this, idTeacher);
			mailService.sendMail(this, universityDB.findTeacherWithId(idTeacher), message);
		} catch (NoTeacherFound e) {
			throw new IllegalTutorRequest();
		} catch (TutorRequestAlreadyActive e) {
			throw e;
		}
	}

	public void sendTutorRemoveRequest() throws NoTutorAssignedError {
		if (this.getIdTutor() == null) {
			throw new NoTutorAssignedError();
		}
		universityDB.studentRemoveTutor(this);
	}

	public void requestEnrollingCourse(String idCourse) {
		if (enrolledCourse.contains(universityDB.findCourseWithId(idCourse))) {
			throw new CourseAttendenceAlreadyActive();
		} else {			
			universityDB.studentRequestCourse(this, idCourse);
		}
	}

	public void requestRemoveEnrolledCourse(String idCourse) {
		universityDB.studentRemoveCourse(this, idCourse);
	}

	public void sendMailToTutor(String message) {
		mailService.sendMail(this, universityDB.findTeacherWithId(idTutor), message);
	}

}