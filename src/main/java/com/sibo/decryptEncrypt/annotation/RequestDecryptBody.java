package com.sibo.decryptEncrypt.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestDecryptBody {
}