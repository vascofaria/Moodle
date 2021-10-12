package sth.survey;

import sth.Subject;
import sth.Observer;

import sth.survey.exceptions.NotEmptySurveyException;
import sth.survey.exceptions.CloseSurveyException;
import sth.survey.exceptions.OpenSurveyException;
import sth.survey.exceptions.FinishSurveyException;
import sth.survey.exceptions.AnswerSurveyException;

import java.io.Serializable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

public class Survey extends Subject implements Serializable {
	private LinkedList<Integer> _hoursLst = new LinkedList<>();
	private HashSet<String> _comments = new HashSet<>();
	private HashSet<Integer> _ids = new HashSet<>();
	private State _state = new CreatedSurvey(this);

	public Survey(TreeMap<Integer, Observer> observers) {
		super(observers);
	}

	public abstract class State implements Serializable {

		protected void setState(State newState) { _state = newState; }
		protected Survey getSurvey() { return Survey.this; }

		public boolean cancellable(String projectName, String disciplineName)
		throws NotEmptySurveyException, FinishSurveyException {
			throw new FinishSurveyException();
		}

		public void projectClosing(String projectName, String disciplineName) { /* In general does nothing */ }

		public void open() throws OpenSurveyException {
			throw new OpenSurveyException();
		}
		public void close() throws CloseSurveyException	{
			throw new CloseSurveyException();
		}
		public void finish() throws FinishSurveyException {
			throw new FinishSurveyException();
		}
		public void answer(int id, int hoursSpent, String comment) throws AnswerSurveyException {
			throw new AnswerSurveyException();
		}
		public abstract String showSurveyResults(int subsNumber);
		public abstract String showStudentSurveyResults();
		public abstract String showSurvey();
	}

	public boolean alreadyAnswered(int id) { return _ids.contains(id); }
	public boolean someoneAnswered() { return (_ids.size() > 0); }

	public void addAnswer(int id, int hoursSpent, String comment) {
		if (!alreadyAnswered(id)) {
			_hoursLst.add(hoursSpent);
			_comments.add(comment);
			_ids.add(id);
		}
	}

	public int getMinHours() {
		int minHours = 0;
		if (_hoursLst.size() > 0)
			minHours = _hoursLst.get(0);
		for (int h : _hoursLst)
			if (h < minHours)
				minHours = h;
		return minHours;
	}
	public int getMaxHours() {
		int maxHours = 0;
		for (int h : _hoursLst)
			if (h > maxHours)
				maxHours = h;
		return maxHours;
	}
	public int getAnsNumber() {
		int ansNumber = 0;
		for (int h : _hoursLst)
            ansNumber++;
        return ansNumber;
	}
	public int getAverageHours() {
		int averageHours = 0, ansNumber = 0;
		for (int h : _hoursLst) {
            averageHours += h;
            ansNumber++;
        }
        if(ansNumber != 0)
            averageHours /= ansNumber;
        return averageHours;
	}

	public void notifyObservers(String msg) {
		super.notifyObservers(msg);
	}

	public void projectClosing(String projectName, String disciplineName) {
		_state.projectClosing(projectName, disciplineName);
	}

	public boolean cancellable(String projectName, String disciplineName)
	throws NotEmptySurveyException, FinishSurveyException {
		try { return _state.cancellable(projectName, disciplineName); }
		catch (NotEmptySurveyException nese) { throw nese; }
		catch (FinishSurveyException fse) { throw fse; }
	}
	public void open(String projectName, String disciplineName)
	throws OpenSurveyException {
		try { _state.open(); }
		catch (OpenSurveyException ose) { throw ose; }
		notifyObservers("Pode preencher inquérito do projecto " + projectName + " da disciplina " + disciplineName);
	}
	public void close()
	throws CloseSurveyException {
		try { _state.close(); }
		catch (CloseSurveyException cse) { throw cse; }
	}
	public void finish(String projectName, String disciplineName)
	throws FinishSurveyException {
		try { _state.finish(); }
		catch (FinishSurveyException fse) { throw fse; }
		notifyObservers("Resultados do inquérito do projecto " + projectName + " da disciplina " + disciplineName);
	}
	public void answer(int id, int hoursSpent, String comment)
	throws AnswerSurveyException {
		try { _state.answer(id, hoursSpent, comment); }
		catch(AnswerSurveyException sse) { throw sse; }
	}
	public String showSurveyResults(String disciplineName, String projectName, int subsNumber) {
		return disciplineName + " - " + projectName + _state.showSurveyResults(subsNumber);
	}
	public String showStudentSurveyResults(String disciplineName, String projectName) {
		return disciplineName + " - " + projectName + _state.showStudentSurveyResults();
	}

	public String showSurvey(String disciplineName, String projectName) {
        return disciplineName + " - " + projectName + _state.showSurvey();
    }
}