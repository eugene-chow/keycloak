package org.keycloak.adapters.undertow;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import org.keycloak.adapters.HttpFacade;
import org.keycloak.util.KeycloakUriBuilder;

import javax.security.cert.X509Certificate;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class UndertowHttpFacade implements HttpFacade {
    protected HttpServerExchange exchange;
    protected RequestFacade requestFacade = new RequestFacade();
    protected ResponseFacade responseFacade = new ResponseFacade();

    public UndertowHttpFacade(HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public Request getRequest() {
        return requestFacade;
    }

    @Override
    public Response getResponse() {
        return responseFacade;
    }

    @Override
    public X509Certificate[] getCertificateChain() {
        X509Certificate[] chain = new X509Certificate[0];
        try {
            chain = exchange.getConnection().getSslSessionInfo().getPeerCertificateChain();
        } catch (Exception ignore) {

        }
        return chain;
    }

    protected class RequestFacade implements Request {
        @Override
        public String getURI() {
            KeycloakUriBuilder uriBuilder = KeycloakUriBuilder.fromUri(exchange.getRequestURI())
                    .replaceQuery(exchange.getQueryString());
            if (!exchange.isHostIncludedInRequestURI()) uriBuilder.scheme(exchange.getRequestScheme()).host(exchange.getHostAndPort());
            return uriBuilder.build().toString();
        }

        @Override
        public boolean isSecure() {
            String protocol = exchange.getRequestScheme();
            return protocol.equalsIgnoreCase("https");
        }

        @Override
        public String getFirstParam(String param) {
            throw new RuntimeException("Not implemented yet");
        }

        @Override
        public String getQueryParamValue(String param) {
            Map<String,Deque<String>> queryParameters = exchange.getQueryParameters();
            if (queryParameters == null) return null;
            Deque<String> strings = queryParameters.get(param);
            if (strings == null) return null;
            return strings.getFirst();
        }

        @Override
        public Cookie getCookie(String cookieName) {
            Map<String, io.undertow.server.handlers.Cookie> requestCookies = exchange.getRequestCookies();
            if (requestCookies == null) return null;
            io.undertow.server.handlers.Cookie cookie = requestCookies.get(cookieName);
            if (cookie == null) return null;
            return new Cookie(cookie.getName(), cookie.getValue(), cookie.getVersion(), cookie.getDomain(), cookie.getPath());
        }

        @Override
        public List<String> getHeaders(String name) {
            return exchange.getRequestHeaders().get(name);
        }

        @Override
        public String getMethod() {
            return exchange.getRequestMethod().toString();
        }



        @Override
        public String getHeader(String name) {
            return exchange.getRequestHeaders().getFirst(name);
        }

        @Override
        public InputStream getInputStream() {
            if (!exchange.isBlocking()) exchange.startBlocking();
            return exchange.getInputStream();
        }

        @Override
        public String getRemoteAddr() {
            InetSocketAddress sourceAddress = exchange.getSourceAddress();
            if (sourceAddress == null) {
                return "";
            }
            InetAddress address = sourceAddress.getAddress();
            if (address == null) {
                // this is unresolved, so we just return the host name not exactly spec, but if the name should be
                // resolved then a PeerNameResolvingHandler should be used and this is probably better than just
                // returning null
                return sourceAddress.getHostString();
            }
            return address.getHostAddress();
        }
    }

    protected class ResponseFacade implements Response {
        @Override
        public void setStatus(int status) {
            exchange.setResponseCode(status);
        }

        @Override
        public void addHeader(String name, String value) {
            exchange.getResponseHeaders().add(new HttpString(name), value);
        }

        @Override
        public void setHeader(String name, String value) {
            exchange.getResponseHeaders().put(new HttpString(name), value);
        }

        @Override
        public void resetCookie(String name, String path) {
            CookieImpl cookie = new CookieImpl(name, "");
            cookie.setMaxAge(0);
            cookie.setPath(path);
            exchange.setResponseCookie(cookie);
        }

        @Override
        public void setCookie(String name, String value, String path, String domain, int maxAge, boolean secure, boolean httpOnly) {
            CookieImpl cookie = new CookieImpl(name, value);
            cookie.setPath(path);
            cookie.setDomain(domain);
            cookie.setMaxAge(maxAge);
            cookie.setSecure(secure);
            cookie.setHttpOnly(httpOnly);
            exchange.setResponseCookie(cookie);
        }

        @Override
        public OutputStream getOutputStream() {
            if (!exchange.isBlocking()) exchange.startBlocking();
            return exchange.getOutputStream();
        }

        @Override
        public void sendError(int code, String message) {
            exchange.setResponseCode(code);
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
            try {
                exchange.getOutputStream().write(message.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            exchange.endExchange();
        }


        @Override
        public void end() {
            exchange.endExchange();
        }
    }
}
