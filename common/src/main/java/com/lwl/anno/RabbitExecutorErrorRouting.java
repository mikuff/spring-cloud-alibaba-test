package com.lwl.anno;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RabbitExecutorErrorRouting {

   String exchange();
   String routingKey();
   String desc();

}
