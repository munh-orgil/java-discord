package org.example.socket;

import javafx.event.ActionEvent;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.Function;

public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 2;
    public String message;
    public int status;
    public String func;
    public Object body;

    public Response(String message, int status, Object body, String func) {
        this.message = message;
        this.status = status;
        this.body = body;
        this.func = func;
    }

    @Override
    public String toString() {
        return "Response\n" +
                "message: " + this.message + "\n" +
                "status: " + this.status + "\n" +
                "function: " + this.func + "\n" +
                "body: " + this.body + "\n";
    }
}
