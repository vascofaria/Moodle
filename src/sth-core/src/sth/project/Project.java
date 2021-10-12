package sth.project;

import sth.person.Person;
import sth.Observer;

import sth.project.exceptions.UnknownProjectException;
import sth.project.exceptions.UnknownSubmissionException;

import sth.survey.Survey;
import sth.survey.exceptions.UnknownSurveyException;
import sth.survey.exceptions.NotEmptySurveyException;
import sth.survey.exceptions.DupSurveyException;
import sth.survey.exceptions.OpenSurveyException;
import sth.survey.exceptions.CloseSurveyException;
import sth.survey.exceptions.FinishSurveyException;
import sth.survey.exceptions.AnswerSurveyException;

import java.io.Serializable;

import java.util.TreeMap;
import java.util.LinkedList;

public class Project implements Serializable {
	private String _name;
	private String _description = "Projeto sem descrição.";
	private Survey _survey = null;
	private TreeMap<Integer, String> _submissions = new TreeMap<>();
	private State _state = new OpenProject(this);

	public Project(String name) {
		_name = name;
	}

	public Project(String name, String description) {
		this(name);
		_description = description;
	}

	public abstract class State implements Serializable {

		protected void setState(State newState) { _state = newState; }
		protected Project getProject() { return Project.this; }
		protected Survey getSurvey() { return _survey; }

		public void createSurvey(TreeMap<Integer, Observer> observers)
		throws DupSurveyException, UnknownProjectException {
			throw new UnknownProjectException();
		}

		public void openSurvey(String projectName, String disciplineName)
		throws OpenSurveyException, UnknownSurveyException {
			throw new OpenSurveyException();
		}

		public abstract void close();
		public void submit(int id, String submission) throws UnknownProjectException {
			throw new UnknownProjectException();
		}
	}

	public void close(String disciplineName) {
		if (hasSurvey())
			_survey.projectClosing(getName(), disciplineName);
		_state.close();
	}
	public void submit(int id, String submission) throws UnknownProjectException {
		try { _state.submit(id, submission); }
		catch (UnknownProjectException spe) { throw spe; }
	}

	public void setName(String name) { _name = name; }
	public void setDescription(String description) { _description = description; }
	public void setSurvey(Survey survey) { _survey = survey; }

	public String getName() { return _name; }
	public String getDescription() { return _description; }
	public Survey getSurvey() { return _survey; }
	public TreeMap<Integer, String> getSetSubmissions() { return _submissions; }
	public String getSubmission(int id) { return _submissions.get(id); }
	public LinkedList<String> getSubmissions() {
		LinkedList<String> list = new LinkedList<String>();
		list.addAll(_submissions.values());
		return list;
	}
	public int getSubmissionsNumber() {
		return _submissions.size();
	}

	public void addSubmission(int id, String submission) {
		_submissions.put(id, submission);
	}

	public boolean hasSubmit(int id) {
		return _submissions.containsKey(id);
	}

	public boolean hasSurvey() {
		return _survey != null;
	}


	public void createSurvey(TreeMap<Integer, Observer> observers)
	throws DupSurveyException, UnknownProjectException {
		try { _state.createSurvey(observers); }
		catch (UnknownProjectException upe) { throw upe; }
		catch (DupSurveyException dse) { throw dse; }
	}

	public void openSurvey(String projectName, String disciplineName)
	throws OpenSurveyException, UnknownSurveyException {
		try { _state.openSurvey(projectName, disciplineName); }
		catch (UnknownSurveyException use) { throw use; }
		catch (OpenSurveyException ose) { throw ose; }
	}

	public void cancelSurvey(String disciplineName)
	throws NotEmptySurveyException, UnknownSurveyException,
	FinishSurveyException {
		if (!hasSurvey()) { throw new UnknownSurveyException(); }
		try {
			if (_survey.cancellable(getName(), disciplineName))
				setSurvey(null);
		}
		catch (NotEmptySurveyException nese) { throw nese; }
		catch (FinishSurveyException fse) { throw fse; }
	}

	public void closeSurvey()
	throws CloseSurveyException, UnknownSurveyException {
		if (!hasSurvey()) { throw new UnknownSurveyException(); }
		try { _survey.close(); }
		catch (CloseSurveyException cse) { throw cse; }
	}

	public void finishSurvey(String projectName, String disciplineName)
	throws FinishSurveyException, UnknownSurveyException {
		if (!hasSurvey()) { throw new UnknownSurveyException(); }
		try { _survey.finish(projectName, disciplineName); }
		catch (FinishSurveyException fse) { throw fse; }
	}

	public void answerSurvey(int id, int hoursSpent, String comment)
	throws AnswerSurveyException, UnknownSurveyException,
	UnknownSubmissionException {
		if (!hasSubmit(id)) { throw new UnknownSubmissionException(id); }
		if (!hasSurvey()) { throw new UnknownSurveyException(); }
		try { _survey.answer(id, hoursSpent, comment); }
		catch (AnswerSurveyException ase) { throw ase; }
	}

	public String showSurveyResults(String disciplineName)
	throws UnknownSurveyException {
		if (!hasSurvey()) { throw new UnknownSurveyException(); }
		return _survey.showSurveyResults(disciplineName, getName(), getSubmissionsNumber());
	}

	public String showStudentSurveyResults(String disciplineName)
	throws UnknownSurveyException {
		if (!hasSurvey()) { throw new UnknownSurveyException(); }
		return _survey.showStudentSurveyResults(disciplineName, getName());
	}

	public String showSubmissions(String disciplineName) {
		return disciplineName + " - " + toString();
	}

	public String showDisciplineSurveys(String disciplineName) {
        if (hasSurvey())
            return _survey.showSurvey(disciplineName, getName());
        return new String();
    }

	@Override
	public String toString() {
		String s = getName();
		for (int id : getSetSubmissions().keySet())
			s += "\n* " + id + " - " + getSetSubmissions().get(id);
		return s;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Project) {
			Project project = (Project) o;
			return getName().equals(project.getName());
		}
		return false;
	}
}