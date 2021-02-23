package com.saket.aws.lambdamvn.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.saket.aws.lambdamvn.config.DynamoDBConfig;
import com.saket.aws.lambdamvn.model.Person;
import com.saket.aws.lambdamvn.model.PersonResponse;

/**
 * 
 * @author SaketKashyap
 *
 */
public class PersonService {

	private final static Logger LOGGER = Logger.getLogger(PersonService.class.getName());
	
	private String DYNAMODB_TABLE_NAME = "Person";
	
	/**
	 * 
	 * @param request
	 * @return
	 */
    public PersonResponse save(Person request) {
    	
    	 persistData(request);
    	 PersonResponse response = new PersonResponse();
    	 response.setPerson(request);
    	 return response;
      
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public PersonResponse fetch(String id) {
    	 PersonResponse response = new PersonResponse();
    	Person person = getPerson(id);
    	response.setPerson(person);
        return response;
      }
    
    /**
     * 
     * @param personRequest
     * @return
     * @throws ConditionalCheckFailedException
     */
    private PutItemOutcome persistData(Person personRequest) 
		      throws ConditionalCheckFailedException {
    	LOGGER.info("person request::"+personRequest.toString());
		        return new DynamoDBConfig().initDynamoDbOrDaxClient().getTable(DYNAMODB_TABLE_NAME)
		          .putItem(
		            new PutItemSpec().withItem(new Item()
		              .withString("firstName", personRequest.getFirstName())
		              .withString("lastName", personRequest.getLastName())
		              .withString("personId",personRequest.getPersonId().toString())
		              ));
		    }
    
    /**
     * 
     * @param id
     * @return
     */
    public Person getPerson(String id) {
    	LOGGER.info("getting person for id ::"+id);
		 long startTime = System.nanoTime();
		 double duration = 0;
		QuerySpec spec  = new QuerySpec().
				withKeyConditionExpression("personId = :person_id").
				withValueMap(new ValueMap()
						.withString(":person_id", id));
		ItemCollection<QueryOutcome> items = new DynamoDBConfig().initDynamoDbOrDaxClient().
				getTable(DYNAMODB_TABLE_NAME).query(spec);
		List<Person>personList = new ArrayList<Person>();
		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
		  Item item = iterator.next();
		  Person person = new Person();
		  person.setFirstName(String.valueOf(item.get("firstName")));
		  person.setLastName(String.valueOf(item.get("lastName")));
		  person.setPersonId(UUID.fromString(String.valueOf(item.get("personId"))));
		  personList.add(person);
		  
		}
		duration = (System.nanoTime() - startTime);
		LOGGER.info("FetchTime = " + duration  + " nano seconds");
		if(personList.size() == 1) {
			return personList.get(0);
		}
		
		
		return null;
	}
}
