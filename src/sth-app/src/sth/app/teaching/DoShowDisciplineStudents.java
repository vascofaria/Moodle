package sth.app.teaching;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;

//FIXME import other classes if needed
import sth.exceptions.UnknownDisciplineException;
import sth.exceptions.NotValidDisciplinePersonException;
import sth.app.exceptions.NoSuchDisciplineException;

/**
 * 4.3.4. Show course students.
 */
public class DoShowDisciplineStudents extends Command<SchoolManager> {

  //FIXME add input fields if needed
  Input<String> _discipline;

  /**
   * @param receiver
   */
  public DoShowDisciplineStudents(SchoolManager receiver) {
    super(Label.SHOW_COURSE_STUDENTS, receiver);
    //FIXME initialize input fields if needed
    _discipline = _form.addStringInput(Message.requestDisciplineName());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    //FIXME implement command
    try {
      _form.parse();
      _display.addLine(_receiver.showDisciplineStudents(_discipline.value()));
      _display.display();
    }
    catch (UnknownDisciplineException | NotValidDisciplinePersonException e) {
      throw new NoSuchDisciplineException(_discipline.value());
    }
  }

}
