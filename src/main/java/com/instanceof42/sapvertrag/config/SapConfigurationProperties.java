package com.instanceof42.sapvertrag.config;

import com.sap.conn.jco.ext.DestinationDataProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "jco.client")
public class SapConfigurationProperties {

  private final Properties properties = new Properties();

  public void setAshost(String ashost) {
    this.properties.setProperty(DestinationDataProvider.JCO_ASHOST, ashost);
  }

  public void setSysnr(String sysnr) {
    this.properties.setProperty(DestinationDataProvider.JCO_SYSNR, sysnr);
  }

  public void setClient(String client) {
    this.properties.setProperty(DestinationDataProvider.JCO_CLIENT, client);
  }

  public void setUser(String user) {
    this.properties.setProperty(DestinationDataProvider.JCO_USER, user);
  }

  public void setPasswd(String passwd) {
    this.properties.setProperty(DestinationDataProvider.JCO_PASSWD, passwd);
  }

  public void setLang(String lang) {
    this.properties.setProperty(DestinationDataProvider.JCO_LANG, lang);
  }

  public Properties getProperties() {
    return properties;
  }
}
