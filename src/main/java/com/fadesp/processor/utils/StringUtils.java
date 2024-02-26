package com.fadesp.processor.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {

  public static String getOnlyNumbers(String cpfFormatado) {
    return cpfFormatado.replaceAll("\\D+", "");
  }
}
