package com.yeohangttukttak.api.global.config.converter;

import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.StringUtils;

public class StringToValueBasedEnumConverter implements ConverterFactory<String, ValueBasedEnum> {

    private static class SingleEnumConverter<T extends Enum<T> & ValueBasedEnum> implements Converter<String, T> {
        private final Class<T> enumType;

        public SingleEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if (StringUtils.isEmpty(source)) {
                return null;
            }
            for (T enumConstant : enumType.getEnumConstants()) {
                if (enumConstant.getValue().equalsIgnoreCase(source)) {
                    return enumConstant;
                }
            }
            throw new IllegalArgumentException("No enum constant " + enumType.getCanonicalName() + " with value " + source);
        }
    }

    @Override
    public <T extends ValueBasedEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new SingleEnumConverter(targetType);
    }
}