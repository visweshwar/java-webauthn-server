// Copyright (c) 2018, Yubico AB
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice, this
//    list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.yubico.webauthn.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yubico.webauthn.data.exception.Base64UrlException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;


@Value
public class AuthenticatorAssertionResponse implements AuthenticatorResponse {

    @NonNull
    private final ByteArray authenticatorData;

    @NonNull
    private final ByteArray clientDataJSON;

    @NonNull
    private final ByteArray signature;

    @NonNull
    private final Optional<ByteArray> userHandle;

    @NonNull
    private final transient CollectedClientData clientData;

    @Builder
    @JsonCreator
    public AuthenticatorAssertionResponse(
        @NonNull @JsonProperty("authenticatorData") final ByteArray authenticatorData,
        @NonNull @JsonProperty("clientDataJSON") final ByteArray clientDataJSON,
        @NonNull @JsonProperty("signature") final ByteArray signature,
        @JsonProperty("userHandle") final ByteArray userHandle
    ) throws IOException, Base64UrlException {
        this.authenticatorData = authenticatorData;
        this.clientDataJSON = clientDataJSON;
        this.signature = signature;
        this.userHandle = Optional.ofNullable(userHandle);
        this.clientData = new CollectedClientData(this.clientDataJSON);
    }

    public static AuthenticatorAssertionResponseBuilder.MandatoryStages builder() {
        return new AuthenticatorAssertionResponseBuilder.MandatoryStages();
    }

    public static class AuthenticatorAssertionResponseBuilder {
        private Optional<ByteArray> userHandle = Optional.empty();

        public static class MandatoryStages {
            private final AuthenticatorAssertionResponseBuilder builder = new AuthenticatorAssertionResponseBuilder();

            public Step2 authenticatorData(ByteArray authenticatorData) {
                builder.authenticatorData(authenticatorData);
                return new Step2();
            }

            public class Step2 {
                public Step3 clientDataJSON(ByteArray clientDataJSON) {
                    builder.clientDataJSON(clientDataJSON);
                    return new Step3();
                }
            }

            public class Step3 {
                public AuthenticatorAssertionResponseBuilder signature(ByteArray signature) {
                    return builder.signature(signature);
                }
            }
        }
    }

    @JsonIgnore
    public String getClientDataJSONString() {
        return new String(clientDataJSON.getBytes(), Charset.forName("UTF-8"));
    }

}
