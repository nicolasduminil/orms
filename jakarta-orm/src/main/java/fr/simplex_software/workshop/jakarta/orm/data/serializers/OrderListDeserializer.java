package fr.simplex_software.workshop.jakarta.orm.data.serializers;

import fr.simplex_software.workshop.jakarta.orm.data.*;
import jakarta.json.*;
import jakarta.json.bind.*;
import jakarta.json.bind.serializer.*;
import jakarta.json.stream.*;

import java.lang.reflect.*;
import java.util.*;

public class OrderListDeserializer implements JsonbDeserializer<List<Order>>
{
  @Override
  public List<Order> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext, Type type)
  {
    List<Order> orders = new ArrayList<>();
    JsonArray jsonArray = jsonParser.getArray();

    for (JsonValue value : jsonArray)
      if (value.getValueType() == JsonValue.ValueType.OBJECT)
      {
        JsonObject orderObject = value.asJsonObject();
        Order order = new Order();
        order.setId(orderObject.getJsonNumber("id").longValue());
        order.setItem(orderObject.getString("item"));
        order.setPrice(orderObject.getJsonNumber("price").longValue());
        JsonObject customerObject = orderObject.getJsonObject("customer");
        Jsonb jsonb = JsonbBuilder.create();
        Customer customer = jsonb.fromJson(customerObject.toString(), Customer.class);
        order.setCustomer(customer);
        orders.add(order);
      }
    return orders;
  }
}
