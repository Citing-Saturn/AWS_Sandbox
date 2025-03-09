package com.task05;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@LambdaHandler(
		lambdaName = "api_handler",
		roleName = "api_handler-role",
		isPublishVersion = true,
		aliasName = "${lambdas_alias_name}",
		logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class ApiHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

	private final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
	private static final String TABLE_NAME = "Events";
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Map<String, Object> handleRequest(Map<String, Object> request, Context context) {
		context.getLogger().log("Received request: " + request);

		try {
			// Extract request parameters
			int principalId = (int) request.get("principalId");
			Map<String, Object> content = (Map<String, Object>) request.get("content");

			// Generate event data
			String eventId = UUID.randomUUID().toString();
			String createdAt = Instant.now().toString();

			// Construct item to store in DynamoDB
			Map<String, AttributeValue> item = new HashMap<>();
			item.put("id", new AttributeValue(eventId));
			item.put("principalId", new AttributeValue().withN(String.valueOf(principalId)));
			item.put("createdAt", new AttributeValue(createdAt));
			item.put("body", new AttributeValue(objectMapper.writeValueAsString(content))); // Store as JSON string

			// Save event to DynamoDB
			dynamoDB.putItem(new PutItemRequest(TABLE_NAME, item));

			// Create response
			Map<String, Object> eventResponse = new HashMap<>();
			eventResponse.put("id", eventId);
			eventResponse.put("principalId", principalId);
			eventResponse.put("createdAt", createdAt);
			eventResponse.put("body", content);

			Map<String, Object> response = new HashMap<>();
			response.put("statusCode", 201);
			response.put("event", eventResponse);

			return response;

		} catch (Exception e) {
			context.getLogger().log("Error processing request: " + e.getMessage());

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("statusCode", 500);
			errorResponse.put("error", "Internal Server Error: " + e.getMessage());
			return errorResponse;
		}
	}
}
