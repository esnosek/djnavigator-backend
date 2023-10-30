package dev.nos.djnavigator.collection.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        return ofNullable(strings)
                .map(s -> String.join(SPLIT_CHAR, s))
                .orElse(Strings.EMPTY);
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        return ofNullable(string)
                .map(s -> Arrays.asList(s.split(SPLIT_CHAR)))
                .orElse(emptyList());
    }
}