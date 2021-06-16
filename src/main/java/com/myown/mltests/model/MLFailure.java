package com.myown.mltests.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

@XStreamAlias("failure")
public class MLFailure implements Serializable {

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
