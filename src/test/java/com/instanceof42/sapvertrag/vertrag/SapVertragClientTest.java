package com.instanceof42.sapvertrag.vertrag;

import com.instanceof42.sapvertrag.config.SapDestinationProvider;
import com.sap.conn.jco.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
class SapVertragClientTest {

    @MockBean
    SapDestinationProvider sapDestinationProvider;

    @Autowired
    private SapVertragClient client;
    private JCoParameterList exportParameterList;

    @BeforeEach
    void init() throws JCoException {
        exportParameterList = mock(JCoParameterList.class);

        var importParameterList = mock(JCoParameterList.class);
        when(importParameterList.getStructure("IS_READ_PM_KEY")).thenReturn(mock(JCoStructure.class));

        var jCoFunction = mock(JCoFunction.class);
        when(jCoFunction.getExportParameterList()).thenReturn(exportParameterList);
        when(jCoFunction.getImportParameterList()).thenReturn(importParameterList);

        var jCoRepository = mock(JCoRepository.class);
        when(jCoRepository.getFunction("/PM0/ABT_CM_GET_CONTRACT_DATA")).thenReturn(jCoFunction);

        var jCoDestination = mock(JCoDestination.class);
        when(jCoDestination.getRepository()).thenReturn(jCoRepository);

        when(sapDestinationProvider.getDestination()).thenReturn(jCoDestination);
    }

    @Test
    void police1000_sollVertragLiefern() throws JCoException {
        when(exportParameterList.toJSON()).thenReturn("{}");

        assertEquals(Optional.of("{}"), client.leseVertrag("1000", "1000"));
    }

    @Test
    void police2000_sollKeinErgebnis() throws JCoException {
        when(exportParameterList.toJSON()).thenReturn(null);

        assertEquals(Optional.empty(), client.leseVertrag("2000", "2000"));
    }

}