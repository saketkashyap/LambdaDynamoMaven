package com.saket.aws.lambdamvn.model;

public class PersonResponse {
	private PersonError error;

	private Person person;

	public PersonError getError() {
		return error;
	}

	public void setError(PersonError error) {
		this.error = error;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String toString() {
		return "PersonResponse [error=" + error + ", person=" + person + "]";
	}
	
	
	
}
