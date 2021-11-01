package com.instanceof42.sapvertrag.config;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.Environment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Jede Anwendung, die Java Connector 3 verwendet, behandelt Ziele. Ein Ziel stellt eine logische
 * Adresse eines ABAP-Systems dar und trennt somit die Zielkonfiguration von der Anwendungslogik.
 * JCo ruft die zur Laufzeit erforderlichen Zielparameter aus dem in der Laufzeitumgebung
 * registrierten DestinationDataProvider ab. Wenn kein Anbieter registriert ist, verwendet JCo seine
 * Standardimplementierung, die die Konfiguration aus einer Eigenschaftendatei liest. Es wird
 * erwartet, dass jede Umgebung eine geeignete Implementierung bietet, die Sicherheits- und andere
 * Anforderungen erfüllt. Darüber hinaus kann pro Prozess nur ein einziger DestinationDataProvider
 * registriert werden. Der Grund für diese Entwurfsentscheidung ist folgender: Die
 * Anbieterimplementierungen sind Teil der Umgebungsinfrastruktur. Die Implementierung darf nicht
 * anwendungsspezifisch sein und muss insbesondere von der Anwendungslogik getrennt sein.
 *
 * <p>Dieses Beispiel zeigt eine einfache Implementierung der DestinationDataProvider-Schnittstelle
 * und zeigt, wie diese registriert wird. Eine Implementierung in der realen Welt sollte die
 * Konfigurationsdaten auf sichere Weise speichern (hier werden sie der Einfachheit halber im
 * Speicher gespeichert).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SapDestinationProvider {

  private static final String DESTINATION_NAME = "Test-SAP";

  private final SapConfigurationProperties configurationProperties;

  @PostConstruct
  public void init() {
    var memoryProvider = new InMemoryDestinationDataProvider();

    // Registriert den Provider in der JCo-Umgebung.
    // Fängt IllegalStateException, wenn eine Instanz bereits registriert ist.
    try {
      Environment.registerDestinationDataProvider(memoryProvider);
    } catch (IllegalStateException providerAlreadyRegisteredException) {
      log.warn("Die Implementierung ist bereits registriert.", providerAlreadyRegisteredException);
    }

    memoryProvider.changeProperties(DESTINATION_NAME, configurationProperties.getProperties());
  }

  public JCoDestination getDestination() throws JCoException {
    return JCoDestinationManager.getDestination(DESTINATION_NAME);
  }
}
