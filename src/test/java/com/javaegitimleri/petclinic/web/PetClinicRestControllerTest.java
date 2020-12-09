package com.javaegitimleri.petclinic.web;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.javaegitimleri.petclinic.exception.OwnerNotFoundException;
import com.javaegitimleri.petclinic.model.Owner;

public class PetClinicRestControllerTest {
	
	private RestTemplate restTemplate;
	
	@Before
	public void setUp() {
		restTemplate = new RestTemplate();
	}
	
	@Test
	public void testDeleteOwner() {
		restTemplate.delete("http://localhost:8080/rest/owner/1");
		
		try {
			restTemplate.getForEntity("http://localhost:8080/rest/owner/1", Owner.class);
			Assert.fail("should have not returned owner");
		}
		catch (HttpClientErrorException ex) {
			MatcherAssert.assertThat(ex.getStatusCode().value(), Matchers.equalTo(404));
		}
	}
	
	@Test
	public void testOwnerUpdate() {
		Owner owner = restTemplate.getForObject("http://localhost:8080/rest/owner/3", Owner.class);
		
		MatcherAssert.assertThat(owner.getFirstName(),Matchers.equalTo("Kolin3"));
		
		owner.setFirstName("Kolin Kayıkcıoğlu");
		restTemplate.put("http://localhost:8080/rest/owner/3", owner);
		
		owner = restTemplate.getForObject("http://localhost:8080/rest/owner/3", Owner.class);
		
		MatcherAssert.assertThat(owner.getFirstName(), Matchers.equalTo("Kolin Kayıkcıoğlu"));

	}
	
	@Test
	public void testCreateOwner() {

		Owner owner =new Owner();
		owner.setFirstName("Araks");
		owner.setLastName("Kykc");
		
		URI location = restTemplate.postForLocation("http://localhost:8080/rest/owner", owner);
		
		Owner owner2 =restTemplate.getForObject(location,Owner.class);
		
		MatcherAssert.assertThat(owner2.getFirstName(),Matchers.equalTo(owner.getFirstName()));
		MatcherAssert.assertThat(owner2.getLastName(),Matchers.equalTo(owner.getLastName()));
		
	}

	@Test
	public void testGetOwnerById() {
		ResponseEntity<Owner> response = restTemplate.getForEntity("http://localhost:8080/rest/owner/1", Owner.class);
		
		MatcherAssert.assertThat(response.getStatusCodeValue(), Matchers.equalTo(200));
		MatcherAssert.assertThat(response.getBody().getFirstName(),Matchers.equalTo("Kolin"));
		
	}
	
	@Test
	public void testGetOwnersByLastName() {
		ResponseEntity<List> responseEntity =  restTemplate.getForEntity("http://localhost:8080/rest/owner?ln=kykc", List.class);
		
		MatcherAssert.assertThat(responseEntity.getStatusCodeValue(),Matchers.equalTo(200));
		
		List<Map<String, String>> bodyList = responseEntity.getBody();
		
		List<String> firstNames = bodyList.stream().map(e->e.get("firstName")).collect(Collectors.toList());
		
		MatcherAssert.assertThat(firstNames, Matchers.containsInAnyOrder("Kolin"));
		
	}
	
	@Test
	public void TestGetOwners(){
		
		ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:8080/rest/owners", List.class);
		

		MatcherAssert.assertThat(response.getStatusCodeValue(),Matchers.equalTo(200));
		
		List<Map<String, String>> bodyList = response.getBody();
		
		List<String> firstNames = bodyList.stream().map(e->e.get("firstName")).collect(Collectors.toList());
		
		MatcherAssert.assertThat(firstNames, Matchers.containsInAnyOrder("Kolin","Kolin2","Kolin3"));
	}
	
}
