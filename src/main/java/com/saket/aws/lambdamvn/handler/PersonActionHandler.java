package com.saket.aws.lambdamvn.handler;

import java.util.UUID;
import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.saket.aws.lambdamvn.model.PersonActionsEnum;
import com.saket.aws.lambdamvn.model.PersonError;
import com.saket.aws.lambdamvn.model.PersonRequest;
import com.saket.aws.lambdamvn.model.PersonResponse;
import com.saket.aws.lambdamvn.service.PersonService;

/**
 * 
 * @author SaketKashyap
 *
 * You can change the handler to
 * this class in case you want to 
 * use lambda without API gateway
 */
public class PersonActionHandler implements RequestHandler<PersonRequest, PersonResponse> {

	private final static Logger LOGGER = Logger.getLogger(PersonActionHandler.class.getName());
	
	PersonService service = new PersonService();
	
	public PersonResponse handleRequest(PersonRequest input, Context context) {
		LOGGER.info("received input:"+input.toString());
		PersonResponse response = null;
		
			if(PersonActionsEnum.POST.toString().equalsIgnoreCase(input.getAction())) {
				
				if(input.getPerson()!=null) {
					input.getPerson().setPersonId(UUID.randomUUID());
					response = service.save(input.getPerson());
				}
			}
			
			if(PersonActionsEnum.GET.toString().equalsIgnoreCase(input.getAction())) {
				response = handleGet(input,response);
			}
			if(response !=null) {
				LOGGER.info("logging person response::"+response.toString());
			}
		        return response;

	}
	
	public PersonResponse handleGet(PersonRequest input,PersonResponse response) {

		if(input.getPerson()!=null && input.getPerson().getPersonId()!= null) {
		
			response = service.fetch(input.getPerson().getPersonId().toString());
			 }
		else {
			response.setError(new PersonError("please set the personId in the request","NO_ID"));
		}
		return response;
	}
}
