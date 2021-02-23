package com.saket.aws.lambdamvn.config;

import com.amazon.dax.client.dynamodbv2.AmazonDaxClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class DynamoDBConfig {

	
	    private Regions REGION = Regions.US_EAST_1;
	    
	    public DynamoDB initDynamoDbOrDaxClient() {
	    	if(System.getenv("DaxEndpoint")!= null && System.getenv("DaxEndpoint").toString().length()>0) {
	    		
	    		return getDaxClient(System.getenv("DaxEndpoint").toString());
	    	}
	    	else {
	    		return getDynamoDBClient();
	    	}
	    }
	    
	   private DynamoDB getDynamoDBClient() {
	        System.out.println("Creating DynamoDB client ");
	        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
	                .withRegion(REGION)
	                .build();
	        return new DynamoDB(client);
	     }

		
	   private DynamoDB getDaxClient(String daxEndpoint) {
	        System.out.println("Creating a DAX client with cluster endpoint " + daxEndpoint);
	        AmazonDaxClientBuilder daxClientBuilder = AmazonDaxClientBuilder.standard();
	        daxClientBuilder.withRegion(REGION).withEndpointConfiguration(daxEndpoint);
	        AmazonDynamoDB client = daxClientBuilder.build();
	        return new DynamoDB(client);
	     }
	    
	    
}
