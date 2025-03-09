package com.task05;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;

@LambdaHandler(
		lambdaName = "api_handler",
		roleName = "api_handler-role",
		isPublishVersion = true,
		aliasName = "${lambdas_alias_name}",
		logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class ApiHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

	private final AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.defaultClient();
	private final DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
	private final String tableName = System.getenv("target_table");

	@Override
	public Map<String, Object> handleRequest(Map<String, Object> request, Context context) {
		context.getLogger().log("Received request: " + request.toString());

		Map<String, Object> response = new HashMap<>();
		try {
			Table table = dynamoDB.getTable(tableName);
			Item item = new Item()
					.withPrimaryKey("id", UUID.randomUUID().toString())
					.withString("createdAt", Instant.now().toString())
					.withString("eventDetails", request.toString());

			table.putItem(item);
			response.put("statusCode", 200);
			response.put("body", "Event stored successfully in DynamoDB");
		} catch (Exception e) {
			context.getLogger().log("Error: " + e.getMessage());
			response.put("statusCode", 500);
			response.put("body", "Failed to store event: " + e.getMessage());
		}
		return response;
	}
}
