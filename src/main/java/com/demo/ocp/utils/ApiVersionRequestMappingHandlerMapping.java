package com.demo.ocp.utils;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final String prefix;

    public ApiVersionRequestMappingHandlerMapping(final String prefix) {
        super();
        this.prefix = prefix;
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(final Method method, final Class<?> handlerType) {
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
        if(info == null) return null;

        final ApiVersion methodAnnotation = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        if(methodAnnotation != null) {
            final RequestCondition<?> methodCondition = this.getCustomMethodCondition(method);
            // Concatenate our ApiVersion with the usual request mapping
            info = this.createApiVersionInfo(methodAnnotation, methodCondition).combine(info);
        } else {
            final ApiVersion typeAnnotation = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
            if(typeAnnotation != null) {
                final RequestCondition<?> typeCondition = this.getCustomTypeCondition(handlerType);
                // Concatenate our ApiVersion with the usual request mapping
                info = this.createApiVersionInfo(typeAnnotation, typeCondition).combine(info);
            }
        }

        return info;
    }

    private RequestMappingInfo createApiVersionInfo(final ApiVersion annotation, final RequestCondition<?> customCondition) {
        final ApiVersions[] values = annotation.value();
        final String[] patterns = new String[values.length];
        for(int i=0; i<values.length; i++) {
            // Build the URL prefix
            patterns[i] = this.prefix +values[i].getPrefix();
        }

        return new RequestMappingInfo(
            new PatternsRequestCondition(patterns, this.getUrlPathHelper(), this.getPathMatcher(), this.useSuffixPatternMatch(), this.useTrailingSlashMatch(), this.getFileExtensions()),
            new RequestMethodsRequestCondition(),
            new ParamsRequestCondition(),
            new HeadersRequestCondition(),
            new ConsumesRequestCondition(),
            new ProducesRequestCondition(),
            customCondition);
    }

}
