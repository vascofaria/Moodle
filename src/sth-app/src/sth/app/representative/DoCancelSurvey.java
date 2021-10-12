package sth.app.representative;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;

//FIXME import other classes if needed
import sth.exceptions.UnknownDisciplineException;

import sth.project.exceptions.UnknownProjectException;

import sth.survey.exceptions.UnknownSurveyException;
import sth.survey.exceptions.NotEmptySurveyException;
import sth.survey.exceptions.FinishSurveyException;

import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSuchProjectException;
import sth.app.exceptions.NoSurveyException;
import sth.app.exceptions.NonEmptySurveyException;
import sth.app.exceptions.SurveyFinishedException;

/**
 * 4.5.2. Cancel survey.
 */
public class DoCancelSurvey extends Command<SchoolManager> {

  //FIXME add input fields if needed
  Input<String> _discipline;
  Input<String> _project;

  /**
   * @param receiver
   */
  public DoCancelSurvey(SchoolManager receiver) {
    super(Label.CANCEL_SURVEY, receiver);
    //FIXME initialize input fields if needed
    _discipline = _form.addStringInput(Message.requestDisciplineName());
    _project = _form.addStringInput(Message.requestProjectName());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    //FIXME implement command
    try {
      _form.parse();
      _receiver.cancelSurvey(_discipline.value(), _project.value());
    }
    catch (UnknownDisciplineException e) { throw new NoSuchDisciplineException(_discipline.value()); }
    catch (UnknownProjectException e) { throw new NoSuchProjectException(_discipline.value(), _project.value()); }
    catch (UnknownSurveyException e) { throw new NoSurveyException(_discipline.value(), _project.value()); }
    catch (NotEmptySurveyException e) { throw new NonEmptySurveyException(_discipline.value(), _project.value()); }
    catch (FinishSurveyException e) { throw new SurveyFinishedException(_discipline.value(), _project.value()); }
  }

}
