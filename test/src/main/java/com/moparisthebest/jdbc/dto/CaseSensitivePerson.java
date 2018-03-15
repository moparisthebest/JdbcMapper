package com.moparisthebest.jdbc.dto;

public class CaseSensitivePerson {
	private String mPersonFirstName;

	public String getmPersonFirstName() {
		return mPersonFirstName;
	}

	public void setmPersonFirstName(final String mPersonFirstName) {
		this.mPersonFirstName = mPersonFirstName;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final CaseSensitivePerson that = (CaseSensitivePerson) o;

		return mPersonFirstName != null ? mPersonFirstName.equals(that.mPersonFirstName) : that.mPersonFirstName == null;
	}

	@Override
	public int hashCode() {
		return mPersonFirstName != null ? mPersonFirstName.hashCode() : 0;
	}
}
