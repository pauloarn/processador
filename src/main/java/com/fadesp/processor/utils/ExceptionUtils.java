package com.fadesp.processor.utils;

import com.fadesp.processor.ProcessorApplication;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
public class ExceptionUtils {
  public static void logException(Exception ex) {
    var packageName = ProcessorApplication.class.getPackageName();
    log.error("==================================================");
    log.error("[ {} ] :: {}", ex.toString(), ex.getMessage());
    log.error("==================================================");
    Arrays.stream(ex.getStackTrace())
        .filter(st -> st.getClassName().contains(packageName))
        .filter(st -> st.getLineNumber() != -1)
        .forEach(st -> {
          log.error("Stack: [ file: {} ] :: [ class: {} - {} ] [ line: {} ]", st.getFileName(), st.getClassName(), st.getMethodName(), st.getLineNumber());
        });
  }
}
