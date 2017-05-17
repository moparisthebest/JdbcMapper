package com.moparisthebest.jdbc.dto;

import java.util.Date;

/**
 * Created by mopar on 6/10/14.
 */
public class ReverseSetPerson extends ReverseFieldPerson {
	public ReverseSetPerson() {
	}

	public ReverseSetPerson(long personNo, Date birthDate, String firstName, String lastName) {
		super(personNo, birthDate, firstName, lastName);
	}

	public ReverseSetPerson(Person person) {
		super(person);
	}

	public void setPersonNo(long personNo) {
		this.personNo = personNo;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
