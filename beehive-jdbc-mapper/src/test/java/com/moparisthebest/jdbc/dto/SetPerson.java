package com.moparisthebest.jdbc.dto;

import java.util.Date;

/**
 * Created by mopar on 6/10/14.
 */
public class SetPerson extends FieldPerson {
	protected SetPerson() {
	}

	public SetPerson(long personNo, Date birthDate, String firstName, String lastName) {
		super(personNo, birthDate, firstName, lastName);
	}

	public SetPerson(Person person) {
		super(person);
	}

	public void setPersonNo(long personNo) {
		this.personNo = personNo;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
