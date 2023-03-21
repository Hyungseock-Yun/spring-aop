package hello.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 구조를 변경(분리)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV3 {

  private final InternalService internalService;

  public void external() {
    log.info("call external");
    internalService.internal();   // internal을 별도의 클래스로 분리하여 의존성을 주입받아 사용
  }

}
