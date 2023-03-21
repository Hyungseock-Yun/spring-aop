package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

  private CallServiceV1 callServiceV1;

  // setter를 이용하여 의존성 주입
  // (부트 2.6 이상부터는 순환참조를 기본적으로 금지하도록 변경됨. application.properties에 셋팅해야함)
  @Autowired
  public void setCallServiceV1(CallServiceV1 callServiceV1) {
    log.info("callServiceV1 setter={}", callServiceV1.getClass());
    this.callServiceV1 = callServiceV1;
  }

  // 자기 자신을 주입받기 때문에 순환참조 문제 발생
//  @Autowired
//  public CallServiceV1(CallServiceV1 callServiceV1) {
//    this.callServiceV1 = callServiceV1;
//  }

  public void external() {
    log.info("call external");
    callServiceV1.internal();   // 내부 메서드 호출(this.internal())
  }

  public void internal() {
    log.info("call internal");
  }
}
