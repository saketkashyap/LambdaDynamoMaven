package com.saket.aws.lambdamvn.handler;

import java.util.Collections;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.saket.aws.lambdamvn.model.PersonActionsEnum;
import com.saket.aws.lambdamvn.model.PersonError;
import com.saket.aws.lambdamvn.model.PersonRequest;
import com.saket.aws.lambdamvn.model.PersonResponse;
import com.saket.aws.lambdamvn.service.PersonService;

/**
 * Use this class as your lambda handler for
 * API gateway integration
 * @author SaketKashyap
 *
 */
public class PersonRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private final static Logger LOGGER = Logger.getLogger(PersonRequestHandler.class.getName());
	
	PersonService service = new PersonService();
	
	/**
	 * This is invocation over API gateway
	 *  
	 */
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
		LOGGER.info("received input:"+apiGatewayProxyRequestEvent.getBody());
		
		 APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
		 try {
			 String requestString = apiGatewayProxyRequestEvent.getBody();
		        Gson gson = new GsonBuilder().create();
		        PersonRequest input = gson.fromJson(requestString, PersonRequest.class);
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
					generateResponse(apiGatewayProxyResponseEvent, response);
		 }
		
			catch(Exception e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
		        return apiGatewayProxyResponseEvent;

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
	
	
	 private void generateResponse(APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent, PersonResponse response) {
	        apiGatewayProxyResponseEvent.setHeaders(Collections.singletonMap("timeStamp", String.valueOf(System.currentTimeMillis())));
	        apiGatewayProxyResponseEvent.setStatusCode(200);
	        apiGatewayProxyResponseEvent.setBody(response.toString());
	    }
}
