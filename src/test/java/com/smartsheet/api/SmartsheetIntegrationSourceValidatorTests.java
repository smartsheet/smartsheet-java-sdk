package com.smartsheet.api;

import com.smartsheet.api.internal.util.SmartsheetIntegrationSourceValidator;
import com.smartsheet.api.models.enums.SmartsheetIntegrationSourceType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SmartsheetIntegrationSourceValidatorTests {

    private static final String documentationLink = "https://developers.smartsheet.com/api/smartsheet/guides/basics/http-and-rest#http-headers";

    @Test
    void testBuildWithSmartsheetIntegrationSource_allThreeStrings_success() throws SmartsheetException {
        String smartsheetIntegrationSource = "AI,MyOrg,MyGPT";

        assertTrue(SmartsheetIntegrationSourceValidator.isValidFormat(smartsheetIntegrationSource));
    }

    @Test
    void testBuildWithSmartsheetIntegrationSource_twoStringsSeparatedByOneComma_fail() {
        String smartsheetIntegrationSource = "AI,MyGPT";

        Exception exception = assertThrows(SmartsheetException.class, () -> SmartsheetIntegrationSourceValidator.isValidFormat(smartsheetIntegrationSource));
        assertEquals("Invalid smartsheet integration source format. " +
                "Expected format: 'TYPE,ORGANIZATION,INTEGRATOR. " + documentationLink, exception.getMessage());
    }

    @Test
    void testBuildWithSmartsheetIntegrationSource_twoStringsSeparatedByTwoCommas_success() throws SmartsheetException {
        String smartsheetIntegrationSource = "AI,,MyGPT";

        assertTrue(SmartsheetIntegrationSourceValidator.isValidFormat(smartsheetIntegrationSource));
    }

    @Test
    void testBuildWithSmartsheetIntegrationSource_moreThanThreeStrings_fail() {
        String smartsheetIntegrationSource = "AI,MyOrg,MyGPT,MyDivision";

        Exception exception = assertThrows(SmartsheetException.class, () -> SmartsheetIntegrationSourceValidator.isValidFormat(smartsheetIntegrationSource));
        assertEquals("Invalid smartsheet integration source format. " +
                "Expected format: 'TYPE,ORGANIZATION,INTEGRATOR. " + documentationLink, exception.getMessage());
    }

    @Test
    void testBuildWithSmartsheetIntegrationSource_moreThanTwoCommas_fail() {
        String smartsheetIntegrationSource = "AI,MyOrg,,,MyGPT";

        Exception exception = assertThrows(SmartsheetException.class, () -> SmartsheetIntegrationSourceValidator.isValidFormat(smartsheetIntegrationSource));
        assertEquals("Invalid smartsheet integration source format. " +
                "Expected format: 'TYPE,ORGANIZATION,INTEGRATOR. " + documentationLink, exception.getMessage());
    }

    @Test
    void testBuildWithSmartsheetIntegrationSource_integratorTypeNotValidEnum_fail() {
        String smartsheetIntegrationSource = "MyInvalidIntegratorType,MyOrg,MyGPT";

        Exception exception = assertThrows(SmartsheetException.class, () -> SmartsheetIntegrationSourceValidator.isValidFormat(smartsheetIntegrationSource));
        assertEquals("Invalid smartsheet integration source format. " +
                "The integration type has to be one of the following: "
                + Arrays.toString(SmartsheetIntegrationSourceType.values())
                + ". Invalid integration type: " + "MyInvalidIntegratorType" + " " + documentationLink, exception.getMessage());
    }

    @Test
    void testBuildWithSmartsheetIntegrationSource_integratorNameMissing_fail() {
        String smartsheetIntegrationSource = "AI,MyOrg,";

        Exception exception = assertThrows(SmartsheetException.class, () -> SmartsheetIntegrationSourceValidator.isValidFormat(smartsheetIntegrationSource));
        assertEquals("Invalid smartsheet integration source format. " +
                "The integrator name cannot be empty.", exception.getMessage());
    }
}
