package fr.simplex_software.workshop.jakarta.orm.tests;

import fr.simplex_software.workshop.jakarta.orm.data.*;
import fr.simplex_software.workshop.jakarta.orm.data.Order;
import io.restassured.http.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;
import org.slf4j.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;
import org.testcontainers.junit.jupiter.*;
import org.testcontainers.junit.jupiter.Container;

import java.io.*;
import java.util.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

@Testcontainers
public class TestCustomer
{
  private static Logger LOG = LoggerFactory.getLogger("orders");

  @Container
  private static final GenericContainer<?> environment =
    new GenericContainer<>("wildfly-bootable/jakarta-orm:local")
      .withExposedPorts(8080)
      .withNetwork(Network.newNetwork())
      .withNetworkAliases("wildfly-network-alias")
      .withLogConsumer(new Slf4jLogConsumer(LOG))
      .waitingFor(Wait.forLogMessage(".*WFLYSRV0025.*", 1));

  @BeforeAll
  public static void beforeAll()
  {
    baseURI = "http://" + environment.getHost() + ":"
      + String.valueOf(environment.getMappedPort(8080));
  }

  @Test
  public void testCustomerService()
  {
    //
    // Test get customers
    //
    Customer[] customers = given().when().get("/customers").then()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(Customer[].class);
    assertThat(customers).isNotEmpty();
    assertThat(customers).hasSize(2);
    Customer customer = customers[0];
    assertThat(customer.getId()).isNotNull();
    assertThat(customer.getFirstName()).isEqualTo("John");
    //
    // Test get customer
    //
    customer = given().contentType(ContentType.XML).accept(ContentType.XML)
      .when().pathParam("id", customer.getId())
      .get("/customers/{id}").then()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(Customer.class);
    assertThat(customer).isNotNull();
    assertThat(customer.getFirstName()).isEqualTo("John");
    //
    // Test create customer
    //
    customer = new Customer("Mike", "Doe", "mike.doe@email.com", "096-23419",
      List.of(new Order("myItem01", 150L, customer), new Order("myItem02", 35L, customer)));
    customer = given()
      .log().all()
      .body(customer)
      .contentType(ContentType.XML)
      .accept(ContentType.XML)
      .when()
      .post("/customers")
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .contentType(ContentType.XML)
      .extract().body().as(Customer.class);
    assertThat(customer).isNotNull();
    assertThat(customer.getFirstName()).isEqualTo("Mike");
    //
    // Test update customer
    //
    customer.setFirstName("Jack");
    customer.setEmail("jack.doe@email.com");
    customer.setPhone("096-23420");
    customer.addOrder(new Order("myItem03", 85L, customer));
    customer = given()
      .body(customer)
      .contentType(ContentType.XML)
      .when()
      .put("/customers")
      .then()
      .statusCode(HttpStatus.SC_ACCEPTED)
      .extract().body().as(Customer.class);
    assertThat(customer).isNotNull();
    assertThat(customer.getFirstName()).isEqualTo("Jack");
    //
    // Test create order
    //
    Order order = new Order("myItem04", 85L, customer);
    customer.addOrder(order);
    order = given()
      .body(order)
      .when()
      .put("/orders")
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(Order.class);
    assertThat(order).isNotNull();
    assertThat(order.getItem()).isEqualTo("myItem04");
    //
    // Test get orders
    //
    Order[] orders = given().when().get("/orders").then()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(Order[].class);
    assertThat(orders).isNotEmpty();
    assertThat(orders).hasSize(4);
    //
    // Test get order
    //
    order = given().when().pathParam("id", order.getId())
      .get("/orders/{id}").then()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(Order.class);
    assertThat(order).isNotNull();
    assertThat(order.getItem()).isEqualTo("myItem04");
    //
    // Test update order
    //
    order.setItem("myItem05");
    order = given()
      .when().body(order).put("/orders")
      .then()
      .statusCode(HttpStatus.SC_ACCEPTED)
      .extract().body().as(Order.class);
    assertThat(order).isNotNull();
    assertThat(order.getItem()).isEqualTo("myItem05");
    //
    // Test DELETE Order
    //
    given()
      .when().pathParam("id", order.getId()).delete("/orders/{id}")
      .then()
      .statusCode(HttpStatus.SC_NO_CONTENT);
    //
    // Test DELETE Customer
    //
    given()
      .when().pathParam("id", customer.getId()).delete("/customers/{id}")
      .then()
      .statusCode((HttpStatus.SC_NO_CONTENT));
  }
}
