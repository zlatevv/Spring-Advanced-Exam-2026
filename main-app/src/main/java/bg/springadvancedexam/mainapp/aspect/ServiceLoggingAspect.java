package bg.springadvancedexam.mainapp.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Around("execution(* bg.springadvancedexam.mainapp.service..*(..))")
    public Object logServiceCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();

        log.info("Entering {}", method);
        try {
            Object result = joinPoint.proceed();
            log.info("Exiting {}", method);
            return result;
        } catch (Throwable ex) {
            log.warn("Exception in {}: {}", method, ex.getMessage());
            throw ex;
        }
    }
}
