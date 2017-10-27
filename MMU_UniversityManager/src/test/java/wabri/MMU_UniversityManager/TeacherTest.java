package wabri.MMU_UniversityManager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class TeacherTest {

	Teacher teacher;
	private MailService mailService;
	private Course course;

	@Before
	public void init() {
		course = createNewCourse();
		mailService = mock(MailService.class);
		teacher = createNewTestTeacher("NameTest", "SurnameTest", "ID0");

		when(mailService.getMail(teacher)).thenReturn("Mail");
	}

	@Test
	public void testNewTeacherHasData() {
		String name = "Name";
		String surname = "Surname";
		String id = "ID";
		teacher = createNewTestTeacher(name, surname, id);

		assertEquals(name, teacher.getName());
		assertEquals(surname, teacher.getSurname());
		assertEquals(id, teacher.getId());
	}

	@Test
	public void testAskMailWhenMailIsNull() {
		assertMailTeacher("Mail");
	}

	@Test
	public void testAskMailWhenMailIsNotNull() {
		String mail = "MailTest";
		teacher.setMail(mail);

		assertMailTeacher(mail);
	}

	@Test
	public void testNewTeacherHasNoTeachCourse() {
		assertEquals(0, teacher.getListCoursesTeach().size());
	}

	@Test
	public void testAddTeachCourse() {
		teacher.addCourseTeach(course);

		assertEquals(1, teacher.getListCoursesTeach().size());
		assertEquals(course.getId(), teacher.getListCoursesTeach().get(0).getId());
	}

	@Test
	public void testRemoveTeachCourse() {
		teacher.addCourseTeach(course);
		teacher.removeCourseTeach(course.getId());

		assertEquals(0, teacher.getListCoursesTeach().size());
	}

	@Test(expected = NoTeachCoursesError.class)
	public void testRemoveTeachCourseWhenListIsEmpty() {
		teacher.removeCourseTeach("idCourse");

		assertEquals(0, teacher.getListCoursesTeach().size());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveTeachCourseWhenIdOfCourseIsNotRight() {
		teacher.addCourseTeach(course);
		teacher.removeCourseTeach("idError");

		assertEquals(1, teacher.getListCoursesTeach().size());
	}

	@Test
	public void testNewTeacherHasNoTutoredStudents() {
		assertEquals(0, teacher.getTutoredStudents().size());
	}

	@Test(expected = OutOfLimitTutoredStudents.class)
	public void testTutoredStudentsHasAMaximumOfThreeElements() {
		String idStudent0 = "idStudentTest0";
		teacher.addTutoredStudent(creteNewStudent(idStudent0));
		String idStudent1 = "idStudentTest1";
		teacher.addTutoredStudent(creteNewStudent(idStudent1));
		String idStudent2 = "idStudentTest2";
		teacher.addTutoredStudent(creteNewStudent(idStudent2));
		String idStudent3 = "idStudentTest3";
		teacher.addTutoredStudent(creteNewStudent(idStudent3));

		assertEquals(3, teacher.getTutoredStudents().size());
		assertEquals(idStudent0, teacher.getTutoredStudents().get(0));
		assertEquals(idStudent1, teacher.getTutoredStudents().get(1));
		assertEquals(idStudent2, teacher.getTutoredStudents().get(2));
	}

	@Test
	public void testAddNewTutorRequestUpdateListOfRequest() {
		TutorRequest tutorRequest = createNewTutorRequest("id0");
		teacher.addRequestedTutoring(tutorRequest);

		assertEquals(1, teacher.getRequestedTutor().size());
	}

	@Test(expected = OutOfLimitTutoredStudents.class)
	public void testAddNewTutorRequestIsNotAllowedWhenTutoredStudentAreAlreadyThree() {
		TutorRequest tutorRequest = createNewTutorRequest("idTutorRequestTest");
		teacher.addTutoredStudent(creteNewStudent("id0"));
		teacher.addTutoredStudent(creteNewStudent("id1"));
		teacher.addTutoredStudent(creteNewStudent("id2"));
		teacher.addRequestedTutoring(tutorRequest);

		assertEquals(false, teacher.getRequestedTutor().contains(tutorRequest));
	}

	@Test
	public void testAcceptTutorRequest() {
		String idStudent = "idStudent";
		teacher.addRequestedTutoring(createNewTutorRequest(idStudent));
		assertAcceptTutorRequest(idStudent, idStudent);
	}

	@Test(expected = NoTutorRequestError.class)
	public void testAcceptInexistentTutorRequest() {
		String idStudent = "idStudent";
		assertAcceptTutorRequest(null, idStudent);
	}

	@Test(expected = OutOfLimitTutoredStudents.class)
	public void testCantAcceptTutorRequestIfTutoredStudentAreAlreadyThree() {
		teacher.addTutoredStudent(creteNewStudent("id0"));
		teacher.addTutoredStudent(creteNewStudent("id1"));
		teacher.addTutoredStudent(creteNewStudent("id2"));

		assertAcceptTutorRequest("id0", "id3");
	}

	private void assertAcceptTutorRequest(String expected, String accept) {
		teacher.acceptTutorRequest(accept);

		assertEquals(expected, teacher.getTutoredStudents().get(0).getId());
	}

	private TutorRequest createNewTutorRequest(String idStudent) {
		return new TutorRequest(teacher, creteNewStudent(idStudent));
	}

	private Student creteNewStudent(String idStudent) {
		return new Student("nameStudentTest", "surnameStudentTest", idStudent, mailService, null);
	}

	private Course createNewCourse() {
		return new Course("idCourseTest", "nameCourseTest", teacher);
	}

	private void assertMailTeacher(String expected) {
		teacher.requestMail();

		assertEquals(expected, teacher.getMail());
	}

	private Teacher createNewTestTeacher(String name, String surname, String id) {
		return new Teacher(name, surname, id, mailService);
	}
}
