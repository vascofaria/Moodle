package sth.app.representative;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;

//FIXME import other classes if needed
import sth.exceptions.UnknownDisciplineException;

import sth.project.exceptions.UnknownProjectException;

import sth.survey.exceptions.UnknownSurveyException;
import sth.survey.exceptions.FinishSurveyException;

import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSuchProjectException;
import sth.app.exceptions.NoSurveyException;
import sth.app.exceptions.FinishingSurveyException;

/**
 * 4.5.5. Finish survey.
 */
public class DoFinishSurvey extends Command<SchoolManager> {

  //FIXME add input fields
  Input<String> _discipline;
  Input<String> _project;

  /**
   * @param receiver
   */
  public DoFinishSurvey(SchoolManager receiver) {
    super(Label.FINISH_SURVEY, receiver);
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
      _receiver.finishSurvey(_discipline.value(), _project.value());
    }
    catch (UnknownDisciplineException ude) { throw new NoSuchDisciplineException(_discipline.value()); }
    catch (UnknownProjectException upe) { throw new NoSuchProjectException(_discipline.value(), _project.value()); }
    catch (UnknownSurveyException use) { throw new NoSurveyException(_discipline.value(), _project.value()); }
    catch (FinishSurveyException fse) { throw new FinishingSurveyException(_discipline.value(), _project.value()); }
  }

}
