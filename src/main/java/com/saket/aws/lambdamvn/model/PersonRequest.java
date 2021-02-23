package com.saket.aws.lambdamvn.model;

public class PersonRequest {

	private String action;

	private Person person;
	
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "PersonRequest [action=" + action + ", person=" + person + "]";
	}
	
	
}
