package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 5/06/16.
 */
public class KeyUtilsTest {

    @Test
    public void testValidateKeySet() throws Exception {
        ContractSummaryBean contractA = new ContractSummaryBean();
        ContractSummaryBean contractB = new ContractSummaryBean();
        ContractSummaryBean contractC = new ContractSummaryBean();
        //set apikey
        contractA.setApikey("apikey01");
        contractB.setApikey("apikey01");
        contractC.setApikey("apikey01");
        List<ContractSummaryBean> contractBeans = new ArrayList<>();
        contractBeans.add(contractA);
        contractBeans.add(contractB);
        contractBeans.add(contractC);

        //test expectation true
        assertTrue(KeyUtils.validateKeySet(contractBeans));

        contractBeans.remove(contractB);
        contractB.setApikey("apikey02");
        contractBeans.add(contractB);

        //test expectation false
        assertFalse(KeyUtils.validateKeySet(contractBeans));
    }
}