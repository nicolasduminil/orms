package fr.simplex_software.workshop.quarkus.orm.tests;

import fr.simplex_software.workshop.quarkus.orm.data.*;
import fr.simplex_software.workshop.quarkus.orm.data.Order;

import io.quarkus.test.junit.*;
import io.restassured.common.mapper.*;
import io.restassured.http.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;
import org.slf4j.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCustomer
{
  private static final Logger LOG = LoggerFactory.getLogger("orders");
  private static Long customerId;
  private static Long orderId;


  @Test
  @org.junit.jupiter.api.Order(10)
  public void testCreateCustomer()
  {
    Customer customer = new Customer("Mike", "Doe", "mike.doe@email.com", "096-23419");
    customer.addOrder(new Order("myItem01", 150L, customer));
    customer.addOrder(new Order("myItem02", 35L, customer));
    customer = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(customer)
      .when()
      .post("/customers")
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(Customer.class);
    assertThat(customer).isNotNull();
    assertThat(customer.getId()).isNotNull();
    customerId = customer.getId();
    assertThat(customer.getFirstName()).isEqualTo("Mike");
  }

  /*@org.junit.jupiter.api.Order(20)
  @Test
  public void testGetCustomers()
  {
    List<Customer> customers = given()
      .log().all()
      .accept(ContentType.JSON)
      .when().get("/customers")
      .then().log().all()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(new TypeRef<>()
      {
      });
    assertThat(customers).isNotEmpty();
    assertThat(customers).hasSize(1);
    Customer customer = customers.getFirst();
    assertThat(customer.getId()).isNotNull();
    assertThat(customer.getFirstName()).isEqualTo("Mike");
  }

  @org.junit.jupiter.api.Order(30)
  @Test
  public void testGetCustomer()
  {
    Customer customer = given()
      .log().all()
      .accept(ContentType.JSON)
      .when().pathParam("id", customerId)
      .get("/customers/{id}")
      .then().log().all()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(Customer.class);
    assertThat(customer).isNotNull();
    assertThat(customer.getFirstName()).isEqualTo("Mike");
  }

  @org.junit.jupiter.api.Order(40)
  @Test
  public void testUpdateCustomer()
  {
    Customer customer = given()
      .log().all()
      .accept(ContentType.JSON)
      .when().pathParam("id", customerId)
      .get("/customers/{id}")
      .then().log().all()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(Customer.class);
    customer.setFirstName("John");
    customer.setEmail("john.doe@email.com");
    customer.setPhone("096-23420");
    customer = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(customer)
      .when()
      .put("/customers")
      .then().log().all()
      .statusCode(HttpStatus.SC_ACCEPTED)
      .extract().body().as(Customer.class);
    assertThat(customer).isNotNull();
    assertThat(customer.getId()).isNotNull();
    assertThat(customer.getFirstName()).isEqualTo("John");
  }

  @org.junit.jupiter.api.Order(41)
  @Test
  public void testCreateOrder()
  {
    Customer customer = given()
      .log().all()
      .accept(ContentType.JSON)
      .when().pathParam("id", customerId)
      .get("/customers/{id}")
      .then().log().all()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(Customer.class);
    Order order = new Order("myItem04", 85L, customer);
    customer.addOrder(order);
    order = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(order)
      .when()
      .post("/orders")
      .then().log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(Order.class);
    assertThat(order).isNotNull();
    assertThat(order.getItem()).isEqualTo("myItem04");
    orderId = order.getId();
  }

  @org.junit.jupiter.api.Order(45)
  @Test
  public void testGetOrders()
  {
    List<Order> orders = given()
      .log().all()
      .accept(ContentType.JSON)
      .when().get("/orders")
      .then().log().all()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(new TypeRef<>()
      {
      });
    assertThat(orders).isNotEmpty();
    assertThat(orders).hasSize(3);
  }

  @org.junit.jupiter.api.Order(50)
  @Test
  public void testGetOrder()
  {
    assertThat(orderId).isNotNull();
    Order order = given()
      .log().all()
      .accept(ContentType.JSON)
      .when().pathParam("id", orderId)
      .get("/orders/{id}")
      .then().log().all()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(Order.class);
    assertThat(order).isNotNull();
    assertThat(order.getItem()).isEqualTo("myItem04");
  }

  @org.junit.jupiter.api.Order(60)
  @Test
  public void testUpdateOrder()
  {
    Order order = given()
      .log().all()
      .accept(ContentType.JSON)
      .when().pathParam("id", orderId)
      .get("/orders/{id}")
      .then().log().all()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(Order.class);
    order.setItem("myItem05");
    order = given()
      .log().all()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(order)
      .when()
      .put("/orders")
      .then().log().all()
      .statusCode(HttpStatus.SC_ACCEPTED)
      .extract().body().as(Order.class);
    assertThat(order).isNotNull();
    assertThat(order.getItem()).isEqualTo("myItem05");
  }

  @org.junit.jupiter.api.Order(70)
  @Test
  public void testDeleteOrder()
  {
    given()
      .log().all()
      .accept(ContentType.JSON)
      .when().pathParam("id", orderId)
      .delete("/orders/{id}")
      .then().log().all()
      .statusCode(HttpStatus.SC_NO_CONTENT);
  }

  @org.junit.jupiter.api.Order(100)
  @Test
  public void testDeleteCustomer()
  {
    given()
      .log().all()
      .accept(ContentType.JSON)
      .when().pathParam("id", customerId)
      .delete("/customers/{id}")
      .then().log().all()
      .statusCode(HttpStatus.SC_NO_CONTENT);
  }*/
}
