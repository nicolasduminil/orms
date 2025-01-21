package fr.simplex_software.workshop.jakarta.orm.repository;

import fr.simplex_software.workshop.jakarta.orm.data.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;
import jakarta.persistence.*;
import jakarta.transaction.*;
import jakarta.ws.rs.*;

import java.util.*;

@ApplicationScoped
public class OrderRepository
{
  @PersistenceContext(unitName = "orders")
  EntityManager entityManager;

  public List<Order> findAll()
  {
    return (List<Order>) entityManager.createNamedQuery("Orders.findAll")
      .getResultList();
  }
  public List<Order> findAllByCustomerId(Long customerId)
  {
    return (List<Order>) entityManager.createNamedQuery("Orders.findAllWithCustomerId")
      .setParameter("customerId", customerId)
      .getResultList();
  }

  public Order findOrderById(Long id)
  {
    Order order = entityManager.find(Order.class, id);
    if (order == null)
    {
      throw new WebApplicationException("Order with id of " + id + " does not exist.", 404);
    }
    return order;
  }

  @Transactional
  public Order updateOrder(Order order)
  {
    Order orderToUpdate = findOrderById(order.getId());
    orderToUpdate.setItem(order.getItem());
    orderToUpdate.setPrice(order.getPrice());
    entityManager.merge(orderToUpdate);
    return orderToUpdate;
  }

  @Transactional
  public Order createOrder(Order order)
  {
    entityManager.persist(order);
    return order;
  }

  @Transactional
  public void deleteOrder(Long orderId)
  {
    Order o = findOrderById(orderId);
    entityManager.remove(o);
  }
}
