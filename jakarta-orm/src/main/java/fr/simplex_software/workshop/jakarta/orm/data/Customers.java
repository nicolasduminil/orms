package fr.simplex_software.workshop.jakarta.orm.data;

import jakarta.xml.bind.annotation.*;

import java.util.*;
import java.util.ArrayList;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customers
{
  @XmlElement(name = "customer")
  private List<Customer> customers;

  public Customers()
  {
    customers = new ArrayList<>();
  }

  public Customers(List<Customer> customers)
  {
    this.customers = customers;
  }

  public List<Customer> getCustomers()
  {
    return customers;
  }

  public void setCustomers(List<Customer> customers)
  {
    this.customers = customers;
  }

  public void addCustomer(Customer customer)
  {
    customers.add(customer);
  }
}
