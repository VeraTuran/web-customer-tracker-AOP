package com.luv2code.springdemo.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CRMLoggingAspect {
	// mettre en place le logger
	private Logger logger = Logger.getLogger(getClass().getName());
	// declaration pointcut
	@Pointcut("execution (* com.luv2code.springdemo.controller.*.*(..))")
	private void forControllerPackage() {
	}

	@Pointcut("execution(* com.luv2code.springdemo.service.*.*(..))")
	private void forServicePackage() {
	}

	@Pointcut("execution(* com.luv2code.springdemo.dao.*.*(..))")
	private void forDaoPackage() {
	}

	// combiner expressions pointcut
	@Pointcut("forControllerPackage() || forDaoPackage() || forServicePackage()")
	private void forAppFlow() {
	}
	// ajouter un advice : @Before
	@Before("forAppFlow()")
	public void before(JoinPoint jp) {
		// afficher la methode
		String methode = jp.getSignature().toShortString();
		logger.info("------------> in @Before " + methode);

		// afficher les args à la methode
		Object[] args = jp.getArgs();

		// loop thru and display args
		for (Object tempArg : args) {
			logger.info("------------> argument: " + tempArg);
		}
	}
	
	// @AfterReturning advice
	@AfterReturning(pointcut = "forAppFlow()", returning = "result")
	public void afterReturning(JoinPoint jp, Object result) {
		// afficher methode
		String methode = jp.getSignature().toShortString();
		logger.info("------------> in @AfterReturning " + methode);
		// display data returned
		logger.info("------------> result: " + result);
	}
}
