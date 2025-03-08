//package com.task03;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.syndicate.deployment.annotations.lambda.LambdaHandler;
//import com.syndicate.deployment.model.RetentionSetting;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@LambdaHandler(
//    lambdaName = "hello_world",
//	roleName = "hello_world-role",
//	isPublishVersion = true,
//	aliasName = "${lambdas_alias_name}",
//	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
//)
//public class HelloWorld implements RequestHandler<Object, Map<String, Object>> {
//
//	public Map<String, Object> handleRequest(Object request, Context context) {
//		System.out.println("Hello from lambda");
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		resultMap.put("statusCode", 200);
//		resultMap.put("body", "Hello from Lambda");
//		return resultMap;
//	}
//}


package com.task03;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;

import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
		lambdaName = "hello_world",
		roleName = "hello_world-role",
		isPublishVersion = true,
		aliasName = "${lambdas_alias_name}",
		logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class HelloWorld implements RequestHandler<Object, Map<String, Object>> {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Map<String, Object> handleRequest(Object request, Context context) {
		System.out.println("Hello from Lambda");

		// Creating the response body
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("message", "Hello from Lambda");

		// Creating the final response map
		Map<String, Object> response = new HashMap<>();
		response.put("statusCode", 200);

		try {
			// Convert response body to JSON string
			response.put("body", objectMapper.writeValueAsString(responseBody));
		} catch (Exception e) {
			response.put("statusCode", 500);
			response.put("body", "{\"message\": \"Error processing request\"}");
			e.printStackTrace();
		}

		return response;
	}
}
