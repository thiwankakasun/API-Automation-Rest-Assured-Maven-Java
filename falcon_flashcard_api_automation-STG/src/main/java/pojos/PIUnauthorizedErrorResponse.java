package pojos;

import java.util.Map;

public class PIUnauthorizedErrorResponse {

    protected String status;
    protected String message;
    protected String code;
    protected Map<Object, Object> fault;

    public PIUnauthorizedErrorResponse() {

    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Map<Object, Object> getFault() {
        return fault;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setFault(Map<Object, Object> fault) {
        this.fault = fault;
    }
}
