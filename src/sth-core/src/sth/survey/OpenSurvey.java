package sth.survey;

import sth.survey.exceptions.NotEmptySurveyException;
import sth.survey.exceptions.CloseSurveyException;
import sth.survey.exceptions.FinishSurveyException;
import sth.survey.exceptions.AnswerSurveyException;

import java.io.Serializable;

public class OpenSurvey extends Survey.State implements Serializable {

	public OpenSurvey(Survey survey) {
		survey.super();
	}

	@Override
	public void close()
	throws CloseSurveyException	{
		setState(new CloseSurvey(getSurvey()));
	}
	
	@Override
	public boolean cancellable(String projectName, String disciplineName)
	throws NotEmptySurveyException, FinishSurveyException {
		if (getSurvey().someoneAnswered()) {
			throw new NotEmptySurveyException();
		}
		return true;
	}

	@Override
	public void answer(int id, int hoursSpent, String comment)
	 throws AnswerSurveyException {
		if (!getSurvey().alreadyAnswered(id))
			getSurvey().addAnswer(id, hoursSpent, comment);
	}

	@Override
	public String showSurveyResults(int subsNumber) {
		return " (aberto)";
	}

	@Override
	public String showStudentSurveyResults() {
		return " (aberto)";
	}

	@Override
	public String showSurvey() {
		return " (aberto)";
	}

}