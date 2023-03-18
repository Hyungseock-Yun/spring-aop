package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ExecutionTest {

  AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
  Method helloMethod;

  @BeforeEach
  public void init() throws NoSuchMethodException {
    helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
  }

  @Test
  void printMethod() {
    //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    log.info("helloMethod={}", helloMethod);
  }

  /**
   * 접근 제어자 : public
   * 반환 타입 : String
   * 선언 타입 : hello.aop.member.MemberServiceImpl
   * 메서드 이름 : hello
   * 파라미터 : (String) <- 파라미터 타입
   * 예외 : 생략
   */
  @Test
  void exactMatch() {
    //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void allMatch() {
    pointcut.setExpression("execution(* *(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void nameMatch() {
    pointcut.setExpression("execution(* hello(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void nameMatchStar1() {
    pointcut.setExpression("execution(* hel*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void nameMatchStar2() {
    pointcut.setExpression("execution(* *el*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void nameMatchFalse() {
    pointcut.setExpression("execution(* nono(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
  }

  @Test
  void packageExactMatch1() {
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void packageExactMatch2() {
    pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void packageExactMatchFalse() {
    pointcut.setExpression("execution(* hello.aop.*.*(..))");   // 딱 해당 패키지만 해당되기 때문에 False. (여기선 hello.aop)
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
  }

  @Test
  void packageMatchSubPackage1() {
    pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void packageMatchSubPackage2() {
    pointcut.setExpression("execution(* hello.aop..*.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void typeExactMatch() {
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void typeMatchSuperType() {
    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void typeMatchInternal() throws NoSuchMethodException {
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
    assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
  }

  /**
   * 부모 타입에 선언된 메서드만 가능
   * 구현체에 선언되어 있어도, 인터페이스에 선언되어 있지 않으면 매칭되지 않는다.
   * @throws NoSuchMethodException
   */
  @Test
  void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
    assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
  }

  @Test
  void argsMatch() {
    pointcut.setExpression("execution(* *(String))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  /**
   * 파라미터가 없어야함
   * ()
   */
  @Test
  void argsMatchNoArgs() {
    pointcut.setExpression("execution(* *())");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
  }

  /**
   * 정확히 하나의 파라미터 허용, 모든 타입 허용
   * (Xxx)
   */
  @Test
  void argsMatchStar() {
    pointcut.setExpression("execution(* *(*))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  /**
   * 파라미터 갯수와 무관하게 모든 파라미터, 모든 타입 허용
   * (), (Xxx), (Xxx, Xxx)
   */
  @Test
  void argsMatchAll() {
    pointcut.setExpression("execution(* *(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  /**
   * String 타입으로 시작, 파라미터 갯수와 무관하게 모든 파라미터, 모든 타입 허용
   * (), (Xxx), (Xxx, Xxx)
   */
  @Test
  void argsMatchComplex() {
    pointcut.setExpression("execution(* *(String, ..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

}
