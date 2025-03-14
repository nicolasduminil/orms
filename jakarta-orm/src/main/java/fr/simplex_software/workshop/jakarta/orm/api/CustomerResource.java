package fr.simplex_software.workshop.jakarta.orm.api;

import fr.simplex_software.workshop.jakarta.orm.data.*;
import fr.simplex_software.workshop.jakarta.orm.repository.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.*;
import java.util.*;

@ApplicationScoped
@Path("customers")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class CustomerResource
{
  @Inject
  CustomerRepository customerRepository;

  @GET
  public Response getAll()
  {
    return Response.ok().entity(new GenericEntity<List<Customer>>(customerRepository.findAll()) {}).build();
  }

  @GET
  @Path("/{id}")
  public Response getCustomer(@PathParam("id") Long id)
  {
    return Response.ok().entity(customerRepository.findCustomerById(id)).build();
  }

  @POST
  public Response create(Customer customer)
  {
    return Response.created(URI.create("/customers/" + customer.getId()))
      .entity(customerRepository.createCustomer(customer)).build();
  }

  @PUT
  public Response update(Customer customer)
  {
    return Response.accepted().entity(customerRepository.updateCustomer(customer)).build();
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long customerId)
  {
    customerRepository.deleteCustomer(customerId);
    return Response.noContent().build();
  }
}
