package com.instanceof42.sapvertrag.vertrag;

import com.sap.conn.jco.JCoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin()
@RequiredArgsConstructor
@RequestMapping(path = "/api/vertrag", produces = "application/json")
@Slf4j
public class VertragController {

  private final SapVertragClient client;

  @GetMapping("/{policennummer}/{vertragsnummer}")
  public ResponseEntity<String> one(
      @PathVariable String policennummer, @PathVariable String vertragsnummer) throws JCoException {

    var vertrag = client.leseVertrag(policennummer, vertragsnummer);
    return vertrag.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
  }
}
