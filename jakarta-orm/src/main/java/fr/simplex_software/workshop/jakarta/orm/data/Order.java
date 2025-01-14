package fr.simplex_software.workshop.jakarta.orm.data;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

@Entity
@Table(name = "ORDERS")
@NamedQuery(name = "Orders.findAll",
  query = "SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.item")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order
{
  @Id
  @SequenceGenerator(name = "orderSequence", sequenceName = "orderId_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSequence")
  @XmlAttribute
  @Column(name = "ID", nullable = false, length = 40)
  private Long id;
  @Column(name = "ITEM", length = 40)
  @XmlAttribute
  private String item;
  @Column(name = "PRICE", length = 40)
  @XmlAttribute
  private Long price;
  @ManyToOne
  @JoinColumn(name = "CUSTOMER_ID")
  @XmlTransient
  private Customer customer;

  public Order()
  {
  }

  public Order(String item, Long price)
  {
    this.item = item;
    this.price = price;
  }

  public Order(String item, Long price, Customer customer)
  {
    this(item, price);
    this.customer = customer;
  }

  public Customer getCustomer()
  {
    return customer;
  }

  public void setCustomer(Customer customer)
  {
    this.customer = customer;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getItem()
  {
    return item;
  }

  public void setItem(String item)
  {
    this.item = item;
  }

  public Long getPrice()
  {
    return price;
  }

  public void setPrice(Long price)
  {
    this.price = price;
  }

  @Override
  public String toString()
  {
    return "Order{" +
      "id=" + id +
      ", item='" + item + '\'' +
      ", price=" + price +
      '}';
  }
}
