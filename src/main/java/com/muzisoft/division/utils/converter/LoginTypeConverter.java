package com.muzisoft.division.utils.converter;

import com.muzisoft.division.domain.common.enums.LoginType;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;

@Configuration
public class LoginTypeConverter implements AttributeConverter<LoginType, String> {

    @Override
    public String convertToDatabaseColumn(LoginType attribute) {
        if (ObjectUtils.isEmpty(attribute))
            return null;
        return attribute.getValue();
    }

    @Override
    public LoginType convertToEntityAttribute(String dbData) {
        if(!StringUtils.hasText(dbData))
            return null;
        return LoginType.find(dbData);
    }
}