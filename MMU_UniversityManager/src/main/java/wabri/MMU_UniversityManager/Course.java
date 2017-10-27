package wabri.MMU_UniversityManager;

import java.util.ArrayList;
import java.util.List;

public class Course {

	private String id;
	private String name;
	private Teacher teacher;
	private List<Student> enrolledStudent;

	public Course(String id, String name, Teacher teacher) {
		this.setId(id);
		this.setName(name);
		this.setTeacher(teacher);
		enrolledStudent = new ArrayList<Student>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getMail() {
		return teacher.getMail();
	}

	public String getIdTeacher() {
		return teacher.getId();
	}

	public List<Student> getEnrolledStudent() {
		return enrolledStudent;
	}

}
