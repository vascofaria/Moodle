package sth.person;

//import
import java.io.Serializable;

public class Administrative extends Person implements Serializable {

	public Administrative(String name, String phoneNumber, int id) {
		super(name, phoneNumber, id);
	}

	@Override
	public String toString() {
		return "FUNCIONÁRIO" + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Administrative) {
			Administrative administrative = (Administrative) o;
			return super.equals(administrative);
		}
		return false;
	}
}
