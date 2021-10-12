package sth.project.exceptions;

public class UnknownSubmissionException extends Exception {
	private int _id;

	public UnknownSubmissionException(int id) {
		_id = id;
	}

	public int getId() { return _id; }
}
