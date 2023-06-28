package com.muzisoft.division.utils.manager;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

import static org.hibernate.internal.util.config.ConfigurationHelper.getString;

public class SeqManager extends SequenceStyleGenerator {

    public static final String VALUE_USER_SEQ_PARAMETER = "valueUserParameter";
    public static final String VALUE_USER_SEQ_DEFAULT = "";

    private String valueUserParameter;

    public static final String NUMBER_FORMAT_PARAMETER = "numberFormat";
    public static final String NUMBER_FORMAT_DEFAULT = "%0d";

    private String numberFormat;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {

        super.configure(LongType.INSTANCE, params, serviceRegistry);
        valueUserParameter = getString(VALUE_USER_SEQ_PARAMETER, params, VALUE_USER_SEQ_DEFAULT);
        numberFormat = getString(NUMBER_FORMAT_PARAMETER, params, NUMBER_FORMAT_DEFAULT);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        return valueUserParameter + String.format(numberFormat, super.generate(session, object));
    }
}

