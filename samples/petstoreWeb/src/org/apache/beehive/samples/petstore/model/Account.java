/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */
package org.apache.beehive.samples.petstore.model;

/**
 *
 */
public class Account
    implements java.io.Serializable {

    private String _userId;
    private String _password;
    private String _email;
    private String _firstName;
    private String _lastName;
    private String _status;

    private String _favCategory;
    private String _langPref;
    private String _bannerData;
    private boolean _myListOpt;
    private boolean _bannerOpt;

    public Account() {
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public String getUserId() {
        return _userId;
    }

    public void setUserId(String userId) {
        _userId = userId;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    public void setFavCategory(String favCategory) {
        _favCategory = favCategory;
    }

    public String getFavCategory() {
        return _favCategory;
    }

    public void setLangPref(String langPref) {
        _langPref = langPref;
    }

    public String getLangPref() {
        return _langPref;
    }

    public String getBannerData() {
        return _bannerData;
    }

    public void setBannerData(String bannerData) {
        _bannerData = bannerData;
    }

    public boolean isMyListOpt() {
        return _myListOpt;
    }

    public void setMyListOpt(boolean myListOpt) {
		_myListOpt = myListOpt;
    }

    // Derby does not have a boolean datatype so we use the myListState to simulate one with an int
    public int getMyListState() {
        if (_myListOpt)
			return 1;
		else 
			return 0;
    }

    // Derby does not have a boolean datatype so we use the myListState to simulate one with an int
    public void setMyListState(int myListOpt) {
		if (myListOpt == 0)
			_myListOpt = false;
		else
			_myListOpt = true;
    }
	
    public boolean isBannerOpt() {
        return _bannerOpt;
    }

    public void setBannerOpt(boolean bannerOpt) {
			_bannerOpt = bannerOpt;
    }

    // Derby does not have a boolean datatype so we use the bannerOptState to simulate one with an int
	public int getBannerOptState() {
        if (_bannerOpt)
			return 1;
		else 
			return 0;
    }

    // Derby does not have a boolean datatype so we use the bannerOptState to simulate one with an int
    public void setBannerOptState(int bannerOpt) {
		if (bannerOpt == 0)
			_bannerOpt = false;
		else
			_bannerOpt = true;
    }
}
