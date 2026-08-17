package com.smartsheet.api.internal;

import com.smartsheet.api.models.DataClassificationSettings;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.internal.json.JacksonJsonSerializer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * Run manually against local SmarGate + DCS:
 *
 *   ./gradlew test --tests "com.smartsheet.api.internal.LocalIntegrationTest"
 *
 * Prerequisites:
 *   - DCS on localhost:8080
 *   - SmarGate on localhost:8081 (local-integration profile)
 */
@Disabled("Local integration test — remove @Disabled to run manually")
class LocalIntegrationTest {

    private static final long MASKED_PLAN_ID = 1148023251199876L; // raw 41878788

    @Test
    void getDataClassificationSettings_localStack() throws Exception {
        SmartsheetImpl smartsheet = new SmartsheetImpl(
                "http://localhost:8081/2.0/",
                "dummy",
                new DefaultHttpClient(),
                new JacksonJsonSerializer()
        );

        // Subclass in the same package to access the package-private createHeaders(),
        // and inject the internal headers that SmarGate's auth proxy normally sets.
        GovernanceResourcesImpl governance = new GovernanceResourcesImpl(smartsheet) {
            @Override
            Map<String, String> createHeaders() {
                Map<String, String> headers = super.createHeaders();
                headers.put("x-smar-sc-actor-org-id", "1001");
                headers.put("x-smar-sc-actor-id", "9999");
                headers.put("X-Client-DN", "CN=api.governance.a.dev.smar.cloud");
                return headers;
            }
        };

        DataClassificationSettings settings = governance.getDataClassificationSettings(MASKED_PLAN_ID);

        System.out.println("orgId:    " + settings.getOrgId());
        System.out.println("planId:   " + settings.getPlanId());
        System.out.println("disabled: " + settings.getIsDisabled());
        System.out.println("labels:   " + settings.getLabels().size());
        settings.getLabels().forEach(l ->
                System.out.printf("  [%d] %s  color=%s%n",
                        l.getSensitivityOrder(), l.getName(), l.getColor())
        );
        System.out.println("mode:     " + settings.getDowngradeApprovalSettings().getMode());
    }
}
