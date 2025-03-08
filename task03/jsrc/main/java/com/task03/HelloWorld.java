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

	@Override
	public Map<String, Object> handleRequest(Object request, Context context) {
		System.out.println("Hello from Lambda");

		// Fix: Directly include "statusCode" and "message" in the response
		Map<String, Object> response = new HashMap<>();
		response.put("statusCode", 200);
		response.put("message", "Hello from Lambda"); // Move "message" up one level

		return response;
	}
}
