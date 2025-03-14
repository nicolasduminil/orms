package fr.simplex_software.workshop.quarkus.orm.repository;

import fr.simplex_software.workshop.quarkus.orm.data.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;
import jakarta.persistence.*;
import jakarta.transaction.*;
import jakarta.ws.rs.*;

import java.util.*;

@ApplicationScoped
public class CustomerRepository
{
  @Inject
  EntityManager entityManager;

  public List<Customer> findAll()
  {
    return entityManager.createNamedQuery("Customer.findAllWithOrders", Customer.class)
      .getResultList();
  }

  public Customer findCustomerById(Long id)
  {
    Customer customer = entityManager
      .createNamedQuery("Customer.findCustomerByIdWithOrders", Customer.class)
      .setParameter("id", id).getSingleResult();
    if (customer == null)
    {
      throw new WebApplicationException("Customer with id of " + id + " does not exist.", 404);
    }
    return customer;
  }

  @Transactional
  public Customer updateCustomer(Customer customer)
  {
    Customer customerToUpdate = findCustomerById(customer.getId());
    customerToUpdate.setFirstName(customer.getFirstName());
    customerToUpdate.setLastName(customer.getLastName());
    customerToUpdate.setEmail(customer.getEmail());
    customerToUpdate.setPhone(customer.getPhone());
    entityManager.merge(customerToUpdate);
    return customerToUpdate;
  }

  @Transactional
  public Customer createCustomer(Customer customer)
  {
    entityManager.persist(customer);
    return customer;
  }

  @Transactional
  public void deleteCustomer(Long customerId)
  {
    Customer c = findCustomerById(customerId);
    entityManager.remove(c);
  }
}
