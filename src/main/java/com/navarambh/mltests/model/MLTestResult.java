package com.navarambh.mltests.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

@XStreamAlias("TestSuite")
public class MLTestResult implements Serializable {

    @XStreamImplicit
    @XStreamAlias("properties")
    protected List<Properties> properties;

    @XStreamAlias("name")
    protected String name;

    protected String time;

    protected String tests;

    protected String errors;

    protected String skipped;

    protected String failures;

    protected String group;

    public List<Properties> getProperties() {
        return properties;
    }

    public void setProperties(List<Properties> properties) {
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getSkipped() {
        return skipped;
    }

    public void setSkipped(String skipped) {
        this.skipped = skipped;
    }

    public String getFailures() {
        return failures;
    }

    public void setFailures(String failures) {
        this.failures = failures;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
