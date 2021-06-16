package com.myown.mltests.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

@XStreamAlias("testcase")
public class MLTestCase implements Serializable {

    @XStreamAsAttribute
    protected String name;
    @XStreamAsAttribute
    protected String classname;
    @XStreamAsAttribute
    protected String time;
    @XStreamAsAttribute
    @XStreamImplicit
    protected List<MLFailure> failure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<MLFailure> getFailure() {
        return failure;
    }

    public void setFailure(List<MLFailure> failure) {
        this.failure = failure;
    }

    @Override
    public String toString() {
        return "MLTestCase{" +
                "name='" + name + '\'' +
                ", classname='" + classname + '\'' +
                ", time='" + time + '\'' +
                ", failure=" + failure +
                '}';
    }

}
