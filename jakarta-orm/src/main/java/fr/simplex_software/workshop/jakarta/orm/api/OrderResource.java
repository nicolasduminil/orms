package fr.simplex_software.workshop.jakarta.orm.api;

import fr.simplex_software.workshop.jakarta.orm.data.*;
import fr.simplex_software.workshop.jakarta.orm.repository.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.*;
import java.util.List;

@ApplicationScoped
@Path("orders")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class OrderResource
{
  @Inject
  private OrderRepository orderRepository;

  @GET
  public Response getAll()
  {
    return Response.ok().entity(new GenericEntity<List<Order>>(orderRepository.findAll()) {}).build();
  }

  @GET
  @Path("/customer/{customerId}")
  public Response getAllWithCustomer(@PathParam("customerId") Long customerId)
  {
    return Response.ok().entity(orderRepository.findAllByCustomerId(customerId)).build();
  }

  @GET
  @Path("/{id}")
  public Response get(@PathParam("id") Long id)
  {
    return Response.ok().entity(orderRepository.findOrderById(id)).build();
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
