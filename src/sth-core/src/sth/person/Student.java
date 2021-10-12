package sth.person;

//imports
import java.io.Serializable;

public class Student extends Person implements Serializable {

  public Student(String name, String phoneNumber, int id) {
    super(name, phoneNumber, id);
  }


  @Override
  public String toString() {
    return "ALUNO" + super.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Student) {
      Student student = (Student) o;
      return super.equals(student);
    }
    return false;
  }
}
