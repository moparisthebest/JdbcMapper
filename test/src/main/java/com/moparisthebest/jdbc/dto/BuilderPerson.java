package com.moparisthebest.jdbc.dto;

import java.util.Date;

public class BuilderPerson implements Person {

	protected long personNo;
	protected Date birthDate;
	protected String firstName;
	protected String lastName;

	private boolean setterCalled = false;

	public BuilderPerson() {
	}

	public BuilderPerson(Person person) {
		this.personNo = person.getPersonNo();
		this.birthDate = person.getBirthDate();
		this.lastName = person.getLastName();
		this.firstName = person.getFirstName();
		this.setterCalled = true; // for convenience
	}

	@Override
	public long getPersonNo() {
		return personNo;
	}

	public BuilderPerson setPersonNo(final long personNo) {
		this.personNo = personNo;
		this.setterCalled = true;
		return this;
	}

	@Override
	public Date getBirthDate() {
		return birthDate;
	}

	public BuilderPerson setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
		this.setterCalled = true;
		return this;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	public BuilderPerson setFirstName(final String firstName) {
		this.firstName = firstName;
		this.setterCalled = true;
		return this;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	public BuilderPerson setLastName(final String lastName) {
		this.lastName = lastName;
		this.setterCalled = true;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if(!setterCalled || (o instanceof BuilderPerson && !((BuilderPerson)o).setterCalled))
			throw new RuntimeException("setter not called");
		return PersonEqualsHashCode.equals(this, o);
	}

	@Override
	public int hashCode() {
		if(!setterCalled)
			throw new RuntimeException("setter not called");
		return PersonEqualsHashCode.hashCode(this);
	}
}
