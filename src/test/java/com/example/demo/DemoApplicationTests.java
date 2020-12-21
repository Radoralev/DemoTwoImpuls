package com.example.demo;

import com.example.demo.model.Employee;
import com.example.demo.model.Position;
import com.example.demo.model.Status;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testGetEmployeeByID() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        int id = 1003;
        String url = getRootUrl() + "api/v1/employees/" + id;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    public void testGetAllEmployees() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        String url = getRootUrl() + "/api/v1/employees";
        ResponseEntity<String> response =
                restTemplate.exchange(url,
                        HttpMethod.GET, entity, String.class);

        Assert.assertNotNull(response.getBody());
        System.out.println(response.getStatusCode());
        Assert.assertTrue(response.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    public void testCreateEmployee() {
        Employee employee = new Employee();

        Position position = new Position();
        position.setDescription("This is the position description");

        Status status = new Status();
        status.setDescription("This is ivo's status description");

        employee.setName("Ivo");
        employee.setAddress("Heiglhofstrasse 66");
        employee.setBirthdate(Date.valueOf("1999-08-17"));
        employee.setCreated(new Date(System.currentTimeMillis()));
        employee.setPositionid(position);
        employee.setStautsid(status);

        ResponseEntity<Employee> postResponse =
                restTemplate.postForEntity(getRootUrl() + "/api/v1/employees", employee, Employee.class);
        System.out.println(employee.toString());
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
        System.out.println(postResponse.getStatusCode());
        Assert.assertTrue(postResponse.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    public void testUpdateEmployee() {
        Employee employee = new Employee();
        Position pos = new Position();
        Status stat = new Status();
        int id = 1;

        pos.setDescription("This is an updated position description.");
        stat.setDescription("This is an updated description");
        employee.setPositionid(pos);
        employee.setStautsid(stat);

        restTemplate.put(getRootUrl() + "/api/v1/employees/" + id, employee);

        Employee updatedEmployee = restTemplate.getForObject(getRootUrl() + "/api/v1/employees/" + id, Employee.class);
        Assert.assertNotNull(updatedEmployee);
        Assert.assertNotNull(updatedEmployee.getUpdated());
    }

}
