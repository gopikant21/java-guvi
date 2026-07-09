package org.example.emidefaulter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.example.emidefaulter.entity.LoanStatus;
import org.example.emidefaulter.entity.LoanType;
import org.example.emidefaulter.entity.PaymentStatus;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LoanStatusConverter());
        registry.addConverter(new PaymentStatusConverter());
        registry.addConverter(new LoanTypeConverter());
    }

    private static @Nullable LoanStatus parseLoanStatus(@Nullable String source) {
        return parseEnum(source, LoanStatus.class);
    }

    private static @Nullable PaymentStatus parsePaymentStatus(@Nullable String source) {
        return parseEnum(source, PaymentStatus.class);
    }

    private static @Nullable LoanType parseLoanType(@Nullable String source) {
        return parseEnum(source, LoanType.class);
    }

    private static <E extends Enum<E>> @Nullable E parseEnum(@Nullable String source, Class<E> enumType) {
        if (source == null) {
            return null;
        }
        String normalized = source.trim().toUpperCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            return null;
        }
        return Enum.valueOf(enumType, normalized);
    }

    private static class LoanStatusConverter implements Converter<String, LoanStatus> {

        @Override
        public LoanStatus convert(@Nullable String source) {
            return parseLoanStatus(source);
        }
    }

    private static class PaymentStatusConverter implements Converter<String, PaymentStatus> {

        @Override
        public PaymentStatus convert(@Nullable String source) {
            return parsePaymentStatus(source);
        }
    }

    private static class LoanTypeConverter implements Converter<String, LoanType> {

        @Override
        public LoanType convert(@Nullable String source) {
            return parseLoanType(source);
        }
    }
}



