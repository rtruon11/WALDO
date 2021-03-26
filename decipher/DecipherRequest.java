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

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DecipherRequest", value = "/decipher")
public class DecipherRequest extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String cipheredMessage = req.getParameter("cipheredMessage");

        if (cipheredMessage == null) {
            InvalidRequestError error = new InvalidRequestError(
                    InvalidRequestError.CODE_MISSING_PARAMETER,
                    "This service requires a cipheredMessage value"
            );
            resp.getOutputStream().print(new Gson().toJson(error));
            return;
        }
        
        CompoundDecipher decipher = new CompoundDecipher(cipheredMessage);
        CompoundDecipherResult result = decipher.decipher();
        if (!result.didDecipherMessage()) {
            DecipherError error = new DecipherError(
                    result.getFailingProcesses(), 
                    result.getSuccessfulProcesses(), 
                    "Message could not be deciphered because one or more of the brute force methods failed."
            );
            String jsonError = new Gson().toJson(new ErrorWrapper(error));
            resp.getOutputStream().print(jsonError);
            return;
        }
        
        DecipherSuccessResponse responseObject = new DecipherSuccessResponse(
                new DecipherSuccessResponseData(result.getDecipheredMessage())
        );
        resp.getOutputStream().print(new Gson().toJson(responseObject));
    }

}
