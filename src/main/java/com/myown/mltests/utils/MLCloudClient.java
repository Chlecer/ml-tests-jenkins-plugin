package com.myown.mltests.utils;

import com.google.gson.JsonObject;
import hudson.model.Run;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;

public class MLCloudClient {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON_UNICODE = "application/json;charset=UTF-8";
    private static final String API_VERSION = "/api/v1";
    private static final String ML_SCHEME = "AioAuth";
    private static final String IMPORT_RESULTS_FILE = "/project/{mlProjectId}/testcycle/{testCycleId}/import/results";
    private static final String CREATE_CYCLE = "/tests";
    private String mlServerAddress;
    private String projectId;

    public MLCloudClient(String mlServerAddress, String projectId) {
        this.mlServerAddress = mlServerAddress;
        this.projectId = projectId;
    }

    public HttpResponse<String> importResults(String frameworkType, boolean hideDetails, Run<?, ?> run,
                                              PrintStream logger) {

        logger.println("Creating new cycle with prefix " + "testCycleId" + " ....");

        HttpResponse<String> response = this.createTest(frameworkType, run);
        JSONObject responseBody = this.validateResponse(response, "Import results");
        logResults(frameworkType, responseBody, hideDetails, logger);
        return response;
    }

    private void logResults(String frameworkType, JSONObject responseBody, boolean hideDetails, PrintStream logger) {
        final int keyColumnLength = 80;
        final int dividerLength = 100;
        if(responseBody != null) {
            logger.println(StringUtils.rightPad("Status:", 30) + responseBody.getString("status"));
            logger.println(StringUtils.rightPad("Total Runs:", 30) + responseBody.getString("requestCount"));
            logger.println(StringUtils.rightPad("Successfully updated:", 30) + responseBody.getString("successCount"));
            logger.println(StringUtils.rightPad("Errors:", 30) + responseBody.getString("errorCount"));
            if (!hideDetails) {
                logger.println(StringUtils.rightPad("", dividerLength, "-"));
                logger.println(StringUtils.rightPad("Key", keyColumnLength) + (frameworkType.toLowerCase().equals("cucumber")? "": "Run Status"));
                logger.println(StringUtils.rightPad("", dividerLength, "-"));
                if(!responseBody.getString("errorCount").equals("0")) {
                    JSONObject errors = responseBody.getJSONObject("errors");
                    Iterator<String> caseIterator = errors.keys();
                    while (caseIterator.hasNext()) {
                        String key = caseIterator.next();
                        logger.println(StringUtils.rightPad(StringUtils.abbreviate(key,keyColumnLength), keyColumnLength)
                                + errors.getJSONObject(key).getString("message"));
                    }
                }
                if(responseBody.get("processedData") != null) {
                    JSONObject processedData = responseBody.getJSONObject("processedData");
                    Iterator<String> caseIterator = processedData.keys();
                    while (caseIterator.hasNext()) {
                        String key = caseIterator.next();
                        logger.println(StringUtils.rightPad(StringUtils.abbreviate(key,keyColumnLength), keyColumnLength)
                                + (frameworkType.toLowerCase().equals("cucumber") ? "" : processedData.getJSONObject(key).getString("status")));
                    }
                }
                logger.println(StringUtils.rightPad("", dividerLength, "-"));
            }
        }
    }

    private JSONObject validateResponse(HttpResponse<String> response, String task) {
        if(response.getStatus() == 401) {
            throw new MLTestsAuthorizationException();
        } else {
            if(response.getStatus() == 200) {
                return new JsonNode(response.getBody()).getObject();
            } else {
                throw new MLTestsException(task + " failed with error - \"" + response.getBody() + "\"");
            }
        }
    }

    private HttpResponse<String> createTest(String name, Run run) {
        String objective = "Created by automation run " + run.toString() ;
        String cycleTitle = name + " - " + run.getTime().toString();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name",cycleTitle);
        jsonObject.addProperty("description", objective);

        return Unirest.post(getMLEndpoint(CREATE_CYCLE))
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON_UNICODE)
                .body(jsonObject).asString();
    }

    private HttpResponse<String> importResults(String frameworkType,
                                                 boolean addCase, File f) {
        String uploadEndpoint = getMLEndpoint(IMPORT_RESULTS_FILE);
        return Unirest.post(uploadEndpoint)
                .queryString("type",frameworkType)
                .routeParam("mlProjectId", this.projectId)
                .field("file", f).asString();
    }

    private String getMLEndpoint(String url) {
        return mlServerAddress + "/" + url;
    }

    private static String getAuthKey(String apiKey) {
        return ML_SCHEME + " " + apiKey;
    }

}
