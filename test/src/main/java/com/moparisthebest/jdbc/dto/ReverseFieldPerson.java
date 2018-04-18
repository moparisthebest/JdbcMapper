package com.moparisthebest.jdbc.dto;

import java.util.Date;

/**
 * Created by mopar on 6/10/14.
 */
public class ReverseFieldPerson implements Person {

	protected long personNo;
	protected Date birthDate;
	protected String first_name;
	protected String lastName;

	public ReverseFieldPerson(){
	}

	public ReverseFieldPerson(long personNo, Date birthDate, String firstName, String lastName) {
		this.personNo = personNo;
		this.birthDate = birthDate;
		this.lastName = lastName;
		this.first_name = firstName;
	}

	public ReverseFieldPerson(Person person) {
		this.personNo = person.getPersonNo();
		this.birthDate = person.getBirthDate();
		this.lastName = person.getLastName();
		this.first_name = person.getFirstName();
	}

	public long getPersonNo() {
		return personNo;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public String getFirstName() {
		return first_name;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ReverseFieldPerson)) return false;

		ReverseFieldPerson person = (ReverseFieldPerson) o;

		if (personNo != person.personNo) return false;
		if (birthDate != null ? !birthDate.equals(person.birthDate) : person.birthDate != null) return false;
		if (first_name != null ? !first_name.equals(person.first_name) : person.first_name != null) return false;
		if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (personNo ^ (personNo >>> 32));
		result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
		result = 31 * result + (first_name != null ? first_name.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"{" +
				"personNo=" + personNo +
				", birthDate=" + birthDate +
				", first_name='" + first_name + '\'' +
				", lastName='" + lastName + '\'' +
				'}';
	}
}
