package sth.exceptions;

public class CourseAlreadyKnownException extends Exception {

  private String _name;

  /**
    * @param name
    */
  public CourseAlreadyKnownException(String name) {
  	_name = name;
  }

  /** @return name */
  public String getName() {
  	return _name;
  }
}