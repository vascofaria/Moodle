package sth.person;

//import exceptions
import java.io.Serializable;

public class Teacher extends Person implements Serializable {

	public Teacher(String name, String phoneNumber, int id) {
		super(name, phoneNumber, id);
	}

	//public createProject()

	@Override
	public String toString() {
		return "DOCENTE" + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Teacher) {
			Teacher teacher = (Teacher) o;
			return super.equals(teacher);
		}
		return false;
	}
}
