package sth.app.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import sth.app.exceptions.NoSuchPersonException;
import sth.exceptions.NoSuchPersonIdException;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.DialogException;
import sth.SchoolManager;

//FIXME import other classes if needed

/**
 * 4.1.1. Open existing document.
 */
public class DoOpen extends Command<SchoolManager> {

  //FIXME add input fields if needed
  Input<String> _fileName;
  
  /**
   * @param receiver
   */
  public DoOpen(SchoolManager receiver) {
    super(Label.OPEN, receiver);
    //FIXME initialize input fields if needed
    _fileName = _form.addStringInput(Message.openFile());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    _form.parse();

    try {
      //FIXME implement command
      _display.addLine(_receiver.open(_fileName.value()));
      _display.display();
    } catch (FileNotFoundException fnfe) {
      _display.popup(Message.fileNotFound());
    } catch (NoSuchPersonIdException nspe) {
      throw new NoSuchPersonException(nspe.getId());
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
  }

}
