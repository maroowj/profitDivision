package com.muzisoft.division.utils.converter;

import com.muzisoft.division.domain.common.enums.PointSupplier;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import javax.persistence.AttributeConverter;

@Configuration
public class PointSupplierConverter implements AttributeConverter<PointSupplier, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PointSupplier attribute) {
        if (ObjectUtils.isEmpty(attribute))
            return -1;
        return attribute.getValue();
    }

    @Override
    public PointSupplier convertToEntityAttribute(Integer dbData) {
        if (ObjectUtils.isEmpty(dbData))
            return null;
        return PointSupplier.find(dbData);
    }
}