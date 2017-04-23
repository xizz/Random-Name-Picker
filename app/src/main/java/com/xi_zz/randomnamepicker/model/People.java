package com.xi_zz.randomnamepicker.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class People {
	public ArrayList<Person> peopleList;

	public People() {
		this(new ArrayList<Person>());
	}

	public People(@NonNull final ArrayList<Person> peopleList) {
		this.peopleList = peopleList;
	}

	public Person get(int i) {
		return peopleList.get(i);
	}

	public int size() {
		return peopleList.size();
	}

	public void add(final Person person) {
		peopleList.add(person);
	}

	public void remove(final Person person) {
		peopleList.remove(person);
	}

	public void clear() {
		peopleList.clear();
	}

	public Person get(@NonNull final Person person) {
		return peopleList.get(peopleList.indexOf(person));
	}

	public void update(@NonNull final Person person) {
		Person local = get(person);
		if (local != null) {
			local.name = person.name;
			local.photo = person.photo;
		}
	}
}
