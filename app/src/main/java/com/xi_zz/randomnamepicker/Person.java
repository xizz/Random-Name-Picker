package com.xi_zz.randomnamepicker;

import java.io.Serializable;

public class Person implements Serializable {
	public String name;
	public int image;
	public String photo;

	public Person(final String name, final int image) {
		this.name = name;
		this.image = image;
	}

	public Person(final String name, final String photo) {
		this.name = name;
		this.photo = photo;
	}
}
