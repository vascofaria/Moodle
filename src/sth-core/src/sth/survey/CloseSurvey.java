package sth.survey;

import sth.survey.exceptions.NotEmptySurveyException;
import sth.survey.exceptions.CloseSurveyException;
import sth.survey.exceptions.OpenSurveyException;
import sth.survey.exceptions.FinishSurveyException;

import java.io.Serializable;

public class CloseSurvey extends Survey.State implements Serializable {
	public CloseSurvey(Survey survey) {
		survey.super();
	}

	@Override
	public boolean cancellable(String projectName, String disciplineName)
	throws NotEmptySurveyException, FinishSurveyException {
		setState(new OpenSurvey(getSurvey()));
		getSurvey().notifyObservers("Pode preencher inquérito do projecto " + projectName + " da disciplina " + disciplineName);
		return false;
	}

	@Override
	public void open()
	throws OpenSurveyException {
		setState(new OpenSurvey(getSurvey()));
	}

	@Override
	public void close()
	throws CloseSurveyException {
	/* Does nothing */
	}

	@Override
	public void finish()
	throws FinishSurveyException {
		setState(new FinishSurvey(getSurvey()));
	}

	@Override
	public String showSurveyResults(int subsNumber) {
		return " (fechado)";
	}

	@Override
	public String showStudentSurveyResults() {
		return " (fechado)";
	}

	@Override
	public String showSurvey() {
		return " (fechado)";
	}
}