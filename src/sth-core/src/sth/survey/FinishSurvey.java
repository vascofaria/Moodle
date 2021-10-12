package sth.survey;

import sth.survey.exceptions.NotEmptySurveyException;
import sth.survey.exceptions.FinishSurveyException;

import java.io.Serializable;

public class FinishSurvey extends Survey.State implements Serializable {
	public FinishSurvey(Survey survey) {
		survey.super();
	}

	@Override
	public void finish()
	throws FinishSurveyException {
	/* Does nothing */
	}

	@Override
	public String showSurveyResults(int subsNumber) {
		String s = "\n * Número de submissões: " + subsNumber;
		s += "\n * Número de respostas: " + getSurvey().getAnsNumber();
		s += "\n * Tempos de resolução (horas) (mínimo, médio, máximo): " + getSurvey().getMinHours() + ", " 
		+ getSurvey().getAverageHours() + ", " + getSurvey().getMaxHours();
		return s;
	}

	@Override
	public String showStudentSurveyResults() {
		String s = "\n * Número de respostas: " + getSurvey().getAnsNumber();
		s += "\n * Tempo médio (horas): " + getSurvey().getAverageHours();
		return s;
	}

	@Override
	public String showSurvey() {
		return " - " + getSurvey().getAnsNumber() + " respostas - " + getSurvey().getAverageHours() + " horas";
	}

}