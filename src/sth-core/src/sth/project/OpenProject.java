package sth.project;

import sth.project.exceptions.UnknownProjectException;

import sth.survey.Survey;
import sth.survey.exceptions.DupSurveyException;

import sth.Observer;

import java.util.TreeMap;

import java.io.Serializable;

public class OpenProject extends Project.State implements Serializable {

	public OpenProject(Project project) {
		project.super();
	}

	@Override
	public void close() {
		setState(new CloseProject(getProject()));
	}

	@Override
	public void createSurvey(TreeMap<Integer, Observer> observers)
	throws DupSurveyException, UnknownProjectException {
		if (getProject().hasSurvey()) { throw new DupSurveyException(); }
		getProject().setSurvey(new Survey(observers));
	}

	@Override
	public void submit(int id, String submission) throws UnknownProjectException {
		getProject().addSubmission(id, submission);
	}
}