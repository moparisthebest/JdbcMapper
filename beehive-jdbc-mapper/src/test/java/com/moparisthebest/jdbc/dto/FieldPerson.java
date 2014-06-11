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

	protected FieldPerson(){
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FieldPerson)) return false;

		FieldPerson person = (FieldPerson) o;

		if (personNo != person.personNo) return false;
		if (birthDate != null ? !birthDate.equals(person.birthDate) : person.birthDate != null) return false;
		if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) return false;
		if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (personNo ^ (personNo >>> 32));
		result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		return result;
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
}
