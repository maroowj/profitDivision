package com.muzisoft.division.utils.converter;

import com.muzisoft.division.domain.common.enums.AccountBookType;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import javax.persistence.AttributeConverter;

@Configuration
public class AccountBookTypeConverter implements AttributeConverter<AccountBookType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AccountBookType attribute) {
        if (ObjectUtils.isEmpty(attribute))
            return -1;
        return attribute.getValue();
    }

    @Override
    public AccountBookType convertToEntityAttribute(Integer dbData) {
        if (ObjectUtils.isEmpty(dbData))
            return null;
        return AccountBookType.find(dbData);
    }
}