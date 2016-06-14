package root.etc.report.classifier.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReservedKey 
{
	boolean isAgregation() default false;
	boolean isFilter() default false;
	boolean isTemporal() default false;
	String preColumnValue() default "";
	String postColumnValue() default "";
	String filterValue() default "";
	
}
