
package com.google.gct.idea.debugger;

import static org.mockito.Mockito.when;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.services.clouddebugger.Clouddebugger.Debugger;
import com.google.gct.idea.CloudToolsPluginInfoService;
import com.google.gct.idea.testing.BasePluginTestCase;
import com.google.gct.login.CredentialedUser;
import com.google.gct.login.GoogleLogin;
import com.google.gdt.eclipse.login.common.GoogleLoginState;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.LinkedHashMap;

@RunWith(MockitoJUnitRunner.class)
public class CloudDebuggerClientTest extends BasePluginTestCase {

  @Mock
  CloudToolsPluginInfoService mockInfoService;

  @Before
  public void setUp() {
    GoogleLogin mockLogin = Mockito.mock(GoogleLogin.class);
    GoogleLogin.setInstance(mockLogin);
    registerService(CloudToolsPluginInfoService.class, mockInfoService);
    LinkedHashMap<String, CredentialedUser> allUsers = new LinkedHashMap<String, CredentialedUser>();
    CredentialedUser user = Mockito.mock(CredentialedUser.class);
    allUsers.put("foo@example.com", user);
    when(mockLogin.getAllUsers()).thenReturn(allUsers);
    Credential credential = Mockito.mock(Credential.class);
    when(user.getCredential()).thenReturn(credential);
    GoogleLoginState loginState = Mockito.mock(GoogleLoginState.class);
    when(user.getGoogleLoginState()).thenReturn(loginState);
  }

  @After
  public void unsetLogin() {
    GoogleLogin.setInstance(null);
  }

  @Test
  public void testUserAgent() throws IOException {
    when(mockInfoService.getUserAgent()).thenReturn("userAgent");
    Debugger client = CloudDebuggerClient.getLongTimeoutClient("foo@example.com");
    HttpRequest httpRequest = client.debuggees().list().buildHttpRequestUsingHead();
    HttpHeaders headers = httpRequest.getHeaders();
    String userAgent = headers.getUserAgent();
    Assert.assertTrue(userAgent.startsWith("userAgent"));
    Assert.assertTrue(userAgent.endsWith("Google-API-Java-Client"));
  }

  @Test
  public void testGetShortTimeoutClient_fromUserEmail() {
    Debugger client = CloudDebuggerClient.getShortTimeoutClient("foo@example.com");
    Assert.assertNotNull(client.debuggees().breakpoints());
  }

  @Test
  public void testGetLongTimeoutClient_fromNullUserEmail() {
    Assert.assertNull(CloudDebuggerClient.getLongTimeoutClient((String) null));
  }

  @Test
  public void testGetShortTimeoutClient_fromNullUserEmail() {
    Assert.assertNull(CloudDebuggerClient.getShortTimeoutClient((String) null));
  }

}