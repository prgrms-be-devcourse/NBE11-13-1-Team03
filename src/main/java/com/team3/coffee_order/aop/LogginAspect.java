package com.team3.coffee_order.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

//* @Aspect
//"이 클래스는 공통 기능(횡단 관심사)을 모아둔 Aspect다" 라고 선언하는 애너테이션
//이 애너테이션이 붙어야 스프링 AOP가 이 클래스 안의 포인트컷/어드바이스를 인식함
//"AOP 규칙을 담고 있다"는 표시일 뿐, 스프링이 관리하는 빈으로 등록해주지 않음
//스프링 컨테이너에 빈으로 등록해야, 스프링이 이 Aspect를 찾아서 실제로 적용함
@Aspect
@Slf4j
//* System.out.println -> @Sl4fj로 교체한 이유
//(1) 레벨이 없음 : 전부 같은 급이라, 운영에서 "디버그성 출력만 끄기" 같은 제어가 불가능
//(2) 맥락이 없음 : 시간/스레드/클래스 이름이 자동으로 안 붙음 => 문제 추적이 어려움
//(3) 목적이가 고정임 : 콘솔에만 나감. 파일 저장, 날짜별 분할(롤링) 등을 할 수 없음
//(4) 성능에 불리 : 동기 출력이라 요청이 몰리면 병목이 될 수 있음.
// # log.info(...) 로 바꾸면 위 네 가지가 전부 해결된다 - 출력 형태를 보면 차이가 바로 보인다:
//     System.out : [요청 시작] GET /api/boards -> ...
//     log.info   : 2026-07-14T10:00:00.123+09:00  INFO 12345 --- [nio-8080-exec-1] c.e.s.b.aop.LoggingAspect : [요청 시작] ...
//                  └ 시간 ──────────────────────┘ └레벨┘ └PID┘  └── 스레드 ──────┘ └── 어느 클래스가 찍었나 ┘
@Component
public class LogginAspect {
    //Pointcut : "어디에" 적용할 지 정의
    //표현식 해석 : execution(* com.example.spring.basicboard.controller..*.*(..))
    //* : 그 안의 모든 클래스의 모든 메서드
    //(..) : 메서드 파라미터는 개수/타입 상관없이 모두
    //-> "controller 패키지 아래 모든 메서드"를 대상으로 삼겠다는 뜻

    @Pointcut("execution(* com.team3.coffee_order.controller..*(..))")
    public void controllerLog(){
        //메서드 본문(body)는 비워둔다. 실제 로직이 아니라, "대상을 가리기는 이름표" 역할만 수행하기 때문
    }

    //@Around : "언제/무엇을" 할 지 정의하는 어드바이스
    //어드바이스에는 5가지 종류가 있음

    //@Before : 대상 메서드 실행 "직전"에만 실행
    //@AfterReturning : 대상 메서드가 "정상 반환된 후" 실행
    //@AfterThrowing : 대상 메서드가 "예외를 던졌을 때" 실행
    //@After : 정상/예외 상관없이 "끝나면 항상" 실행
    //@Around : 대상 메서드 실행을 "통째로 감싼다". 전/후 예외를 모두 한 메서드에서 제어 가능

    @Around("controllerLog()")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable{
        //* ProceedingJoinPoint
        //지금 가로챈 "그 지점(메서드 호출)"에 대한 정보를 담은 객체
        //어떤 메서드가 호출됐는지(getSigniture), 넘어온 인자는 무엇인지(getArgs) 등을 꺼낼 수 있음
        //@Around 에서만 쓰는 특별할 타입. proceed()로 "진짜 대상 메서드를 실행" 시킬 수 있다.

        String method = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();

        String httpInfo = "";

        //RequestContextHolder : 스프링이 "지금 이 요청"의 정보를 담아두는 보관소. 어디서든 꺼낼 수 잇음
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes!=null){
            HttpServletRequest request = attributes.getRequest();
            httpInfo = request.getMethod()+" "+request.getRequestURI();
        }

        //=== 대상 메서드 실행 "전" 로깅
        log.info("[요청 시작] {} -> {}",httpInfo, method);
        log.info("[파라미터] {}",Arrays.toString(joinPoint.getArgs()));
//        System.out.println("[요청 시작] "+httpInfo+" -> "+method);
//        System.out.println("[파라미터] "+ Arrays.toString(joinPoint.getArgs()));

        long start = System.currentTimeMillis();

        try{
            // 이 한 줄을 기준으로 요청받은 메서드 실행 전, 실행 후 로 나뉜다.
            Object result = joinPoint.proceed();

            long end = System.currentTimeMillis() -start; //걸린 시간

            //=== 대상 메서드가 "정상 종료"된 후 로깅
            log.info("[요청 완료] {} : {}",method, end);
            //System.out.println("[요청 완료] "+method+" : "+end+"ms");

            return result;
        }catch (Throwable e){
            //=== 대상 메서드가 "예외를 던졌을 때" 로깅
            long end = System.currentTimeMillis()-start;
            log.warn("[요청 실패] {} : {}",method, end);
            //System.out.println("[요청 실패] "+method+" : "+end+"ms");

            //잡은 예외를 다시 던짐
            //여기서 예외를 던지지 않고 삼키면, 컨트롤러는 정상 처리된 것처럼 보여 버그가 됨
            throw e;
        }
    }
}
