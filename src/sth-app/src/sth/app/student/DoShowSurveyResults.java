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

import sth.app.exceptions.NoSuchProjectException;
import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSurveyException;

/**
 * 4.4.3. Show survey results.
 */
public class DoShowSurveyResults extends Command<SchoolManager> {

  //FIXME add input fields if needed
  Input<String> _discipline;
  Input<String> _project;

  /**
   * @param receiver
   */
  public DoShowSurveyResults(SchoolManager receiver) {
    super(Label.SHOW_SURVEY_RESULTS, receiver);
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
      _display.addLine(_receiver.showSurveyResults(_discipline.value(), _project.value()));
      _display.display();
    }
    catch(UnknownDisciplineException | NotValidDisciplinePersonException e) {
      throw new NoSuchDisciplineException(_discipline.value());
    }
    catch (UnknownProjectException | UnknownSubmissionException e) {
      throw new NoSuchProjectException(_discipline.value(), _project.value());
    }
    catch (UnknownSurveyException e) {
      throw new NoSurveyException(_discipline.value(), _project.value());
    }
  }

}
