package dev.nos.djnavigator.collection.model.converter;

import dev.nos.djnavigator.collection.model.AlbumId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AlbumIdConverter implements AttributeConverter<AlbumId, String> {
    @Override
    public String convertToDatabaseColumn(AlbumId attribute) {
        return attribute.id();
    }

    @Override
    public AlbumId convertToEntityAttribute(String dbData) {
        return AlbumId.from(dbData);
    }
}
