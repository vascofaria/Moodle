package sth;

//FIXME import other classes if needed

import sth.person.Person;
import sth.person.Student;
import sth.person.Teacher;
import sth.person.Administrative;

import sth.Course;
import sth.exceptions.UnknownCourseException;
import sth.exceptions.DupCourseException;

import sth.exceptions.UnknownDisciplineException;
import sth.exceptions.DupDisciplineException;
import sth.exceptions.UnknownInscriptionException;
import sth.exceptions.UnknownDisciplineException;
import sth.exceptions.NotValidDisciplinePersonException;

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
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Locale;
import java.text.Collator;
import java.util.Collections;

import sth.exceptions.BadEntryException;

/**
 * School implementation.
 */
public class School implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201810051538L;

  //FIXME define object fields (attributes and, possibly, associations)
  private int _nextId = 100000;
  private Map<String, Course> _courses;
  private Map<Integer, Student> _students;
  private Map<Integer, Administrative> _administratives;
  private Map<Integer, Teacher> _teachers;

  //FIXME implement constructors if needed
  public School() {
    _courses = new TreeMap<>();
    _students = new TreeMap<>();
    _administratives = new TreeMap<>();
    _teachers = new TreeMap<>();
  }
  
  /**
   * @param filename
   * @throws BadEntryException
   * @throws IOException
   */
  public void importFile(String fileName) throws IOException, BadEntryException {
    //FIXME implement text file reader
    int id=99999;
    boolean isLastStudentRepresentative=false;
    String name, phoneNumber;
    String[] parts;

    try {
      BufferedReader in = new BufferedReader(new FileReader(fileName));
      String line = in.readLine();

      while (line != null) {
        parts = line.split("\\|");

        if (parts[0].equals("ALUNO")) {
          id = Integer.parseInt(parts[1]);
          phoneNumber = parts[2];
          name = parts[3];
          addStudent(new Student(name, phoneNumber, id));
          isLastStudentRepresentative = false;

        } else if (parts[0].equals("DELEGADO")) {
          id = Integer.parseInt(parts[1]);
          phoneNumber = parts[2];
          name = parts[3];
          addStudent(new Student(name, phoneNumber, id));
          isLastStudentRepresentative = true;

        } else if (parts[0].equals("DOCENTE")) {
          id = Integer.parseInt(parts[1]);
          phoneNumber = parts[2];
          name = parts[3];
          addTeacher(new Teacher(name, phoneNumber, id));

        } else if (parts[0].equals("FUNCIONÁRIO")) {
          id = Integer.parseInt(parts[1]);
          phoneNumber = parts[2];
          name = parts[3];
          addAdministrative(new Administrative(name, phoneNumber, id));

        } else if (parts[0].charAt(0) == '#') {
          parts[0] = parts[0].replace("# ", "");
          String courseName = parts[0];
          String disciplineName = parts[1];

          addCourse(new Course(courseName));
          Course course = getCourse(courseName);

          if (!course.hasDiscipline(disciplineName))
            course.addDiscipline(new Discipline(disciplineName, 30));

          Discipline discipline = course.getDiscipline(disciplineName);

          if (hasStudent(id)) {
            Student student = getStudent(id);
            course.addStudent(student);
            discipline.addStudent(student);

            if (isLastStudentRepresentative)
              course.addRepresentative(student);

          } else if (hasTeacher(id)) {
            Teacher teacher = getTeacher(id);
            discipline.addTeacher(teacher);
          }

        } else {
          throw new BadEntryException(line);
        }
        line = in.readLine();
      }
      in.close();
      setNextId(id+1);
    }
    catch (BadEntryException e) { throw e; }
    catch (IOException e) { throw e; }
  }
  
  //FIXME implement other methods
  public void setNextId(int id) { _nextId = id; }
  public int getNextId() { return _nextId; }

  public void addCourse(Course course) {
    if (!_courses.containsKey(course.getName()))
      _courses.put(course.getName(), course);
  }
  public void addStudent(Student student) {
    if (!_students.containsKey(student.getId()))
      _students.put(student.getId(),student);
  }
  public void addAdministrative(Administrative administrative) {
    if (!_administratives.containsKey(administrative.getId()))
      _administratives.put(administrative.getId(), administrative);
  }
  public void addTeacher(Teacher teacher) {
    if (!_teachers.containsKey(teacher.getId()))
      _teachers.put(teacher.getId(), teacher);
  }
  

  public LinkedList<Course> getCourses() {
    LinkedList<Course> list = new LinkedList<Course>();
    list.addAll(_courses.values());
    return list;
  }
  public LinkedList<Student> getStudents() {
    LinkedList<Student> list = new LinkedList<Student>();
    list.addAll(_students.values());
    return list;
  }
  public LinkedList<Administrative> getAdministratives() {
    LinkedList<Administrative> list = new LinkedList<Administrative>();
    list.addAll(_administratives.values());
    return list;
  }
  public LinkedList<Teacher> getTeachers() {
    LinkedList<Teacher> list = new LinkedList<Teacher>();
    list.addAll(_teachers.values());
    return list;
  }
  public Course getCourse(String name) {
    return _courses.get(name);
  }
  public Student getStudent(int id) {
    return _students.get(id);
  }
  public Administrative getAdministrative(int id) {
    return _administratives.get(id);
  }
  public Teacher getTeacher(int id) {
    return _teachers.get(id);
  }
  public Person getPerson(int id) {
  	for (Administrative administrative : getAdministratives())
      if (administrative.getId() == id)
      	return administrative;

    for (Teacher teacher : getTeachers())
      if (teacher.getId() == id)
        return teacher;

    for (Student student : getStudents())
      if (student.getId() == id)
        return student;

    return null;
  }
  public Discipline getDiscipline(String disciplineName) {
    Discipline discipline = null;
    for (Course course : getCourses()) {
      if (course.hasDiscipline(disciplineName)) {
        discipline = course.getDiscipline(disciplineName);
      }
    }
    return discipline;
  }
  public Course getIdCourse(int id) {
    for (Course course : getCourses())
      for (Discipline discipline : course.getDisciplines())
        if (discipline.hasStudent(id))
          return course;
    return null;
  }

  public void removeCourse(String name){
    if (_courses.containsKey(name))
      _courses.remove(name);
  }
  public void removeStudent(int id) {
    if (_students.containsKey(id))
      _students.remove(id);
  }
  public void removeAdministrative(int id) {
    if (_administratives.containsKey(id))
      _administratives.remove(id);
  }
  public void removeTeacher(int id) {
    if (_teachers.containsKey(id))
      _teachers.remove(id);
  }

  public boolean hasCourse(String courseName) {
  	return _courses.containsKey(courseName);
  }
  public boolean hasAdministrative(int id) {
  	return _administratives.containsKey(id);	
  }	
  public boolean hasTeacher(int id) {
  	return _teachers.containsKey(id);
  }
  public boolean hasStudent(int id) {
  	return _students.containsKey(id);
  }
  public boolean hasRepresentative(int id) {
    LinkedList<Course> courses = getCourses();
    for (Course course : courses)
      if (course.hasRepresentative(id))
        return true;
    return false;
  }
  public boolean hasDiscipline(String disciplineName) {
  	LinkedList<Course> courses = getCourses();
  	for(Course course : courses) 
  		if(course.hasDiscipline(disciplineName))
  			return true;
  	return false;
  }

  private String disciplineToString(Course course, Discipline discipline) {
  	return "* " + course.getName() + " - " + discipline.getName() + "\n";
  }
  private String disciplinesToString(int id) {
    TreeMap<String, String> disciplines = new TreeMap<>(Collator.getInstance(Locale.getDefault()));
    String displayText = new String();

    for(Course course : getCourses()) {
    	disciplines.put(course.getName(), course.disciplinesToString(id));
    }

    for(String s : disciplines.values()) {
    	displayText += s;
    }

    return displayText;
  }
  public String administrativeToString(int id) {
  	return getAdministrative(id).toString() + "\n" + disciplinesToString(id);
  }
  public String teacherToString(int id) {
    return getTeacher(id).toString() + "\n" + disciplinesToString(id);
  }
  public String studentToString(int id) {
    return getStudent(id).toString() + "\n" + disciplinesToString(id);
  }
  public String representativeToString(int id) {
    return getStudent(id).toString().replace("ALUNO", "DELEGADO") + "\n" + disciplinesToString(id);
  }
  public String personToString(int id) {
	String displayText = new String();

    if (hasAdministrative(id))
      displayText += administrativeToString(id);

    else if (hasTeacher(id))
      displayText += teacherToString(id);

    else if (hasRepresentative(id))
        displayText += representativeToString(id);
    
    else if (hasStudent(id))
      displayText += studentToString(id);

    return displayText;
  }

  public String displayNotifications(int id) {
    return getPerson(id).displayNotifications();
  }

  /**
   * @param phoneNumber new phone number
   * @param id          login id
   * @return            person discription
   */
  public String changePhoneNumber(String phoneNumber, int id) {
    getPerson(id).setPhoneNumber(phoneNumber);
    return personToString(id);
  }


  /**
   * @param name        person name
   * @return            person discription
   */
  public String searchPerson(String name) {
  	LinkedList<Person> persons = new LinkedList<Person>();
    String displayText = new String();

  	for (Administrative administrative : getAdministratives())
      if (administrative.getName().contains(name))
        persons.add(administrative);

    for (Teacher teacher : getTeachers())
      if (teacher.getName().contains(name))
        persons.add(teacher);

    for (Student student : getStudents())
      if (student.getName().contains(name))
        persons.add(student);
  	
    Collections.sort(persons, Person.NAME_COMPARATOR);

  	for(Person person : persons) {
        displayText += personToString(person.getId());
  	}
    return displayText;
  }

  /**
   * @return            persons discriptions
   */
   public String showAllPersons() {
  	TreeMap<Integer, String> persons = new TreeMap<Integer, String>();
    String displayText = new String();
  		
  	for(Administrative administrative : getAdministratives())
  		persons.put(administrative.getId(), administrativeToString(administrative.getId()));

  	for(Teacher teacher : getTeachers())
  		persons.put(teacher.getId(), teacherToString(teacher.getId()));

  	for(Student student : getStudents()) {
      if (hasRepresentative(student.getId()))
        persons.put(student.getId(), representativeToString(student.getId()));
      else
        persons.put(student.getId(), studentToString(student.getId()));
  	}

  	for(String s : persons.values())
  		displayText += s;

    return displayText;
  }

  /**
   * @param disciplineName
   * @param id             login id
   * @return               students description
   */
   public String showDisciplineStudents(String disciplineName, int id)
   throws UnknownDisciplineException, NotValidDisciplinePersonException {
    TreeMap<Integer, String> students = new TreeMap<Integer, String>();
    String displayText = new String();

    String courseName = getPersonalCourseName(id, disciplineName);
    if(!hasDiscipline(disciplineName))
        throw new UnknownDisciplineException();
    if (!hasCourse(courseName))
      throw new NotValidDisciplinePersonException();
    Course course = getCourse(courseName);
    for(Student student : course.getStudents()) {
        if(course.hasDisciplineMember(student.getId(), disciplineName)) {
            students.put(student.getId(), personToString(student.getId()));
        }
    }
    for (String str : students.values())
        displayText += str;

    return displayText;
  }

  /*-------------Course-------------------*/
  public String getPersonalCourseName(int id, String disciplineName) {
    String courseName = new String();
    for (Course course : getCourses())
      if (course.hasDisciplineMember(id, disciplineName)) {
        courseName = course.getName();
        break;
      }
    return courseName;
  }

  public void createCourse(String courseName)
  throws DupCourseException {
    if (hasCourse(courseName))
      throw new DupCourseException();
    Course newCourse = new Course(courseName);
    addCourse(newCourse);
  }

  public void destroyCourse(String courseName)
  throws UnknownCourseException {
    if (!hasCourse(courseName))
      throw new UnknownCourseException();
    removeCourse(courseName);
  }

  /*-----------Discipline-----------------*/
  public void createDiscipline(String disciplineName, String courseName)
  throws DupDisciplineException, UnknownCourseException {
    if (!hasCourse(courseName))
      throw new UnknownCourseException();
    Course course = getCourse(courseName);
    try { course.createDiscipline(disciplineName); }
    catch (DupDisciplineException dde) { throw dde; }
  }

  public void destroyDiscipline(String disciplineName, String courseName)
  throws UnknownDisciplineException, UnknownCourseException {
    if (!hasCourse(courseName))
      throw new UnknownCourseException();
    Course course = getCourse(courseName);
    try { course.destroyDiscipline(disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
  }

  /*------------Project-------------------*/

  /**
   * @param  id                 login id
   * @param  projectName
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws DupProjectException
   * @throws NotValidDisciplinePersonException
   */
  public void createProject(int id, String projectName, String disciplineName)
  throws UnknownDisciplineException, DupProjectException, 
  NotValidDisciplinePersonException {
    String courseName = getPersonalCourseName(id, disciplineName);
    if(!hasDiscipline(disciplineName))
    	throw new UnknownDisciplineException();
    if (!hasCourse(courseName))
      throw new NotValidDisciplinePersonException();
    Course course = getCourse(courseName);
    try { course.createProject(projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (DupProjectException dpe) { throw dpe; }
  }

  /**
   * @param  id                 login id
   * @param  projectName
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws UnknownProjectException
   * @throws NotValidDisciplinePersonException
   */
  public void closeProject(int id, String projectName, String disciplineName)
  throws UnknownDisciplineException,
  UnknownProjectException, NotValidDisciplinePersonException {
    String courseName = getPersonalCourseName(id, disciplineName);
    if(!hasDiscipline(disciplineName))
    	throw new UnknownDisciplineException();
    if (!hasCourse(courseName))
      throw new NotValidDisciplinePersonException();
    Course course = getCourse(courseName);
    try { course.closeProject(projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
  }

  /**
   * @param  id                 login id
   * @param  submission
   * @param  projectName
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws UnknownProjectException
   * @throws UnknownInscriptionException
   * @throws NotValidDisciplinePersonException
   */
  public void submitProject(int id, String submission, String projectName, String disciplineName)
  throws UnknownDisciplineException,
  UnknownProjectException, UnknownInscriptionException, 
  NotValidDisciplinePersonException {
    String courseName = getPersonalCourseName(id, disciplineName);
    if(!hasDiscipline(disciplineName))
    	throw new UnknownDisciplineException();
    if (!hasCourse(courseName))
      throw new NotValidDisciplinePersonException();
    Course course = getCourse(courseName);
    try { course.submitProject(id, submission, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownInscriptionException uie) { throw uie; }
  }

  /**
   * @param  id                 login id
   * @param  projectName
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws UnknownProjectException
   * @throws NotValidDisciplinePersonException
   */
  public String showProjectSubmissions(int id, String projectName, String disciplineName)
  throws UnknownDisciplineException,
  UnknownProjectException, NotValidDisciplinePersonException {
    String courseName = getPersonalCourseName(id, disciplineName);
    if(!hasDiscipline(disciplineName))
    	throw new UnknownDisciplineException();
    if (!hasCourse(courseName))
      throw new NotValidDisciplinePersonException();
    Course course = getCourse(courseName);
    try { return course.showProjectSubmissions(projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
  }

  /*-------------Survey-------------------*/
  /**
   * @param  id                 login id
   * @param  projectName
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws UnknownProjectException
   * @throws DupSurveyException
   */
  public void createSurvey(int id, String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  DupSurveyException {
    Course course = getIdCourse(id);
    try { course.createSurvey(projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (DupSurveyException dse) { throw dse; }
  }

  /**
   * @param  id                 login id
   * @param  projectName
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws UnknownProjectException
   * @throws UnknownSurveyException
   * @throws OpenSurveyException
   */
  public void openSurvey(int id, String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, OpenSurveyException {
    Course course = getIdCourse(id);
    try { course.openSurvey(projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (OpenSurveyException ose) { throw ose; }
  }

  /**
   * @param  id                 login id
   * @param  projectName
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws UnknownProjectException
   * @throws UnknownSurveyException
   * @throws NotEmptySurveyException
   * @throws FinishSurveyException
   */
  public void cancelSurvey(int id, String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, NotEmptySurveyException,
  FinishSurveyException {
    Course course = getIdCourse(id);
    try { course.cancelSurvey(projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (NotEmptySurveyException nese) { throw nese; }
    catch (FinishSurveyException fse) { throw fse; }
  }

  /**
   * @param  id                 login id
   * @param  projectName
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws UnknownProjectException
   * @throws UnknownSurveyException
   * @throws CloseSurveyException
   */
  public void closeSurvey(int id, String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, CloseSurveyException {
    Course course = getIdCourse(id);
    try { course.closeSurvey(projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (CloseSurveyException cse) { throw cse; }
  }

  /**
   * @param  id                 login id
   * @param  projectName
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws UnknownProjectException
   * @throws UnknownSurveyException
   * @throws FinishSurveyException
   */
  public void finishSurvey(int id, String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, FinishSurveyException {
    Course course = getIdCourse(id);
    try { course.finishSurvey(projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSurveyException use) { throw use; }
    catch (FinishSurveyException fse) { throw fse; }
  }

  /**
   * @param  id                 login id
   * @param  hoursSpent
   * @param  comment
   * @param  projectName
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws UnknownProjectException
   * @throws UnknownSurveyException
   * @throws AnswerSurveyException
   * @throws UnknownSubmissionException
   * @throws NotValidDisciplinePersonException
   */
  public void answerSurvey(int id, int hoursSpent, String comment, String projectName, String disciplineName)
  throws UnknownDisciplineException, UnknownProjectException,
  UnknownSurveyException, AnswerSurveyException , UnknownSubmissionException, 
  NotValidDisciplinePersonException {
    String courseName = getPersonalCourseName(id, disciplineName);
    if(!hasDiscipline(disciplineName))
    	throw new UnknownDisciplineException();
    if (!hasCourse(courseName))
      throw new NotValidDisciplinePersonException();
    Course course = getCourse(courseName);
    try { course.answerSurvey(id, hoursSpent, comment, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSubmissionException use) { throw use; }
    catch (UnknownSurveyException use) { throw use; }
    catch (AnswerSurveyException ase) { throw ase; }
  }

  /**
   * @param  id                 login id
   * @param  projectName
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws UnknownProjectException
   * @throws UnknownSurveyException
   * @throws UnknownSubmissionException
   * @throws NotValidDisciplinePersonException
   */
  public String showSurveyResults(int id, String projectName, String disciplineName)
  throws UnknownDisciplineException, NotValidDisciplinePersonException,
  UnknownProjectException, UnknownSurveyException, UnknownSubmissionException {
  	String courseName = getPersonalCourseName(id, disciplineName);
    if(!hasDiscipline(disciplineName))
    	throw new UnknownDisciplineException();
    if (!hasCourse(courseName))
      throw new NotValidDisciplinePersonException();
    Course course = getCourse(courseName);
    try { return course.showSurveyResults(id, projectName, disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
    catch (UnknownProjectException upe) { throw upe; }
    catch (UnknownSubmissionException use) { throw use; }
    catch (UnknownSurveyException use) { throw use; }
  }

  /**
   * @param  id                 login id
   * @param  disciplineName
   * @throws UnknownDisciplineException
   * @throws NotValidDisciplinePersonException
   */
  public String showDisciplineSurveys(int id, String disciplineName)
  throws UnknownDisciplineException, NotValidDisciplinePersonException {
    Course course = getIdCourse(id);
    try { return course.showDisciplineSurveys(disciplineName); }
    catch (UnknownDisciplineException ude) { throw ude; }
  }
  
}
