package sth.person;

//import exceptions

import sth.Notifications;
import sth.Observer;

import java.io.Serializable;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;
import java.text.Collator;


public abstract class Person implements Serializable, Observer {

  public final static Comparator<Person> ID_COMPARATOR = new IDComparator();

  public final static Comparator<Person> NAME_COMPARATOR = new NameComparator();

  private static class IDComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
      return p1.getId() - p2.getId();
    }
  }

  private static class NameComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
      Collator collator = Collator.getInstance(Locale.getDefault());
      return collator.compare(p1.getName(), p2.getName());
    }
  }

  private String _name;
  private String _phoneNumber;
  private int _id;
  private Notifications _notifications;

  public Person(String name, String phoneNumber, int id) {
    _name = name;
    _phoneNumber = phoneNumber;
    _id = id;
    _notifications = new Notifications();
  }

  public void setName(String name) { _name = name; }
  public void setPhoneNumber(String phoneNumber) { _phoneNumber = phoneNumber; }
  public void setId(int id) { _id = id; }

  public String getName() { return _name; }
  public String getPhoneNumber() { return _phoneNumber; }
  public int getId() { return _id; }

  public void update(String msg) {
    _notifications.addNotification(msg);
  }

  public String displayNotifications() {
    return _notifications.displayNotifications();
  }

  @Override
  public String toString() {
    return "|" + _id + "|" + _phoneNumber + "|" + _name;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Person) {
      Person person = (Person) o;
      return _name.equals(person.getName()) && _phoneNumber == person.getPhoneNumber() && _id == person.getId();
    }
    return false;
  }
}
