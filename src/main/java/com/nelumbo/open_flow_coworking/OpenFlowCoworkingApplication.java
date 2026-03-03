package com.nelumbo.open_flow_coworking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class OpenFlowCoworkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenFlowCoworkingApplication.class, args);
		System.out.println("Api corriendo...");
	}

}
