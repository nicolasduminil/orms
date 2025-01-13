package fr.simplex_software.workshop.quarkus.orm.data.serializers;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import fr.simplex_software.workshop.quarkus.orm.data.*;

import java.io.*;
import java.util.*;

public class OrderListSerializer extends JsonSerializer<List<Order>>
{
  @Override
  public void serialize(List<Order> orders, JsonGenerator jsonGenerator,
                        SerializerProvider serializerProvider) throws IOException
  {
    jsonGenerator.writeStartArray();
    for (Order order : orders)
    {
      jsonGenerator.writeStartObject();
      if (order.getId() != null)
        jsonGenerator.writeNumberField("id", order.getId());
      jsonGenerator.writeStringField("item", order.getItem());
      jsonGenerator.writeNumberField("price", order.getPrice());
      jsonGenerator.writeObjectField("customer", order.getCustomer());
      jsonGenerator.writeEndObject();
    }
    jsonGenerator.writeEndArray();
  }
}
