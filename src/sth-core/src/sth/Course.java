package sth;

//import exceptions

import sth.person.Student;
import sth.person.Teacher;

import sth.Observer;

import sth.Discipline;
import sth.exceptions.UnknownDisciplineException;
import sth.exceptions.DupDisciplineException;
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

import java.io.Serializable;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Locale;
import java.text.Collator;

public class Course implements Serializable {

  private String _name;
  private Map<String, Discipline> _disciplines;
  private Map<Integer, Student> _students;
  private Map<Integer, Student> _representatives;

  public Course(String name) {
    _name = name;
    _disciplines = new TreeMap<String, Discipline>();
    _students = new TreeMap<Integer, Student>();
    _representatives = new TreeMap<Integer, Student>();
  }

  public void setName(String name) { _name = name; }
  public String getName() { return _name; }

  public void addDiscipline(Discipline discipline) {
    _disciplines.put(discipline.getName(), discipline);
  }
  public void addStudent(Student student) {
    _students.put(student.getId(), student);
  }
  public void addRepresentative(Student representative) {
    _representatives.put(representative.getId(), representative);
  }

  public boolean hasDiscipline(String disciplineName) {
    return _disciplines.containsKey(disciplineName);
  }
  public boolean hasStudent(int id) {
    return _students.containsKey(id);
  }
  public boolean hasRepresentative(int id) {
    return _representatives.containsKey(id);
  }

  public List<Discipline> getDisciplines() {
    List<Discipline> list = new LinkedList<Discipline>();
    list.addAll(_disciplines.values());
    return list;
  }
  public List<Student> getStudents() {
    List<Student> list = new LinkedList<Student>();
    list.addAll(_students.values());
    return list;
  }
  public List<Student> getRepresentatives() {
    List<Student> list = new LinkedList<Student>();
    list.addAll(_representatives.values());
    return list;
  }
  public Discipline getDiscipline(String name) {
    return _disciplines.get(name);
  }
  public Student getStudent(int id) {
    return _students.get(id);
  }
  public Student getRepresentative(int id) {
    return _representatives.get(id);
  }

  public void removeDiscipline(String name) {
    _disciplines.remove(name);
  }
  public void removeStudent(int id) {
    _students.remove(id);
  }
  public void removeRepresentative(int id) {
    _representatives.remove(id);
  }

  public String disciplinesToString(int id) {
    TreeMap<String, String> disciplines = new TreeMap<>(Collator.getInstance(Locale.getDefault()));
    String displayText = new String();

    for(Discipline discipline : getDisciplines()) {
      disciplines.put(discipline.getName(), discipline.disciplineToString(getName(), discipline, id));
    }

    for(String s : disciplines.values()) {
      displayText += s;
    }

    return displayText;
  }

   public boolean hasDisciplineMember(int id, String disciplineName) {
    if (hasDiscipline(disciplineName)) {
      Discipline discipline = getDiscipline(disciplineName);
      return (discipline.hasStudent(id) || discipline.hasTeacher(id));
    }
    return false;
  }

  /*-----------Discipline-------------------*/
  public void createDiscipline(String disciplineName)
  throws DupDisciplineException {
    if(hasDiscipline(disciplineName))
      throw new DupDisciplineException();
    Discipline newDiscipline = new Discipline(disciplineName);
    addDiscipline(newDiscipline);
  }

  public void destroyDiscipline(String disciplineName)
  throws UnknownDisciplineException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    removeDiscipline(disciplineName);
  }

  /*------------Project---------------------*/
  public void createProject(String projectName, String disciplineName)
  throws UnknownDisciplineException, DupProjectException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    Discipline discipline = getDiscipline(disciplineName);
    try { discipline.createProject(projectName); }
    catch (DupProjectException dpe) { throw dpe; }
  }

  public void closeProject(String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    Discipline discipline = getDiscipline(disciplineName);
    try { discipline.closeProject(projectName); }
    catch (UnknownProjectException upe) { throw upe; }
  }

  public void submitProject(int id, String submission, String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownInscriptionException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    Discipline discipline = getDiscipline(disciplineName);
    try { discipline.submitProject(id, submission, projectName); }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownInscriptionException uie) { throw uie; }
  }

  public String showProjectSubmissions(String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    Discipline discipline = getDiscipline(disciplineName);
    try { return discipline.showProjectSubmissions(projectName); }
    catch (UnknownProjectException upe) { throw upe; }
  }

  /*-------------Survey---------------------*/

  public void createSurvey(String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException, DupSurveyException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    TreeMap<Integer, Observer> observers = new TreeMap<>(_representatives);
    Discipline discipline = getDiscipline(disciplineName);
    try { discipline.createSurvey(observers, projectName); }
    catch (UnknownProjectException upe) { throw upe; }
    catch (DupSurveyException dse) { throw dse; }
  }

  public void openSurvey(String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, OpenSurveyException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    Discipline discipline = getDiscipline(disciplineName);
    try { discipline.openSurvey(projectName); }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (OpenSurveyException ose) { throw ose; }
  }

  public void cancelSurvey(String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, NotEmptySurveyException, FinishSurveyException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    Discipline discipline = getDiscipline(disciplineName);
    try { discipline.cancelSurvey(projectName); }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (NotEmptySurveyException nese) { throw nese; }
    catch (FinishSurveyException fse) { throw fse; }
  }

  public void closeSurvey(String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, CloseSurveyException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    Discipline discipline = getDiscipline(disciplineName);
    try { discipline.closeSurvey(projectName); }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (CloseSurveyException cse) { throw cse; }
  }

  public void finishSurvey(String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, FinishSurveyException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    Discipline discipline = getDiscipline(disciplineName);
    try { discipline.finishSurvey(projectName); }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (FinishSurveyException fse) { throw fse; }
  }

  public void answerSurvey(int id, int hoursSpent, String comment, String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException, UnknownSubmissionException,
  UnknownSurveyException, AnswerSurveyException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    Discipline discipline = getDiscipline(disciplineName);
    try { discipline.answerSurvey(id, hoursSpent, comment, projectName); }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSubmissionException use) { throw use; }
    catch (UnknownSurveyException use) { throw use; }
    catch (AnswerSurveyException ase) { throw ase; }
  }

  public String showSurveyResults(int id, String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, UnknownSubmissionException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    Discipline discipline = getDiscipline(disciplineName);
    try { return discipline.showSurveyResults(id, projectName); }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSubmissionException use) { throw use; }
    catch (UnknownSurveyException use) { throw use; }
  }

  public String showDisciplineSurveys(String disciplineName)
  throws UnknownDisciplineException {
    if (!hasDiscipline(disciplineName))
      throw new UnknownDisciplineException();
    Discipline discipline = getDiscipline(disciplineName);
    return discipline.showDisciplineSurveys();
  }
  
}