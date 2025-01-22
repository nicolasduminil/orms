package fr.simplex_software.workshop.quarkus.orm.data;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

import java.util.*;

@Cacheable
@Entity
@Table(name = "CUSTOMERS")
@NamedQuery(name = "Customers.findAll",
  query = "SELECT c FROM Customer c ORDER BY c.id",
  hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@NamedQuery(
  name = "Customer.findAllWithOrders",
  query = "SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.orders"
)
@NamedQuery(
  name = "Customer.findCustomerByIdWithOrders",
  query = "SELECT c FROM Customer c LEFT JOIN FETCH c.orders where c.id = :id"
)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonPropertyOrder({"id", "firstName", "lastName", "email", "phone", "orders"})
public class Customer
{
  @Id
  @SequenceGenerator(name = "customerSequence", sequenceName = "customerId_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerSequence")
  @Column(name = "ID", nullable = false)
  @XmlAttribute
  @JsonProperty
  private Long id;
  @Column(name = "FIRST_NAME", nullable = false, length = 40)
  @XmlAttribute
  @JsonProperty
  private String firstName;
  @Column(name = "LAST_NAME", nullable = false, length = 40)
  @XmlAttribute
  @JsonProperty
  private String lastName;
  @Column(name = "EMAIL", nullable = false, unique = true, length = 40)
  @XmlAttribute
  @JsonProperty
  private String email;
  @Column(name = "PHONE", nullable = false, unique = true, length = 40)
  @XmlAttribute
  @JsonProperty
  private String phone;
  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
  @JsonManagedReference
  @XmlTransient
  @JsonProperty
  public List<Order> orders = new ArrayList<>();

  public Customer() {}

  public Customer(String firstName, String lastName, String email, String phone)
  {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
  }

  public Customer(String firstName, String lastName, String email, String phone, List<Order> orders)
  {
    this(firstName, lastName, email, phone);
    this.orders = orders;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
  }

  public List<Order> getOrders()
  {
    return orders;
  }

  public List<Order> addOrder(Order order)
  {
    orders.add(order);
    return orders;
  }

  @Override
  public String toString()
  {
    return "Customer{" +
      "id=" + id +
      ", firstName='" + firstName + '\'' +
      ", lastName='" + lastName + '\'' +
      ", email='" + email + '\'' +
      ", phone='" + phone + '\'' +
      ", orders=" + orders +
      '}';
  }
}
