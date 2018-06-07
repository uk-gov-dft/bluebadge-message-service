package uk.gov.dft.bluebadge.client.message.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@Qualifier("messageServiceConfiguration")
public class ServiceConfiguration {

    @Value("${messageservice.servicehost.scheme}")
    private String scheme;
    @Value("${messageservice.servicehost.host}")
    private String host;
    @Value("${messageservice.servicehost.port}")
    private Integer port;
    @Value("${messageservice.servicehost.connectiontimeout}")
    private Integer connectionTimeout;
    @Value("${messageservice.servicehost.requesttimeout}")
    private Integer requestTimeout;
    @Value("${messageservice.servicehost.contextpath}")
    private String contextPath;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getUrlPrefix(){
        return UriComponentsBuilder.newInstance().scheme(scheme).host(host)
                .port(port).path(contextPath).build().toUriString();
    }
}
