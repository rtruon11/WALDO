/*
 * Copyright 2018 chrismorris.
 *
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
 */
package com.ist440.decipher;

/**
 *
 * @author chrismorris
 */
public class InvalidRequestError {
    
    public final static String CODE_MISSING_PARAMETER = "CODE_MISSING_PARAMETER";
    public final static String CODE_MISSING_BODY = "CODE_MISSING_BODY";
    public final static String CODE_EMPTY_ANNOTATION_RESPONSE = "CODE_EMPTY_ANNOTATION_RESPONSE";
    public final static String CODE_INVALID_SOURCE_LANGUAGE = "CODE_INVALID_SOURCE_LANGUAGE";
    
    private String code;
    private String message;
    
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    public InvalidRequestError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
