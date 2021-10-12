package sth.app.student;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;

//FIXME import other classes if needed
import sth.exceptions.NotValidDisciplinePersonException;
import sth.exceptions.UnknownDisciplineException;

import sth.project.exceptions.UnknownProjectException;
import sth.project.exceptions.UnknownSubmissionException;

import sth.survey.exceptions.UnknownSurveyException;
import sth.survey.exceptions.AnswerSurveyException;

import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSuchProjectException;
import sth.app.exceptions.NoSurveyException;

/**
 * 4.4.2. Answer survey.
 */
public class DoAnswerSurvey extends Command<SchoolManager> {

  //FIXME add input fields if needed
  Input<String> _discipline;
  Input<String> _project;
  Input<Integer> _hoursSpent;
  Input<String> _comment;

  /**
   * @param receiver
   */
  public DoAnswerSurvey(SchoolManager receiver) {
    super(Label.ANSWER_SURVEY, receiver);
    //FIXME initialize input fields if needed
    _discipline = _form.addStringInput(Message.requestDisciplineName());
    _project = _form.addStringInput(Message.requestProjectName());
    _hoursSpent = _form.addIntegerInput(Message.requestProjectHours());
    _comment = _form.addStringInput(Message.requestComment());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    //FIXME implement command
    try {
      _form.parse();
      _receiver.answerSurvey(_discipline.value(), _project.value(), _hoursSpent.value(), _comment.value());
    }
    catch (UnknownDisciplineException | NotValidDisciplinePersonException e) {
      throw new NoSuchDisciplineException(_discipline.value());
    }
    catch (UnknownProjectException | UnknownSubmissionException e) {
      throw new NoSuchProjectException(_discipline.value(), _project.value());
    }
    catch (UnknownSurveyException | AnswerSurveyException e) {
      throw new NoSurveyException(_discipline.value(), _project.value());
    }
  }

}
