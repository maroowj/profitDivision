package com.muzisoft.division.utils.converter;

import com.muzisoft.division.domain.common.enums.AccountBookType;
import com.muzisoft.division.domain.common.enums.ProfitLogType;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import javax.persistence.AttributeConverter;

@Configuration
public class ProfitLogTypeConverter implements AttributeConverter<ProfitLogType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProfitLogType attribute) {
        if (ObjectUtils.isEmpty(attribute))
            return -1;
        return attribute.getValue();
    }

    @Override
    public ProfitLogType convertToEntityAttribute(Integer dbData) {
        if (ObjectUtils.isEmpty(dbData))
            return null;
        return ProfitLogType.find(dbData);
    }
}