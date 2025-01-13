package fr.simplex_software.workshop.jakarta.orm.data.serializers;

import fr.simplex_software.workshop.jakarta.orm.data.*;
import jakarta.json.bind.serializer.*;
import jakarta.json.stream.*;

import java.util.*;

public class OrderListSerializer implements JsonbSerializer<List<Order>>
{

  @Override
  public void serialize(List<Order> orders, JsonGenerator generator, SerializationContext serializationContext)
  {
    generator.writeStartArray();
    for (Order order : orders)
    {
      generator.writeStartObject();
      generator.write("id", order.getId());
      generator.write("item", order.getItem());
      generator.write("price", order.getPrice());
      if (order.getCustomer() != null)
      {
        generator.writeStartObject("customer");
        Customer customer = order.getCustomer();
        generator.write("id", customer.getId());
        generator.write("firstName", customer.getFirstName());
        generator.write("lastName", customer.getLastName());
        generator.write("email", customer.getEmail());
        generator.writeEnd(); // end customer object
      }
      generator.writeEnd(); // end order object
    }
    generator.writeEnd(); // end array
  }
}
