package fr.simplex_software.workshop.quarkus.orm.data.serializers;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import fr.simplex_software.workshop.quarkus.orm.data.*;

import java.io.*;
import java.util.*;

public class OrderListDeserializer extends JsonDeserializer<List<Order>>
{
  @Override
  public List<Order> deserialize(JsonParser p, DeserializationContext ctxt)
    throws IOException
  {
    List<Order> orders = new ArrayList<>();
    JsonNode node = p.getCodec().readTree(p);
    if (node.isArray())
    {
      for (JsonNode orderNode : node)
      {
        Order order = new Order();
        order.setId(orderNode.get("id").asLong());
        order.setItem(orderNode.get("item").asText());
        order.setPrice(orderNode.get("price").asLong());
        order.setCustomer(ctxt.readValue(orderNode.get("customer").traverse(), Customer.class));
        orders.add(order);
      }
    }
    return orders;
  }
}
