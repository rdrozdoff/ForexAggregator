package org.home.forex.serialization;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.home.forex.model.Quote;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class QuoteSerde implements Serde<Quote> {

    @Override
    public Serializer<Quote> serializer() {
        return new JsonSerializer<Quote>();
    }

    @Override
    public Deserializer<Quote> deserializer() {
        JsonDeserializer<Quote> deserializer = new JsonDeserializer<>(Quote.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        return deserializer;
    }
}
