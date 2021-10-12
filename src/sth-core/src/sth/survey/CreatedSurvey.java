package sth.survey;

import sth.survey.exceptions.NotEmptySurveyException;
import sth.survey.exceptions.OpenSurveyException;
import sth.survey.exceptions.FinishSurveyException;

import java.io.Serializable;

public class CreatedSurvey extends Survey.State implements Serializable {
	public CreatedSurvey(Survey survey) {
		survey.super();
	}

	@Override
	public void projectClosing(String projectName, String disciplineName) {
		setState(new OpenSurvey(getSurvey()));
		getSurvey().notifyObservers("Pode preencher inquérito do projecto " + projectName + " da disciplina " + disciplineName);
	}

	@Override
	public void open()
	throws OpenSurveyException {
		setState(new OpenSurvey(getSurvey())); }

	@Override
	public boolean cancellable(String projectName, String disciplineName)
	throws NotEmptySurveyException, FinishSurveyException {
		return true;
	}

	@Override
	public String showSurveyResults(int subsNumber) {
		return " (por abrir)";
	}

	@Override
	public String showStudentSurveyResults() {
		return " (por abrir)";
	}

	@Override
	public String showSurvey() {
		return " (por abrir)";
	}
}