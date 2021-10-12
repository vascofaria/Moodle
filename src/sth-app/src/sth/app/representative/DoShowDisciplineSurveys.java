package sth.app.representative;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;

//FIXME import other classes if needed

import sth.exceptions.UnknownDisciplineException;
import sth.exceptions.NotValidDisciplinePersonException;

import sth.app.exceptions.NoSuchDisciplineException;

/**
 * 4.5.6. Show discipline surveys.
 */
public class DoShowDisciplineSurveys extends Command<SchoolManager> {

  //FIXME add input fields if needed
  Input<String> _discipline;

  /**
   * @param receiver
   */
  public DoShowDisciplineSurveys(SchoolManager receiver) {
    super(Label.SHOW_DISCIPLINE_SURVEYS, receiver);
    //FIXME initialize input fields if needed
    _discipline = _form.addStringInput(Message.requestDisciplineName());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    try {
      _display.addLine(_receiver.showDisciplineSurveys(_discipline.value()));
      _display.display();
    }
    catch (UnknownDisciplineException | NotValidDisciplinePersonException e) {
      throw new NoSuchDisciplineException(_discipline.value());
    }

  }

}
