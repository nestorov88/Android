package com.melon.utils;

import com.melon.rest.RestClient;

public class Manager {

	private static RestClient serviceClient;
	
	
	public static RestClient getServiceClient() {
		if(serviceClient == null) {
			serviceClient = new RestClient();
		}
		
		return serviceClient;
	}
}
