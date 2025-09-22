package com.smartsheet.api.internal.util;

import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.enums.SmartsheetIntegrationSourceType;

import java.util.Arrays;

public class SmartsheetIntegrationSourceValidator {

    private static final String documentationLink = "https://developers.smartsheet.com/api/smartsheet/guides/basics/http-and-rest#http-headers";

    /**
     * Validates a smartsheet integration source string in the format:
     *   type, organisation name, integrator name
     *
     * - type: must be one of the enum values
     * - organisation name: optional (can be empty)
     * - integrator name: non-empty
     */
    public static boolean isValidFormat(String input) throws SmartsheetException {
        if (input == null) {
            throw new SmartsheetException("Smartsheet integration source cannot be null");
        }

        String[] parts = input.split(",", -1); // -1 keeps empty slots
        if (parts.length != 3) {
            throw new SmartsheetException("Invalid smartsheet integration source format. " +
                    "Expected format: 'TYPE,ORGANIZATION,INTEGRATOR. " + documentationLink);
        }

        String integrationType = parts[0];
        String integratorName = parts[2];

        // The First slot (integration type) must match enum
        if (!isValidType(integrationType)) {
            throw new SmartsheetException("Invalid smartsheet integration source format. " +
                    "The integration type has to be one of the following: "
                    + Arrays.toString(SmartsheetIntegrationSourceType.values())
                    + ". Invalid integration type: " + integrationType + " " + documentationLink);
        }

        // Integrator name must be non-empty
        if (integratorName.isEmpty()) {
            throw new SmartsheetException("Invalid smartsheet integration source format. " +
                    "The integrator name cannot be empty.");
        }

        return true;
    }

    /**
     * Checks if the integration type matches one of the enum values
     */
    private static boolean isValidType(String integrationTypeValue) {
        if (integrationTypeValue == null || integrationTypeValue.isEmpty()) {
            return false;
        }
        for (SmartsheetIntegrationSourceType sourceType : SmartsheetIntegrationSourceType.values()) {
            if (sourceType.name().equalsIgnoreCase(integrationTypeValue)) {
                return true;
            }
        }
        return false;
    }
}
