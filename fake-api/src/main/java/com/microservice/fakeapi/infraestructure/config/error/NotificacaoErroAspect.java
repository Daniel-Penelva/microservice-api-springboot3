package com.microservice.fakeapi.infraestructure.config.error;

import com.microservice.fakeapi.business.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class NotificacaoErroAspect {

    private EmailService emailService;

    @Pointcut("@within(com.microservice.fakeapi.infraestructure.config.error.NotificacaoErro) || @annotation(com.microservice.fakeapi.infraestructure.config.error.NotificacaoErro)")
    public void notificacaoErroPointCut() {};

    @AfterThrowing(pointcut = "notificacaoErroPointCut()", throwing = "e")
    public void notificacaoErro(final Exception e){
        emailService.enviaEmailExcecao(e);
    }
}
