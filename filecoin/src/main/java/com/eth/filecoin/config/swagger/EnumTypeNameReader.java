package com.eth.filecoin.config.swagger;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiModelProperty;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.Annotations;
import springfox.documentation.service.ListVendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.schema.ApiModelProperties;

/**
 * @author catface
 * @date 2020/5/16
 */
@Component
@Order
public class EnumTypeNameReader implements ModelPropertyBuilderPlugin {

  @Override
  public void apply(ModelPropertyContext context) {
    try {
      Optional<ApiModelProperty> annotation = Optional.absent();
      if (context.getAnnotatedElement().isPresent()) {
        annotation = annotation.or(
            ApiModelProperties.findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
      }
      if (context.getBeanPropertyDefinition().isPresent()) {
        annotation = annotation.or(Annotations.findPropertyAnnotation(
            context.getBeanPropertyDefinition().get(),
            ApiModelProperty.class));
      }
      if (annotation.isPresent()) {
        JavaType enumType = findEnumType(
            context.getBeanPropertyDefinition().get().getPrimaryType());
        if (enumType != null) {
          Class enumClass = enumType.getRawClass();
          Object[] values = enumClass.getEnumConstants();
          Field descField = Arrays.stream(enumClass.getDeclaredFields()).filter(
              field -> field.getType() == String.class)
              .findFirst().orElse(null);
          if (descField != null) {
            descField.setAccessible(true);
          }
          String[] descArray = new String[values.length];
          for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            String desc = "";
            if (descField != null) {
              desc = (String) descField.get(value);
            }
            descArray[i] = desc;
          }
          ListVendorExtension<String> descExtension = new ListVendorExtension<>(
              "ex-enum-descriptions",
              Arrays.asList(descArray));
          context.getBuilder().description(
              annotation.get().value() + String.format("[%s]",
                  enumType.getRawClass().getSimpleName())).extensions(
              Collections.singletonList(descExtension));
        }
      }
    } catch (Exception e) {
    }
  }

  private JavaType findEnumType(JavaType javaType) {
    if (javaType.getRawClass().isEnum()) {
      return javaType;
    }
    for (int i = 0; i < javaType.containedTypeCount(); i++) {
      JavaType enumType = findEnumType(javaType.containedType(i));
      if (enumType != null) {
        return enumType;
      }
    }
    return null;
  }

  @Override
  public boolean supports(DocumentationType documentationType) {
    return true;
  }
}
