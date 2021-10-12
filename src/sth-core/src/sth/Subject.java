package sth;

import sth.person.Person;

import java.util.TreeMap;
import java.io.Serializable;

public abstract class Subject implements Serializable{
	private TreeMap<Integer, Observer> _observers;

	public Subject(TreeMap<Integer, Observer> observers) {
		_observers = observers;
	}

	public void registerObserver(Observer o) {
		Person p = (Person) o;
		int id = p.getId();
		_observers.put(id, o);
	}

	public void removeObserver(Observer o) {
		Person p = (Person) o;
		int id = p.getId();
		_observers.remove(id);
	}

	public void notifyObservers(String msg) {
		for(Observer o : _observers.values()) {
			o.update(msg);
		}
	}
}