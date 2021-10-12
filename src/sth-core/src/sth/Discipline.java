package sth;

//import exceptions

import sth.person.Student;
import sth.person.Teacher;
import sth.person.Administrative;

import sth.Observer;

import sth.exceptions.UnknownInscriptionException;

import sth.project.Project;
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

public class Discipline implements Serializable {
  private String _name;
  private int _maxStudents = 30;
  private Map<Integer, Student> _students = new TreeMap<Integer, Student>();
  private Map<Integer, Administrative> _administratives = new TreeMap<Integer, Administrative>();
  private Map<Integer, Teacher> _teachers = new TreeMap<Integer, Teacher>();
  private Map<String, Project> _projects = new TreeMap<String, Project>();

  public Discipline(String name) {
    _name = name;
  }

  public Discipline(String name, int maxStudents) {
    this(name);
    _maxStudents = maxStudents;
  }

  public void setName(String name) { _name = name; }
  public void setMaxStudents(int maxStudents) { _maxStudents = maxStudents; }

  public String getName() { return _name; }
  public int getMaxStudents() { return _maxStudents; }

  public void addStudent(Student student) {
    _students.put(student.getId(), student);
  }
  public void addAdministrative(Administrative administrative) {
    _administratives.put(administrative.getId(), administrative);
  }
  public void addTeacher(Teacher teacher) {
    _teachers.put(teacher.getId(), teacher);
  }
  public void addProject(Project project) {
    _projects.put(project.getName(), project);
  }

  public Student getStudent(int id) {
    for (Student student : _students.values())
      if (student.getId() == id)
        return student;
    return null;
  }
  public Administrative getAdministrative(int id) {
    for (Administrative administrative : _administratives.values())
      if (administrative.getId() == id)
        return administrative;
    return null;
  }
  public Teacher getTeacher(int id) {
    for (Teacher teacher : _teachers.values())
      if (teacher.getId() == id)
        return teacher;
    return null;
  }
  public Project getProject(String name) {
    for (Project project : _projects.values())
      if (project.getName().equals(name))
        return project;
    return null;
  }
  public List<Student> getStudents() {
    List<Student> list = new LinkedList<Student>();
    list.addAll(_students.values());
    return list;
  }
  public List<Administrative> getAdministratives() {
    List<Administrative> list = new LinkedList<Administrative>();
    list.addAll(_administratives.values());
    return list;
  }
  public List<Teacher> getTeachers() {
    List<Teacher> list = new LinkedList<Teacher>();
    list.addAll(_teachers.values());
    return list;
  }
  public List<Project> getProjects() {
    List<Project> list = new LinkedList<Project>();
    list.addAll(_projects.values());
    return list;
  }
  public LinkedList<Observer> getDisciplineMembers() {
    LinkedList<Observer> list = new LinkedList<>();
    list.addAll(getStudents());
    list.addAll(getTeachers());
    return list;
  }

  public boolean hasStudent(int id) {
    return _students.containsKey(id);
  }
  public boolean hasAdministrative(int id) {
    return _students.containsKey(id);
  }
  public boolean hasTeacher(int id) {
    return _teachers.containsKey(id);
  }
  public boolean hasProject(String projectName) {
    return _projects.containsKey(projectName);
  }

  public void removeStudent(int id) {
    _students.remove(id);
  }
  public void removeAdministrative(int id) {
    _administratives.remove(id);
  }
  public void removeTeacher(int id) {
    _teachers.remove(id);
  }
  public void removeProject(String name) {
    _projects.remove(name);
  }

  public String disciplineToString(String courseName, Discipline discipline, int id) {
    String s = new String();
    if(hasTeacher(id) || hasAdministrative(id) || hasStudent(id)) {
      s = "* " + courseName + " - " + getName() + "\n";
    }
    return s;
  }

    /*----------Project---------------------*/
  public void createProject(String projectName)
  throws DupProjectException {
    Project newProject = new Project(projectName);
    if (hasProject(projectName))
      throw new DupProjectException();
    addProject(newProject);
  }

  public void closeProject(String projectName)
  throws UnknownProjectException {
    if (!hasProject(projectName))
      throw new UnknownProjectException();
    Project project = getProject(projectName);
    project.close(getName());
  }

  public void submitProject(int id, String submission, String projectName)
  throws UnknownProjectException, UnknownInscriptionException {
    if (!hasProject(projectName))
      throw new UnknownProjectException();
    if (!hasStudent(id))
      throw new UnknownInscriptionException();
    Project project = getProject(projectName);
    try { project.submit(id, submission); }
    catch (UnknownProjectException upe) { throw upe; }
  }

  public String showProjectSubmissions(String projectName)
  throws UnknownProjectException {
    if (!hasProject(projectName))
      throw new UnknownProjectException();
    Project project = getProject(projectName);
    return project.showSubmissions(getName());
  }

  /*-----------Survey---------------------*/

  public void createSurvey(TreeMap<Integer, Observer> observers, String projectName)
  throws UnknownProjectException, DupSurveyException {
    if (!hasProject(projectName))
      throw new UnknownProjectException();
    Project project = getProject(projectName);
    observers.putAll(_students);
    observers.putAll(_teachers);
    try { project.createSurvey(observers); }
    catch (UnknownProjectException upe) { throw upe; }
    catch (DupSurveyException dse) { throw dse; }
  }

  public void openSurvey(String projectName)
  throws UnknownProjectException, UnknownSurveyException, OpenSurveyException {
    if (!hasProject(projectName))
      throw new UnknownProjectException();
    Project project = getProject(projectName);
    try { project.openSurvey(projectName, getName()); }
    catch (UnknownSurveyException use) { throw use; }
    catch (OpenSurveyException ose) { throw ose; }
  }

  public void cancelSurvey(String projectName)
  throws UnknownProjectException, UnknownSurveyException,
  NotEmptySurveyException, FinishSurveyException {
    if (!hasProject(projectName))
      throw new UnknownProjectException();
    Project project = getProject(projectName);
    try { project.cancelSurvey(getName()); }
    catch (UnknownSurveyException use) { throw use; }
    catch (NotEmptySurveyException nese) { throw nese; }
    catch (FinishSurveyException fse) { throw fse; }
  }

  public void closeSurvey(String projectName)
  throws UnknownProjectException, UnknownSurveyException, CloseSurveyException {
    if (!hasProject(projectName))
      throw new UnknownProjectException();
    Project project = getProject(projectName);
    try { project.closeSurvey(); }
    catch (UnknownSurveyException use) { throw use; }
    catch (CloseSurveyException cse) { throw cse; }
  }

  public void finishSurvey(String projectName)
  throws UnknownProjectException, UnknownSurveyException, FinishSurveyException {
    if (!hasProject(projectName))
      throw new UnknownProjectException();
    Project project = getProject(projectName);
    try { project.finishSurvey(projectName, getName()); }
    catch (UnknownSurveyException use) { throw use; }
    catch (FinishSurveyException cse) { throw cse; }
  }

  public void answerSurvey(int id, int hoursSpent, String comment, String projectName)
  throws UnknownProjectException, AnswerSurveyException,
  UnknownSurveyException, UnknownSubmissionException {
    if (!hasProject(projectName))
      throw new UnknownProjectException();
    Project project = getProject(projectName);
    try { project.answerSurvey(id, hoursSpent, comment); }
    catch (UnknownSubmissionException use) { throw use; }
    catch (UnknownSurveyException use) { throw use; }
    catch (AnswerSurveyException ase) { throw ase; }
  }

  public String showSurveyResults(int id, String projectName)
  throws UnknownProjectException, UnknownSurveyException,
  UnknownSubmissionException {
    if (!hasProject(projectName))
      throw new UnknownProjectException();
    Project project = getProject(projectName);
    if (!project.hasSubmit(id) && hasStudent(id))
      throw new UnknownSubmissionException(id);
    try { 
      if (hasStudent(id))
        return project.showStudentSurveyResults(getName());
      else
        return project.showSurveyResults(getName()); }
    catch (UnknownSurveyException use) { throw use; }
  }

  public String showDisciplineSurveys() {
    String s = new String();
    TreeMap<String, String> surveys = new TreeMap<>(Collator.getInstance(Locale.getDefault()));
    for (Project project : getProjects())
      surveys.put(project.getName(), project.showDisciplineSurveys(_name));

    for (String str : surveys.values())
      s += str;

    return s;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Discipline) {
      Discipline discipline = (Discipline) o;
      return _name.equals(discipline.getName()) && _maxStudents == discipline.getMaxStudents();
    }
    return false;
  }
}