package org.example.socket;

import javafx.event.ActionEvent;
import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 99808453L;
    public String module;
    public String method;
    public String func;
    public Object body;
    public Request(String method, String module, String func, Object body) {
        this.module = module;
        this.method = method;
        this.func = func;
        this.body = body;
    }
    public Request(String method, String module, Object body) {
        this.module = module;
        this.method = method;
        this.body = body;
    }
}
