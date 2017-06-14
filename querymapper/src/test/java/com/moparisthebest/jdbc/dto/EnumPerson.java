package com.moparisthebest.jdbc.dto;

import java.util.Date;

/**
 * Created by mopar on 6/13/17.
 */
public class EnumPerson implements Person {

	public FirstName firstName;
	public String lastName;

	public EnumPerson() {
	}

	public EnumPerson(final FirstName firstName) {
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

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof EnumPerson)) return false;

		final EnumPerson that = (EnumPerson) o;

		return firstName == that.firstName;
	}

	@Override
	public int hashCode() {
		return firstName != null ? firstName.hashCode() : 0;
	}
}
