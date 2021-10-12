package sth;

import sth.person.Student;
import sth.person.Teacher;
import sth.person.Administrative;
import sth.person.Person;

import sth.Course;

import sth.exceptions.UnknownDisciplineException;
import sth.exceptions.UnknownInscriptionException;

import sth.project.exceptions.UnknownProjectException;
import sth.project.exceptions.DupProjectException;
import sth.project.exceptions.UnknownSubmissionException;

import sth.survey.exceptions.UnknownSurveyException;
import sth.survey.exceptions.NotEmptySurveyException;
import sth.survey.exceptions.DupSurveyException;
import sth.survey.exceptions.OpenSurveyException;
import sth.survey.exceptions.CloseSurveyException;
import sth.survey.exceptions.FinishSurveyException;
import sth.survey.exceptions.AnswerSurveyException;

import sth.exceptions.BadEntryException;
import sth.exceptions.ImportFileException;
import sth.exceptions.NotValidDisciplinePersonException;
import sth.exceptions.NoSuchPersonIdException;

//FIXME import other classes if needed
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;


/**
 * The façade class.
 */
public class SchoolManager {

  //FIXME add object attributes if needed
  private School _school;
  private int _login;
  private boolean _changed = true;
  private String _loadFile = new String();

  //FIXME implement constructors if needed
  public SchoolManager() {
    _school = new School();
  }
  
  /**
   * @param datafile
   * @throws ImportFileException
   * @throws InvalidCourseSelectionException
   */
  public void importFile(String datafile) throws ImportFileException {
    try {
      _school.importFile(datafile);
    } catch (IOException | BadEntryException e) {
      throw new ImportFileException();
    }
  }

  /**
   * @param id
   * @throws NoSuchPersonIdException
   */
  public String login(int id) throws NoSuchPersonIdException {
    //FIXME implement method
    _login = id;

    if (!(hasAdministrative() || hasProfessor() || hasStudent()))
      throw new NoSuchPersonIdException(id);
    return _school.displayNotifications(id);
  }

  /**
   * @return true when the currently logged in person is an administrative
   */
  public boolean hasAdministrative() {
    //FIXME implement predicate
    return _school.hasAdministrative(_login);
  }

  /**
   * @return true when the currently logged in person is a professor
   */
  public boolean hasProfessor() {
    //FIXME implement predicate
    return _school.hasTeacher(_login);
  }

  /**
   * @return true when the currently logged in person is a student
   */
  public boolean hasStudent() {
    //FIXME implement predicate
    return _school.hasStudent(_login);
  }

  /**
   * @return true when the currently logged in person is a representative
   */
  public boolean hasRepresentative() {
    //FIXME implement predicate
    return _school.hasRepresentative(_login);
  }

  public void setLoadFile(String loadFile) {
    _loadFile = loadFile;
  }

  public String getLoadFile() {
    return _loadFile;
  }

  public boolean hasLoadFile() {
    return !_loadFile.equals("");
  }

  //FIXME implement other methods (in general, one for each command in sth-app)

  //MAIN
  public String open(String fileName) throws IOException, ClassNotFoundException, FileNotFoundException, NoSuchPersonIdException {
    try {
    	School school;
      	ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
      	school = (School) ois.readObject();
      	ois.close();

     	if (!(school.hasAdministrative(_login) || school.hasTeacher(_login) || school.hasStudent(_login)))
        	throw new NoSuchPersonIdException(_login);
        _school = school;
    }
    catch (ClassNotFoundException cnfe) { throw cnfe; }
    catch (FileNotFoundException fnfe) { throw fnfe; }
    catch (NoSuchPersonIdException nspe) { throw nspe; }
    catch (IOException e) { throw e; }

    _loadFile = fileName;
    _changed = true;

    return login(_login);
  }  
  
  public void save(String fileName) throws IOException, FileNotFoundException {
    if (_changed) {
      if (!hasLoadFile())
        setLoadFile(fileName);
      try {
        ObjectOutputStream oos = new ObjectOutputStream(
          new BufferedOutputStream(
            new FileOutputStream(getLoadFile())));
        oos.writeObject(_school);
        oos.close();
      }
      catch (FileNotFoundException fnfe) { throw fnfe; }
      catch (IOException e) { throw e; }
      _changed = false;
    }
  }

  public String changePhoneNumber(String phoneNumber) {
    _changed = true;
    return _school.changePhoneNumber(phoneNumber, _login);
  }

  public String showPerson() {
    return _school.personToString(_login);
  }

  public String searchPerson(String name) {
  	return _school.searchPerson(name);
  }

  public String showAllPersons() {
  	return _school.showAllPersons();
  }
 
  //REPRESENTATIVE
  public void createSurvey(String disciplineName, String projectName)
  throws UnknownDisciplineException, UnknownProjectException,
  DupSurveyException {
    try { _school.createSurvey(_login, projectName, disciplineName); }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (DupSurveyException dse) { throw dse; }
    _changed = true;
  }

  public void openSurvey(String disciplineName, String projectName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, OpenSurveyException {
    try { _school.openSurvey(_login, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (OpenSurveyException ose) { throw ose; }
    _changed = true;
  }

  public void cancelSurvey(String disciplineName, String projectName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, NotEmptySurveyException,
  FinishSurveyException {
    try { _school.cancelSurvey(_login, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (NotEmptySurveyException nese) { throw nese; }
    catch (FinishSurveyException fse) { throw fse; }
    _changed = true;
  }

  public void closeSurvey(String disciplineName, String projectName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, CloseSurveyException {
    try { _school.closeSurvey(_login, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (CloseSurveyException cse) { throw cse; }
    _changed = true;
  }

  public void finishSurvey(String disciplineName, String projectName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, FinishSurveyException {
    try { _school.finishSurvey(_login, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (FinishSurveyException fse) { throw fse; }
    _changed = true;
  }


  public String showDisciplineSurveys(String disciplineName)
  throws UnknownDisciplineException,NotValidDisciplinePersonException {
    try { return _school.showDisciplineSurveys(_login, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (NotValidDisciplinePersonException nvdpe) { throw nvdpe; }
  }

  //STUDENT
  public void answerSurvey(String disciplineName, String projectName, int hoursSpent, String comment)
  throws NotValidDisciplinePersonException, UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, AnswerSurveyException , UnknownSubmissionException {
    try { _school.answerSurvey(_login, hoursSpent, comment, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (NotValidDisciplinePersonException nvdpe) { throw nvdpe; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSubmissionException use) { throw use; }
    catch (UnknownSurveyException use) { throw use; }
    catch (AnswerSurveyException ase) { throw ase; }
    _changed = true;
  }

  public void deliverProject(String disciplineName, String projectName, String submission)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownInscriptionException, NotValidDisciplinePersonException {
    try { _school.submitProject(_login, submission, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (NotValidDisciplinePersonException nvdpe) { throw nvdpe; }
    catch (UnknownInscriptionException uie) { throw uie; }
    catch (UnknownProjectException upe) { throw upe; }
    _changed = true;
  }

  public String showSurveyResults(String disciplineName, String projectName)
  throws NotValidDisciplinePersonException, UnknownDisciplineException,
  UnknownProjectException, UnknownSurveyException, UnknownSubmissionException {
    try { return _school.showSurveyResults(_login, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (NotValidDisciplinePersonException nvdpe) { throw nvdpe; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSubmissionException use) { throw use; }
    catch (UnknownSurveyException use) { throw use; }
  }

  //TEACHERS 1|^
  public void createProject(String disciplineName, String projectName) 
  throws UnknownDisciplineException, DupProjectException,
  NotValidDisciplinePersonException {
    try { _school.createProject(_login, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (NotValidDisciplinePersonException nvdpe) { throw nvdpe; }
    catch (DupProjectException dpe) { throw dpe; }
    _changed = true;
  }

  public void closeProject(String disciplineName, String projectName) 
  throws UnknownDisciplineException, UnknownProjectException,
  NotValidDisciplinePersonException {
    try { _school.closeProject(_login, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (NotValidDisciplinePersonException nvdpe) { throw nvdpe; }
    catch (UnknownProjectException upe) { throw upe; }
    _changed = true;
  }

  public String showDisciplineStudents(String disciplineName)
    throws UnknownDisciplineException, NotValidDisciplinePersonException {
    try { return _school.showDisciplineStudents(disciplineName, _login); }
    catch  (UnknownDisciplineException e) 	{ throw e; }
    catch  (NotValidDisciplinePersonException e) { throw e; }
  }

  public String showProjectSubmissions(String disciplineName, String projectName)
  throws UnknownDisciplineException, UnknownProjectException,
  NotValidDisciplinePersonException {
    try { return _school.showProjectSubmissions(_login, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (NotValidDisciplinePersonException nvdpe) { throw nvdpe; }
    catch (UnknownProjectException upe) { throw upe; }
  }

}
