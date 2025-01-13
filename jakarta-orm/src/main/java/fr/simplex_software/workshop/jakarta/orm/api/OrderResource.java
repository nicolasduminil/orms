package fr.simplex_software.workshop.jakarta.orm.api;

import fr.simplex_software.workshop.jakarta.orm.data.*;
import fr.simplex_software.workshop.jakarta.orm.repository.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.*;

@ApplicationScoped
@Path("orders")
@Produces("application/json")
@Consumes("application/json")
public class OrderResource
{
  @Inject
  private OrderRepository orderRepository;

  @GET
  public Response getAll()
  {
    return Response.ok().entity(orderRepository.findAll()).build();
  }

  @GET
  @Path("/{customerId}")
  public Response getAllWithCustomer(@PathParam("customerId") Long customerId)
  {
    return Response.ok().entity(orderRepository.findAllByCustomerId(customerId)).build();
  }

  @POST
  public Response create(Order order)
  {
    return Response.created(URI.create("/orders/" + order.getId()))
      .entity(orderRepository.createOrder(order)).build();
  }

  @PUT
  public Response update(Order order)
  {
    return Response.accepted().entity(orderRepository.updateOrder(order)).build();
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long orderId)
  {
    orderRepository.deleteOrder(orderId);
    return Response.noContent().build();
  }
}
