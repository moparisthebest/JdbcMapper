package com.moparisthebest.jdbc.dto;

import java.util.Date;

/**
 * Created by mopar on 6/10/14.
 */
public class FieldPerson implements Person {

	protected long personNo;
	protected Date birthDate;
	protected String firstName;
	protected String lastName;

	public FieldPerson(){
	}

	public FieldPerson(long personNo, Date birthDate, String firstName, String lastName) {
		this.personNo = personNo;
		this.birthDate = birthDate;
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public FieldPerson(Person person) {
		this.personNo = person.getPersonNo();
		this.birthDate = person.getBirthDate();
		this.lastName = person.getLastName();
		this.firstName = person.getFirstName();
	}

	public long getPersonNo() {
		return personNo;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public boolean equals(final Object o) {
		return PersonEqualsHashCode.equals(this, o);
	}

	@Override
	public int hashCode() {
		return PersonEqualsHashCode.hashCode(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"{" +
				"personNo=" + personNo +
				", birthDate=" + birthDate +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				'}';
	}

	public FieldPerson cleanThyself() {
		this.firstName += " " + this.lastName;
		this.lastName = null;
		return this;
	}
}
