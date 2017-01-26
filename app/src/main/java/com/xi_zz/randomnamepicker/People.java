package com.xi_zz.randomnamepicker;

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

	public Person get(final Person person) {
		return peopleList.get(peopleList.indexOf(person));
	}
}
