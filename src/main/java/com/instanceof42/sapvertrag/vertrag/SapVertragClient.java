package com.instanceof42.sapvertrag.vertrag;

import com.sap.conn.jco.JCoException;
import com.instanceof42.sapvertrag.config.SapDestinationProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class SapVertragClient {

  SapDestinationProvider sapDestinationProvider;

  public Optional<String> leseVertrag(String policennummer, String vertragsnummer)
      throws JCoException {

    var destination = sapDestinationProvider.getDestination();
    var function = destination.getRepository().getFunction("/PM0/ABT_CM_GET_CONTRACT_DATA");

    var filterStructure = function.getImportParameterList().getStructure("IS_READ_PM_KEY");
    filterStructure.setValue("POLICY_NR", policennummer);
    filterStructure.setValue("POLPR_NR", vertragsnummer);
    filterStructure.setValue("EFFECTIVE_DT", LocalDate.now().toString());
    function.getImportParameterList().setValue("IS_READ_PM_KEY", filterStructure);

    function.execute(destination);

    return Optional.ofNullable(function.getExportParameterList().toJSON());
  }
}
