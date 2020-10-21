package com.moparisthebest.jdbc.dto;

import java.util.Date;

public class PublicFieldDto {
    public String firstName;
    public SubClass1 subClass1;

    public static class SubClass1 {
        public SubClass2 subClass2;
    }

    public static class SubClass2 {
        public SubClass3 subClass3;
    }

    public static class SubClass3 {
        private long personNo;
        public Date birthDate;
        public String lastName;
    }
}
