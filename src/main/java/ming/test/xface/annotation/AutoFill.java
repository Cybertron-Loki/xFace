package ming.test.xface.annotation;

import ming.test.xface.aspect.OperationType;
import org.aspectj.lang.annotation.Aspect;

import java.lang.annotation.*;


@Aspect
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
     String value();
}
