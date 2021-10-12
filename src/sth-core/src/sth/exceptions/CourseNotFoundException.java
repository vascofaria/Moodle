package sth.exceptions;

public class CourseNotFoundException extends Exception {

  private String _name;

  /**
    * @param name
    */
  public CourseNotFoundException(String name) {
  	_name = name;
  }

  /** @return name */
  public String getName() {
  	return _name;
  }
}