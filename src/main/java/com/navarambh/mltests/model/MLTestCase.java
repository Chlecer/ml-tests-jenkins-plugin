package com.navarambh.mltests.model;

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
