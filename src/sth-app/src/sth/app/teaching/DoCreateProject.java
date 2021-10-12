package sth.app.teaching;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;

//FIXME import other classes if needed
import sth.project.exceptions.DupProjectException;

import sth.exceptions.UnknownDisciplineException;
import sth.exceptions.NotValidDisciplinePersonException;
import sth.app.exceptions.DuplicateProjectException;
import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSuchProjectException;

/**
 * 4.3.1. Create project.
 */
public class DoCreateProject extends Command<SchoolManager> {

  //FIXME add input fields if needed
  Input<String> _discipline;
  Input<String> _project;

  /**
   * @param receiver
   */
  public DoCreateProject(SchoolManager receiver) {
    super(Label.CREATE_PROJECT, receiver);
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
      _receiver.createProject(_discipline.value(), _project.value());
    }
    catch (UnknownDisciplineException | NotValidDisciplinePersonException e) {
      throw new NoSuchDisciplineException(_discipline.value());
    }
    catch (DupProjectException e) {
      throw new DuplicateProjectException(_discipline.value(), _project.value());
    }
  }

}
