package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.dto.Person;

import java.util.Date;

public class SameClassPathEnumPerson implements Person {

	public enum FirstName {
		First,
		Second,
		Third,
	}

	public FirstName firstName;
	public String lastName;

	public SameClassPathEnumPerson() {
	}

	public SameClassPathEnumPerson(final FirstName firstName) {
		this.firstName = firstName;
	}

	@Override
	public long getPersonNo() {
		return 0;
	}

	@Override
	public Date getBirthDate() {
		return null;
	}

	@Override
	public String getFirstName() {
		return firstName.name();
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	public void setFirstName(final FirstName firstName) {
		this.firstName = firstName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof SameClassPathEnumPerson)) return false;

		final SameClassPathEnumPerson that = (SameClassPathEnumPerson) o;

		return firstName == that.firstName;
	}

	@Override
	public int hashCode() {
		return firstName != null ? firstName.hashCode() : 0;
	}
}
