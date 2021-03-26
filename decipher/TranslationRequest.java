/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ist440.decipher;

import com.google.cloud.http.HttpTransportOptions;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TranslationRequest", value = "/translation")
public class TranslationRequest extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String sourceLanguage = req.getParameter("source");
        if (sourceLanguage == null) {
            InvalidRequestError error = new InvalidRequestError(
                    InvalidRequestError.CODE_MISSING_PARAMETER,
                    "This service requires a source value"
            );
            resp.getOutputStream().print(new Gson().toJson(error));
            return;
        }
        
        String text = req.getParameter("text");
        if (text == null) {
            InvalidRequestError error = new InvalidRequestError(
                    InvalidRequestError.CODE_MISSING_PARAMETER,
                    "This service requires a text value"
            );
            resp.getOutputStream().print(new Gson().toJson(error));
            return;
        }
        
        HttpTransportOptions transportOptions = TranslateOptions.getDefaultHttpTransportOptions();
        transportOptions = transportOptions.toBuilder().setConnectTimeout(60000).setReadTimeout(60000).build();
        
        Translate translate = TranslateOptions.newBuilder()
                .setApiKey("AIzaSyDDKV_qoxBG5tarYETn1MAV2SafCLriIQk")
                .setTransportOptions(transportOptions)
                .build().getService();
        Translation translation = translate.translate(
                text, 
                TranslateOption.sourceLanguage(sourceLanguage), 
                TranslateOption.targetLanguage("en")
        );
        TranslationSuccessResponse responseBody = new TranslationSuccessResponse(translation.getTranslatedText());
        resp.getOutputStream().print(new Gson().toJson(responseBody));
    }
    
}
