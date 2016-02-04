/*
  Copyright 2012 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.scribe.builder.api;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.AccessTokenExtractor;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.scribe.extractors.PayPalJsonExtractor;

/**
 * This class represents the OAuth API implementation for PayPal. It could be part of the Scribe library.
 * 
 * @author Jerome Leleu
 * @since 1.4.2
 */
public class PayPalApi20 extends DefaultApi20 {
    private static final String AUTHORIZATION_URL = "https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize?client_id=%s&redirect_uri=%s&scope=%s&response_type=code&nonce=%s";
    
    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(),
                                    "Must provide a valid url as callback. PayPal does not support OOB");
        final String nonce = System.currentTimeMillis() + CommonHelper.randomString(10);
        return String.format(AUTHORIZATION_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()),
                             OAuthEncoder.encode(config.getScope()), nonce);
    }
    
    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.paypal.com/v1/identity/openidconnect/tokenservice";
    }
    
    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new PayPalJsonExtractor();
    }
}
