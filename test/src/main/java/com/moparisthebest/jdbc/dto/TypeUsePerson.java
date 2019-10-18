package com.moparisthebest.jdbc.dto;

import com.moparisthebest.jdbc.TypeUseAnnotation;

import java.util.Date;

public class TypeUsePerson implements Person {

    public long personNo;
    
    @TypeUseAnnotation
    public Date birthDate;
    @TypeUseAnnotation
    public String firstName, lastName;

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
}
