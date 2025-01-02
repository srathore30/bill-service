package sfa.bill_service.interceptor;

import sfa.bill_service.constants.UserRole;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAuthorization {
    UserRole[] allowedRoles();

}