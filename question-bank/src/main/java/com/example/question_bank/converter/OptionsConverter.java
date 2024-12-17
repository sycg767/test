package com.example.question_bank.converter;

import com.example.question_bank.entity.QuestionOption;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Converter
public class OptionsConverter implements AttributeConverter<List<QuestionOption>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(OptionsConverter.class);

    @Override
    public String convertToDatabaseColumn(List<QuestionOption> options) {
        try {
            return objectMapper.writeValueAsString(options);
        } catch (Exception e) {
            log.error("Error converting options to JSON", e);
            return "[]";
        }
    }

    @Override
    public List<QuestionOption> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(dbData, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, QuestionOption.class));
        } catch (Exception e) {
            log.error("Error converting JSON to options", e);
            return new ArrayList<>();
        }
    }
} 