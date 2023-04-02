package com.smartsheet.api.internal;

/*
 * #[license]
 * Smartsheet SDK for Java
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


import com.smartsheet.api.ContactResources;
import com.smartsheet.api.EventResources;
import com.smartsheet.api.FavoriteResources;
import com.smartsheet.api.FolderResources;
import com.smartsheet.api.GroupResources;
import com.smartsheet.api.HomeResources;
import com.smartsheet.api.ImageUrlResources;
import com.smartsheet.api.PassthroughResources;
import com.smartsheet.api.ReportResources;
import com.smartsheet.api.SearchResources;
import com.smartsheet.api.ServerInfoResources;
import com.smartsheet.api.SheetResources;
import com.smartsheet.api.SightResources;
import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.TemplateResources;
import com.smartsheet.api.TokenResources;
import com.smartsheet.api.Trace;
import com.smartsheet.api.UserResources;
import com.smartsheet.api.WebhookResources;
import com.smartsheet.api.WorkspaceResources;
import com.smartsheet.api.internal.http.AndroidHttpClient;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.internal.http.HttpClient;
import com.smartsheet.api.internal.json.JacksonJsonSerializer;
import com.smartsheet.api.internal.json.JsonSerializer;
import com.smartsheet.api.internal.util.Util;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is the implementation of Smartsheet interface.
 *
 * Thread Safety: This class is thread safe because all its mutable fields are safe-guarded using AtomicReference to
 * ensure atomic modifications, and also the underlying HttpClient and JsonSerializer interfaces are thread safe.
 */
public class SmartsheetImpl implements Smartsheet {

    /**
     * Represents the base URI of the Smartsheet REST API.
     *
     * It will be initialized in constructor and will not change afterwards.
     */
    private URI baseURI;

    /**
     * Represents the AtomicReference for access token.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and can be set via corresponding setter, therefore effectively the access token can be updated in the
     * SmartsheetImpl in thread safe manner.
     */
    private final AtomicReference<String> accessToken;

    /**
     * Represents the HttpClient.
     *
     * It will be initialized in constructor and will not change afterwards.
     */
    private final HttpClient httpClient;

    /**
     * Represents the JsonSerializer.
     *
     * It will be initialized in constructor and will not change afterwards.
     */
    private JsonSerializer jsonSerializer;

    /**
     * Represents the AtomicReference for assumed user email.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and can be set via corresponding setter, therefore effectively the assumed user can be updated in the
     * SmartsheetImpl in thread safe manner.
     */
    private final AtomicReference<String> assumedUser;

    /**
     * Represents the AtomicReference for change agent
     *
     * It will be initialized in constructor and will not change afterwards.
     *
     */
    private final AtomicReference<String> changeAgent;

    /**
     * Represents the AtomicReference for the user agent
     */
    private final AtomicReference<String> userAgent;

    /**
     * Represents the AtomicReference to HomeResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private AtomicReference<HomeResources> home;

    /**
     * Represents the AtomicReference to WorkspaceResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private AtomicReference<WorkspaceResources> workspaces;

    /**
     * Represents the AtomicReference to FolderResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private AtomicReference<FolderResources> folders;

    /**
     * Represents the AtomicReference to TemplateResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private AtomicReference<TemplateResources> templates;

    /**
     * Represents the AtomicReference to SheetResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private AtomicReference<SheetResources> sheets;

    /**
     * Represents the AtomicReference to SightResources
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private AtomicReference<SightResources> sights;

    /**
     * Represents the AtomicReference to UserResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private AtomicReference<UserResources> users;

    /**
     * Represents the AtomicReference to {@link GroupResources}.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private AtomicReference<GroupResources> groups;

    /**
     * Represents the AtomicReference to SearchResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private AtomicReference<SearchResources> search;

    /**
     * Represents the AtomicReference to ReportResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private AtomicReference<ReportResources> reports;

    /**
     * Represents the AtomicReference for ServerInfoResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private final AtomicReference<ServerInfoResources> serverInfo;

    /**
     * Represents the AtomicReference for FavoriteResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private final AtomicReference<FavoriteResources> favorites;

    /**
     * Represents the AtomicReference for TokenResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private final AtomicReference<TokenResources> tokens;

    /**
     * Represents the AtomicReference for ContactResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private final AtomicReference<ContactResources> contacts;

    /**
     * Represents the AtomicReference for ImageUrlResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private final AtomicReference<ImageUrlResources> imageUrls;

    /**
     * Represents the AtomicReference for WebhookResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private final AtomicReference<WebhookResources> webhooks;

    /**
     * Represents the AtomicReference for PassthroughResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private final AtomicReference<PassthroughResources> passthrough;

    /**
     * Represents the AtomicReference for EventResources.
     *
     * It will be initialized in constructor and will not change afterwards. The underlying value will be initially set
     * as null, and will be initialized to non-null at the first time it is accessed via corresponding getter, therefore
     * effectively the underlying value is lazily created in a thread safe manner.
     */
    private final AtomicReference<EventResources> events;

    /**
     * Create an instance with given server URI, HttpClient (optional) and JsonSerializer (optional)
     *
     * Exceptions: - IllegalArgumentException : if serverURI/version/accessToken is null/empty
     *
     * @param baseURI the server uri
     * @param accessToken the access token
     */
    public SmartsheetImpl(String baseURI, String accessToken) {
        this(baseURI, accessToken, null, null);
    }

    /**
     * Create an instance with given server URI, HttpClient (optional) and JsonSerializer (optional)
     *
     * Exceptions: - IllegalArgumentException : if serverURI/version/accessToken is null/empty
     *
     * @param baseURI the server uri
     * @param accessToken the access token
     * @param httpClient the http client (optional)
     * @param jsonSerializer the json serializer (optional)
     */
    public SmartsheetImpl(String baseURI, String accessToken, HttpClient httpClient, JsonSerializer jsonSerializer) {
        Util.throwIfNull(baseURI);
        Util.throwIfEmpty(baseURI);

        this.baseURI = URI.create(baseURI);
        this.accessToken = new AtomicReference<>(accessToken);
        this.jsonSerializer = ((jsonSerializer == null) ? new JacksonJsonSerializer() : jsonSerializer);
        this.httpClient = ((httpClient == null) ?
                new DefaultHttpClient(HttpClients.createDefault(), this.jsonSerializer) :  httpClient);
        this.assumedUser = new AtomicReference<>(null);
        this.changeAgent = new AtomicReference<>(null);
        this.userAgent = new AtomicReference<>(generateUserAgent(null));

        // Initialize resources
        this.home = new AtomicReference<>();
        this.workspaces = new AtomicReference<>();
        this.folders = new AtomicReference<>();
        this.templates = new AtomicReference<>();
        this.sheets = new AtomicReference<>();
        this.sights = new AtomicReference<>();
        this.favorites = new AtomicReference<>();
        this.users = new AtomicReference<>();
        this.groups = new AtomicReference<>();
        this.search = new AtomicReference<>();
        this.reports = new AtomicReference<>();
        this.serverInfo = new AtomicReference<>();
        this.tokens = new AtomicReference<>();
        this.contacts = new AtomicReference<>();
        this.imageUrls = new AtomicReference<>();
        this.webhooks = new AtomicReference<>();
        this.passthrough = new AtomicReference<>();
        this.events = new AtomicReference<>();
    }

    /**
     * Finalize the object, this method is overridden to close the HttpClient.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected void finalize() throws IOException {
        this.httpClient.close();
    }

    /**
     * Getter of corresponding field.
     *
     * Returns: corresponding field.
     *
     * @return the base uri
     */
    URI getBaseURI() {
        return baseURI;
    }

    /**
     * Return the access token
     *
     * @return the access token
     */
    String getAccessToken() {
        return accessToken.get();
    }

    /**
     * Set the access token to use.
     *
     * Parameters: - accessToken : the access token
     *
     * Returns: None
     *
     *
     * @param accessToken the new access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken.set(accessToken);
    }

    /**
     * Getter of corresponding field.
     *
     * @return corresponding field
     */
    JsonSerializer getJsonSerializer() {
        return jsonSerializer;
    }

    /**
     * Getter of corresponding field.
     *
     * @return corresponding field.
     */
    HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Return the assumed user.
     *
     * @return the assumed user
     */
    String getAssumedUser() {
        return assumedUser.get();
    }

    /**
     * Set the email of the user to assume. Null/empty string indicates no user is assumed.
     *
     * @param assumedUser the email of the user to assume
     */
    public void setAssumedUser(String assumedUser) {
        this.assumedUser.set(assumedUser);
    }

    /**
     * Return the change agent identifier.
     *
     * @return the access token
     */
    String getChangeAgent() {
        return changeAgent.get();
    }

    /**
     * Sets the change agent identifier
     *
     * @param changeAgent
     */
    public void setChangeAgent(String changeAgent) {
        this.changeAgent.set(changeAgent);
    }

    /**
     * Return the user agent string
     *
     * @return the user agent string
     */
    public String getUserAgent() {
        return userAgent.get();
    }

    /**
     * Sets the user agent string
     *
     * @param userAgent the user agent string
     */
    public void setUserAgent(String userAgent) {
        this.userAgent.set(generateUserAgent(userAgent));
    }

    /**
     * Sets the max retry time if the HttpClient is an instance of DefaultHttpClient
     *
     * @param maxRetryTimeMillis max retry time
     */
    public void setMaxRetryTimeMillis(long maxRetryTimeMillis) {
        if (this.httpClient instanceof DefaultHttpClient) {
            ((DefaultHttpClient) this.httpClient).setMaxRetryTimeMillis(maxRetryTimeMillis);
        }
        else if (this.httpClient instanceof AndroidHttpClient) {
            ((AndroidHttpClient) this.httpClient).setMaxRetryTimeMillis(maxRetryTimeMillis);
        }
        else
            throw new UnsupportedOperationException("Invalid operation for class " + this.httpClient.getClass());
    }

    /** set what request/response fields to log in trace-logging */
    public void setTraces(Trace... traces) {
        if (this.httpClient instanceof DefaultHttpClient) {
            ((DefaultHttpClient)this.httpClient).setTraces(traces);
        }
        else
            throw new UnsupportedOperationException("Invalid operation for class " + this.httpClient.getClass());
    }

    /** set whether or not to generate "pretty formatted" JSON in trace-logging */
    public void setTracePrettyPrint(boolean pretty) {
        if (this.httpClient instanceof DefaultHttpClient) {
            ((DefaultHttpClient)this.httpClient).setTracePrettyPrint(pretty);
        }
        else
            throw new UnsupportedOperationException("Invalid operation for class " + this.httpClient.getClass());
    }

    /**
     * Returns the HomeResources instance that provides access to Home resources.
     *
     * @return the home resources
     */
    public HomeResources homeResources() {
        if (home.get() == null) {
            home.compareAndSet(null, new HomeResourcesImpl(this));
        }
        return home.get();
    }

    /**
     * Returns the WorkspaceResources instance that provides access to Workspace resources.
     *
     * @return the workspace resources
     */
    public WorkspaceResources workspaceResources() {
        if (workspaces.get() == null) {
            workspaces.compareAndSet(null, new WorkspaceResourcesImpl(this));
        }
        return workspaces.get();
    }

    /**
     * Returns the FolderResources instance that provides access to Folder resources.
     *
     * @return the folder resources
     */
    public FolderResources folderResources() {
        if (folders.get() == null) {
            folders.compareAndSet(null, new FolderResourcesImpl(this));
        }
        return folders.get();
    }

    /**
     * Returns the TemplateResources instance that provides access to Template resources.
     *
     * @return the template resources
     */
    public TemplateResources templateResources() {
        if (templates.get() == null) {
            templates.compareAndSet(null, new TemplateResourcesImpl(this));
        }
        return templates.get();
    }

    /**
     * Returns the SheetResources instance that provides access to Sheet resources.
     *
     * @return the sheet resources
     */
    public SheetResources sheetResources() {
        if (sheets.get() == null) {
            sheets.compareAndSet(null, new SheetResourcesImpl(this));
        }
        return sheets.get();
    }

    /**
     * Returns the SightResources instance that provides access to Sight resources.
     *
     * @return the sight resources
     */
    public SightResources sightResources() {
        if (sights.get() == null) {
            sights.compareAndSet(null, new SightResourcesImpl(this));
        }
        return sights.get();
    }
    /**
     * Returns the FavoriteResources instance that provides access to Favorite resources.
     *
     * @return the favorite resources
     */
    public FavoriteResources favoriteResources() {
        if (favorites.get() == null) {
            favorites.compareAndSet(null, new FavoriteResourcesImpl(this));
        }
        return favorites.get();
    }

    /**
     * Returns the {@link UserResources} instance that provides access to User resources.
     *
     * @return the user resources
     */
    public UserResources userResources() {
        if (users.get() == null) {
            users.compareAndSet(null, new UserResourcesImpl(this));
        }
        return users.get();
    }

    /**
     * Returns the {@link GroupResources} instance that provides access to User resources.
     *
     * @return the user resources
     */
    public GroupResources groupResources() {
        if (groups.get() == null) {
            groups.compareAndSet(null, new GroupResourcesImpl(this));
        }
        return groups.get();
    }

    /**
     * Returns the {@link SearchResources} instance that provides access to searching resources.
     *
     * @return the search resources
     */
    public SearchResources searchResources() {
        if (search.get() == null) {
            search.compareAndSet(null, new SearchResourcesImpl(this));
        }
        return search.get();
    }

    /**
     * Returns the {@link ReportResources} instance that provides access to Report resources.
     *
     * @return the report resources
     */
    public ReportResources reportResources() {
        if (reports.get() == null) {
            reports.compareAndSet(null, new ReportResourcesImpl(this));
        }
        return reports.get();
    }

    /**
     * Returns the {@link ServerInfoResources} instance that provides access to ServerInfo resources.
     *
     * @return the ServerInfo resources
     */
    public ServerInfoResources serverInfoResources() {
        if (serverInfo.get() == null) {
            serverInfo.compareAndSet(null, new ServerInfoResourcesImpl(this));
        }
        return serverInfo.get();
    }

    /**
     * Returns the TokenResources instance that provides access to token resources.
     *
     * @return the token resources
     */
    public TokenResources tokenResources() {
        if (tokens.get() == null) {
            tokens.compareAndSet(null, new TokenResourcesImpl(this));
        }
        return tokens.get();
    }

    /**
     * Returns the ContactResources instance that provides access to contact resources.
     *
     * @return the contact resources
     */
    public ContactResources contactResources() {
        if (contacts.get() == null) {
            contacts.compareAndSet(null, new ContactResourcesImpl(this));
        }
        return contacts.get();
    }

    /**
     * Returns the ImageUrlResources instance that provides access to image url resources.
     *
     * @return the image url resources
     */
    public ImageUrlResources imageUrlResources() {
        if (imageUrls.get() == null) {
            imageUrls.compareAndSet(null, new ImageUrlResourcesImpl(this));
        }
        return imageUrls.get();
    }

    /**
     * Returns the WebhookResources instance that provides access to webhook resources.
     *
     * @return the webhook resources
     */
    public WebhookResources webhookResources() {
        if (webhooks.get() == null) {
            webhooks.compareAndSet(null, new WebhookResourcesImpl(this));
        }
        return webhooks.get();
    }

    /**
     * Returns the PassthroughResources instance that provides access to passthrough resources.
     *
     * @return the passthrough resources
     */
    public PassthroughResources passthroughResources() {
        if (passthrough.get() == null) {
            passthrough.compareAndSet(null, new PassthroughResourcesImpl(this));
        }
        return passthrough.get();
    }

    /**
     * Returns the EventResources instance that provides access to events resources.
     *
     * @return the events resources
     */
    public EventResources eventResources() {
        if (events.get() == null) {
            events.compareAndSet(null, new EventResourcesImpl(this));
        }
        return events.get();
    }

    /**
     * Compose a User-Agent string that represents this version of the SDK (along with platform info)
     *
     * @return a User-Agent string
     */
    private String generateUserAgent(String userAgent) {
        String title = null;
        String thisVersion = null;

        if(userAgent == null) {
            StackTraceElement[] callers = Thread.currentThread().getStackTrace();
            String module = null;
            String callerClass = null;
            int stackIdx;
            for(stackIdx = callers.length - 1; stackIdx >= 0; stackIdx--) {
                callerClass = callers[stackIdx].getClassName();
                try {
                    Class<?> clazz = Class.forName(callerClass);
                    ClassLoader classLoader = clazz.getClassLoader();
                    // skip JRE classes
                    if (classLoader == null) {
                        continue;
                    }
                    String classFilePath = callerClass.replace(".", "/") + ".class";
                    URL classUrl = classLoader.getResource(classFilePath);
                    if (classUrl != null) {
                        String classUrlPath = classUrl.getPath();
                        int jarSeparator = classUrlPath.indexOf('!');
                        if (jarSeparator > 0) {
                            module = classUrlPath.substring(0, jarSeparator);
                            // extract the last path element (the jar name only)
                            module = module.substring(module.lastIndexOf('/') + 1);
                            break;
                        }
                    }
                } catch (Exception ex) { }
            }
            userAgent = module + "!" + callerClass;
        }
        try {
            final Properties properties = new Properties();
            properties.load(this.getClass().getClassLoader().getResourceAsStream("sdk.properties"));
            thisVersion = properties.getProperty("sdk.version");
            title = properties.getProperty("sdk.name");
        } catch (IOException e) { }
        return title + "/" + thisVersion + "/" + userAgent + "/" + System.getProperty("os.name") + " "
                + System.getProperty("java.vm.name") + " " + System.getProperty("java.vendor") + " "
                + System.getProperty("java.version");
    }
}
