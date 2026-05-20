package twitter.challenge.espenia.infra.util;


import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class RequestContextHolderHelper {

  private RequestContextHolderHelper() {}

  public static void mock() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setCharacterEncoding("UTF_8");
    ServletRequestAttributes webRequest = new ServletRequestAttributes(request);
    RequestContextHolder.setRequestAttributes(webRequest);
  }
}
