package com.moparisthebest.jdbc.dto;

import java.util.Date;

/**
 * Created by mopar on 6/10/14.
 */
public class SetBoss extends SetPerson implements Boss {
	protected String department;
	protected String first_name;

	protected SetBoss() {
		super();
	}

	public SetBoss(long personNo, Date birthDate, String firstName, String lastName, String department, String first_name) {
		super(personNo, birthDate, firstName, lastName);
		this.department = department;
		this.first_name = first_name;
	}

	public SetBoss(Boss boss) {
		super(boss);
		this.department = boss.getDepartment();
		this.first_name = boss.getFirst_name();
	}

	public String getDepartment() {
		return department;
	}

	public String getFirst_name() {
		return first_name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SetBoss)) return false;
		if (!super.equals(o)) return false;

		SetBoss boss = (SetBoss) o;

		if (department != null ? !department.equals(boss.department) : boss.department != null) return false;
		if (first_name != null ? !first_name.equals(boss.first_name) : boss.first_name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (department != null ? department.hashCode() : 0);
		result = 31 * result + (first_name != null ? first_name.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"{" +
				"department='" + department + '\'' +
				", first_name='" + first_name + '\'' +
				"} " + super.toString();
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
}
