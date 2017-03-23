package com.t1t.apim.facades;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Created by michallispashidis on 5/09/15.
 */
public class UserFacadeTokenTest {
    private static final Logger log = LoggerFactory.getLogger(UserFacadeTokenTest.class.getName());
    @Test
    public void testUserFromSAML2BearerToken() throws Exception {
        String exampleBearerToken = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHNhbWwycDpSZXNwb25zZSBEZXN0aW5hdGlvbj0iaHR0cDovL2xvY2FsaG9zdDo4MDgwL0FQSS1FbmdpbmUtd2ViL3YxL3VzZXJzL2lkcC9jYWxsYmFjayIgSUQ9Im5kYWFwZmdlY25jaGdkamtqaXBwZWFhZGdjbmNnZm9pYWRhZWRuYmwiIEluUmVzcG9uc2VUbz0iMCIgSXNzdWVJbnN0YW50PSIyMDE1LTA5LTA1VDE1OjIwOjIxLjE1NloiIFZlcnNpb249IjIuMCIgeG1sbnM6c2FtbDJwPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6cHJvdG9jb2wiPjxzYW1sMjpJc3N1ZXIgRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6bmFtZWlkLWZvcm1hdDplbnRpdHkiIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj5sb2NhbGhvc3Q8L3NhbWwyOklzc3Vlcj48c2FtbDJwOlN0YXR1cz48c2FtbDJwOlN0YXR1c0NvZGUgVmFsdWU9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpzdGF0dXM6U3VjY2VzcyIvPjwvc2FtbDJwOlN0YXR1cz48c2FtbDI6QXNzZXJ0aW9uIElEPSJiaG1oaGNtZmpwZ2JqcGtvYmpwY2JjcHBsbGNsbm9wYm9pYmxrYmpnIiBJc3N1ZUluc3RhbnQ9IjIwMTUtMDktMDVUMTU6MjA6MjEuMTU2WiIgVmVyc2lvbj0iMi4wIiB4bWxuczpzYW1sMj0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiI%2BPHNhbWwyOklzc3VlciBGb3JtYXQ9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpuYW1laWQtZm9ybWF0OmVudGl0eSI%2BbG9jYWxob3N0PC9zYW1sMjpJc3N1ZXI%2BPHNhbWwyOlN1YmplY3Q%2BPHNhbWwyOk5hbWVJRCBGb3JtYXQ9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpuYW1laWQtZm9ybWF0OmVudGl0eSI%2BYWRtaW48L3NhbWwyOk5hbWVJRD48c2FtbDI6U3ViamVjdENvbmZpcm1hdGlvbiBNZXRob2Q9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpjbTpiZWFyZXIiPjxzYW1sMjpTdWJqZWN0Q29uZmlybWF0aW9uRGF0YSBJblJlc3BvbnNlVG89IjAiIE5vdE9uT3JBZnRlcj0iMjAxNS0wOS0wNVQxNToyNToyMS4xNTZaIiBSZWNpcGllbnQ9Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9BUEktRW5naW5lLXdlYi92MS91c2Vycy9pZHAvY2FsbGJhY2siLz48L3NhbWwyOlN1YmplY3RDb25maXJtYXRpb24%2BPC9zYW1sMjpTdWJqZWN0PjxzYW1sMjpDb25kaXRpb25zIE5vdEJlZm9yZT0iMjAxNS0wOS0wNVQxNToyMDoyMS4xNTZaIiBOb3RPbk9yQWZ0ZXI9IjIwMTUtMDktMDVUMTU6MjU6MjEuMTU2WiI%2BPHNhbWwyOkF1ZGllbmNlUmVzdHJpY3Rpb24%2BPHNhbWwyOkF1ZGllbmNlPmFwaW1hcmtldDwvc2FtbDI6QXVkaWVuY2U%2BPC9zYW1sMjpBdWRpZW5jZVJlc3RyaWN0aW9uPjwvc2FtbDI6Q29uZGl0aW9ucz48c2FtbDI6QXV0aG5TdGF0ZW1lbnQgQXV0aG5JbnN0YW50PSIyMDE1LTA5LTA1VDE1OjIwOjIxLjE1NloiPjxzYW1sMjpBdXRobkNvbnRleHQ%2BPHNhbWwyOkF1dGhuQ29udGV4dENsYXNzUmVmPnVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphYzpjbGFzc2VzOlBhc3N3b3JkPC9zYW1sMjpBdXRobkNvbnRleHRDbGFzc1JlZj48L3NhbWwyOkF1dGhuQ29udGV4dD48L3NhbWwyOkF1dGhuU3RhdGVtZW50Pjwvc2FtbDI6QXNzZXJ0aW9uPjwvc2FtbDJwOlJlc3BvbnNlPg%3D%3D";
        UserFacade userFacade = new UserFacade();
        String user = userFacade.userFromSAML2BearerToken(exampleBearerToken);
        log.info("User in bearer token:{}",user);
        assertEquals(user,"admin");
    }
}