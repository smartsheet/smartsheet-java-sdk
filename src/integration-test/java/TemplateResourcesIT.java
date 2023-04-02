/*
 * #[license]
 * Smartsheet Java SDK
 * %%
 * Copyright (C) 2023 Smartsheet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * %[license]
 */
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TemplateResourcesIT extends ITResourcesImpl{
    Smartsheet smartsheet;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testListPublicTemplates() throws IOException, SmartsheetException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Template> templates = smartsheet.templateResources().listPublicTemplates(null);

        assertNotNull(templates);
    }

    @Test
    void testListTemplates() throws IOException, SmartsheetException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Template> templates = smartsheet.templateResources().listUserCreatedTemplates(parameters);

        assertNotNull(templates);
    }
}
