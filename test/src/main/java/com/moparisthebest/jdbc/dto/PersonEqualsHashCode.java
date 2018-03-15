package com.moparisthebest.jdbc.dto;

public class PersonEqualsHashCode {
	public static boolean equals(final Person thiss, final Object o) {
		if (thiss == o) return true;
		if (!(o instanceof Person)) return false;

		final Person that = (Person) o;

		if (thiss.getPersonNo() != that.getPersonNo()) return false;
		if (thiss.getBirthDate() != null ? !thiss.getBirthDate().equals(that.getBirthDate()) : that.getBirthDate() != null)
			return false;
		if (thiss.getFirstName() != null ? !thiss.getFirstName().equals(that.getFirstName()) : that.getFirstName() != null)
			return false;
		return thiss.getLastName() != null ? thiss.getLastName().equals(that.getLastName()) : that.getLastName() == null;
	}

	public static int hashCode(final Person thiss) {
		int result = (int) (thiss.getPersonNo() ^ (thiss.getPersonNo() >>> 32));
		result = 31 * result + (thiss.getBirthDate() != null ? thiss.getBirthDate().hashCode() : 0);
		result = 31 * result + (thiss.getFirstName() != null ? thiss.getFirstName().hashCode() : 0);
		result = 31 * result + (thiss.getLastName() != null ? thiss.getLastName().hashCode() : 0);
		return result;
	}
}
