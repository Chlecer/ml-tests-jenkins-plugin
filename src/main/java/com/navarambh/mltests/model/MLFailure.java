package com.navarambh.mltests.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("failure")
public class MLFailure {

    @XStreamAsAttribute
    protected String value;
    @XStreamAsAttribute
    protected String message;
    @XStreamAsAttribute
    protected String type;

    @Override
    public String toString() {
        return "MLFailure{" +
                "value='" + value + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
