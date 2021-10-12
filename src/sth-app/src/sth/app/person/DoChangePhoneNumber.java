package sth.app.person;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;

//FIXME import other classes if needed

/**
 * 4.2.2. Change phone number.
 */
public class DoChangePhoneNumber extends Command<SchoolManager> {

  //FIXME add input fields if needed
  Input<String> _newPhoneNumber;

  /**
   * @param receiver
   */
  public DoChangePhoneNumber(SchoolManager receiver) {
    super(Label.CHANGE_PHONE_NUMBER, receiver);
    //FIXME initialize input fields if needed
    _newPhoneNumber = _form.addStringInput(Message.requestPhoneNumber());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    //FIXME implement command
    _form.parse();
    _display.addLine(_receiver.changePhoneNumber(_newPhoneNumber.value()));
    _display.display();
  }

}
