package sth.app.student;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;

//FIXME import other classes if needed
import sth.exceptions.UnknownDisciplineException;
import sth.exceptions.NotValidDisciplinePersonException;
import sth.exceptions.UnknownInscriptionException;

import sth.project.exceptions.UnknownProjectException;

import sth.app.exceptions.NoSuchDisciplineException;
import sth.app.exceptions.NoSuchProjectException;

/**
 * 4.4.1. Deliver project.
 */
public class DoDeliverProject extends Command<SchoolManager> {

  //FIXME add input fields if needed
  Input<String> _discipline;
  Input<String> _project;
  Input<String> _submission;

  /**
   * @param receiver
   */
  public DoDeliverProject(SchoolManager receiver) {
    super(Label.DELIVER_PROJECT, receiver);
    //FIXME initialize input fields if needed
    _discipline = _form.addStringInput(Message.requestDisciplineName());
    _project = _form.addStringInput(Message.requestProjectName());
    _submission = _form.addStringInput(Message.requestDeliveryMessage());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    try {
      _receiver.deliverProject(_discipline.value(), _project.value(), _submission.value());
    }
    catch (UnknownDisciplineException | NotValidDisciplinePersonException | UnknownInscriptionException e) {
      throw new NoSuchDisciplineException(_discipline.value());
    }
    catch (UnknownProjectException e) {
      throw new NoSuchProjectException(_discipline.value(), _project.value());
    }
  }

}
