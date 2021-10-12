package sth.app.teaching;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;

//FIXME import other classes if needed
import sth.project.exceptions.UnknownProjectException;

import sth.exceptions.UnknownDisciplineException;
import sth.exceptions.NotValidDisciplinePersonException;
import sth.app.exceptions.NoSuchProjectException;
import sth.app.exceptions.NoSuchDisciplineException;

/**
 * 4.3.3. Show project submissions.
 */
public class DoShowProjectSubmissions extends Command<SchoolManager> {

  //FIXME add input fields if needed
  Input<String> _discipline;
  Input<String> _project;

  /**
   * @param receiver
   */
  public DoShowProjectSubmissions(SchoolManager receiver) {
    super(Label.SHOW_PROJECT_SUBMISSIONS, receiver);
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
      _display.addLine(_receiver.showProjectSubmissions(_discipline.value(), _project.value()));
      _display.display();
    }
    catch (UnknownDisciplineException | NotValidDisciplinePersonException ude) {
      throw new NoSuchDisciplineException(_discipline.value());
    }
    catch (UnknownProjectException upe) {
      throw new NoSuchProjectException(_discipline.value(), _project.value());
    }
  }

}
