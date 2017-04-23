package com.xi_zz.randomnamepicker.model;

import java.io.Serializable;

public class Person implements Serializable {
	public String id;
	public String name;
	public String photo;

	public Person() {}

	public Person(final String id, final String name, final String photo) {
		this.id = id;
		this.name = name;
		this.photo = photo;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Person))
			return false;
		Person p = (Person) obj;

		return id.equals(p.id);
	}
}
