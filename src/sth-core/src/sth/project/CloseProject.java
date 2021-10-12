package sth.project;

import sth.survey.exceptions.UnknownSurveyException;
import sth.survey.exceptions.OpenSurveyException;

import java.io.Serializable;

public class CloseProject extends Project.State implements Serializable {
	public CloseProject(Project project) {
		project.super();
	}

	public void openSurvey(String projectName, String disciplineName)
	throws OpenSurveyException, UnknownSurveyException {
		if (!getProject().hasSurvey()) { throw new UnknownSurveyException(); }
		try { getSurvey().open(projectName, disciplineName); }
		catch (OpenSurveyException ose) { throw ose; }
	}

	@Override
	public void close() { /* Does nothing */ }
}