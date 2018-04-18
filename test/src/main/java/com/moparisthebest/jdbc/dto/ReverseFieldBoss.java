package com.moparisthebest.jdbc.dto;

import java.util.Date;

/**
 * Created by mopar on 6/10/14.
 */
public class ReverseFieldBoss extends ReverseFieldPerson implements Boss {
	protected String department;
	protected String firstName;

	public ReverseFieldBoss() {
		super();
	}

	public ReverseFieldBoss(long personNo, Date birthDate, String firstName, String lastName, String department, String first_name) {
		super(personNo, birthDate, firstName, lastName);
		this.department = department;
		this.firstName = first_name;
	}

	public ReverseFieldBoss(Boss boss) {
		super(boss);
		this.department = boss.getDepartment();
		this.firstName = boss.getFirst_name();
	}

	public String getDepartment() {
		return department;
	}

	public String getFirst_name() {
		return firstName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ReverseFieldBoss)) return false;
		if (!super.equals(o)) return false;

		ReverseFieldBoss boss = (ReverseFieldBoss) o;

		if (department != null ? !department.equals(boss.department) : boss.department != null) return false;
		if (firstName != null ? !firstName.equals(boss.firstName) : boss.firstName != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (department != null ? department.hashCode() : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"{" +
				"department='" + department + '\'' +
				", firstName='" + firstName + '\'' +
				"} " + super.toString();
	}
}
