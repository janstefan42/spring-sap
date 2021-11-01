package com.instanceof42.sapvertrag.config;

import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Properties;

/**
 * Der benutzerdefinierte DestinationDataProvider implementiert DestinationDataProvider und bietet
 * eine Implementierung für mindestens getDestinationProperties (String). Wann immer möglich, sollte
 * die Implementierung Ereignisse unterstützen und die JCo-Laufzeit benachrichtigen, wenn ein Ziel
 * erstellt, geändert oder gelöscht wird. Andernfalls überprüft die JCo-Laufzeit regelmäßig, ob eine
 * zwischengespeicherte Zielkonfiguration noch gültig ist, was zu einer Leistungsbeeinträchtigung
 * führt.
 */
@Slf4j
class InMemoryDestinationDataProvider implements DestinationDataProvider {
  private DestinationDataEventListener eventListener;
  private final HashMap<String, Properties> secureDBStorage = new HashMap<>();

  @Override
  public Properties getDestinationProperties(String destinationName) {
    try {
      var properties = secureDBStorage.get(destinationName);

      if (properties != null && properties.isEmpty())
        throw new DataProviderException(
            DataProviderException.Reason.INVALID_CONFIGURATION,
            "Destination Konfiguration ist nicht korrekt.",
            null);

      return properties;
    } catch (RuntimeException e) {
      throw new DataProviderException(DataProviderException.Reason.INTERNAL_ERROR, e);
    }
  }

  // Eine Implementierung, die Ereignisse unterstützt, muss die von der JCo-Laufzeit bereitgestellte
  // eventListener-Instanz beibehalten. Diese Listener-Instanz soll verwendet werden, um die
  // JCo-Laufzeit über alle Änderungen in den Zielkonfigurationen zu informieren.
  @Override
  public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
    this.eventListener = eventListener;
  }

  @Override
  public boolean supportsEvents() {
    return true;
  }

  /** Implementierung, die die Properties im Speicher ablegt */
  void changeProperties(String destName, Properties properties) {
    if (eventListener == null) {
      log.error("Die Verbindung zu SAP kann nicht hergestellt werden.");
      return;
    }
    synchronized (secureDBStorage) {
      if (properties == null) {
        if (secureDBStorage.remove(destName) != null) eventListener.deleted(destName);
      } else {
        secureDBStorage.put(destName, properties);
        eventListener.updated(destName);
      }
    }
  }
}
