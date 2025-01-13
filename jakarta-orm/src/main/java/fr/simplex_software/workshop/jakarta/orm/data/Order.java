package fr.simplex_software.workshop.jakarta.orm.data;

import jakarta.persistence.*;

@Entity
@Table(name = "ORDERS")
@NamedQuery(name = "Orders.findAll",
  query = "SELECT o FROM Order o WHERE o.customer.id = :customerId ORDER BY o.item")

public class Order
{
  @Id
  @SequenceGenerator(name = "orderSequence", sequenceName = "orderId_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSequence")
  private Long id;
  @Column(length = 40)
  private String item;
  @Column
  private Long price;
  @ManyToOne
  @JoinColumn(name = "customer_id")
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
