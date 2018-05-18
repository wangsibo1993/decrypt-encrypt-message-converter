package com.sibo.decryptEncrypt.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludeEncryptDecrypt {
}