package sth.exceptions;

public class PersonIdAlreadyKnownException extends Exception {

  private int _id;

  /**
    * @param id
    */
  public PersonIdAlreadyKnownException(int id) {
  	_id = id;
  }

  /** @return id */
  public int getId() {
  	return _id;
  }
}