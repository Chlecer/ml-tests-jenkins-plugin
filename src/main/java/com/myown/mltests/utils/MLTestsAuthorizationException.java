package com.myown.mltests.utils;

public class MLTestsAuthorizationException extends MLTestsException {

    MLTestsAuthorizationException() {
        super("User is unauthorized.  Please check API token of whether user has access to project.");
    }

}
