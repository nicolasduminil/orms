package fr.simplex_software.workshop.jakarta.orm.data;

import jakarta.xml.bind.annotation.*;

import java.util.*;

@XmlRootElement(name = "orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class Orders
{
  @XmlElement(name = "order")
  private List<Order> orders;

  public Orders() {}

  public Orders(List<Order> orders) {
    this.orders = orders;
  }

  public List<Order> getOrders()
  {
    return orders;
  }

  public void setOrders(List<Order> orders)
  {
    this.orders = orders;
  }

  public void addOrder(Order order)
  {
    orders.add(order);
  }
}
