package com.entic.payroll.core.application.commons.errors;

import java.util.HashMap;
import java.util.Map;

public interface ApplicationErrorSpec {
    String errorCode();
    String getMessage();
    default Object[] messageArgs(){
        return new Object[]{};
    }
    default Map<String, Object> metadata(){
        return new HashMap<>();
    }
}
