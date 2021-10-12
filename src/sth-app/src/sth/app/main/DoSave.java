package sth.app.main;

import java.io.IOException;
import java.io.FileNotFoundException;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;
import sth.SchoolManager;

//FIXME import other classes if needed

/**
 * 4.1.1. Save to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<SchoolManager> {
  //FIXME add input fields if needed
  Input<String> _fileName;

  /**
   * @param receiver
   */
  public DoSave(SchoolManager receiver) {
    super(Label.SAVE, receiver);
    //FIXME initialize input fields if needed
    _fileName = _form.addStringInput(Message.newSaveAs());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    //FIXME
    String file = null;
    if (!_receiver.hasLoadFile()) {
      _form.parse();
      file = _fileName.value();
    } else {
      file = new String();
    }

    try {
      _receiver.save(file);
    } catch (FileNotFoundException fnfe) {
      _display.popup(Message.fileNotFound());
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

}
