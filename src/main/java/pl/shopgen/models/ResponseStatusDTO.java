package pl.shopgen.models;

public class ResponseStatusDTO {
    private int code;
    private String message;

    public ResponseStatusDTO(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseStatusDTO() {

    }

    @Override
    final public int hashCode() {
        int result = code;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    final public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof ResponseStatusDTO)) {
            return false;
        }

        ResponseStatusDTO that = (ResponseStatusDTO) o;

        if(code != that.code) {
            return false;
        }
        return message != null ? message.equals(that.message) : that.message == null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
