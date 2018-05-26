package pl.shopgen.builders;

import java.util.StringJoiner;

public class ApiErrorMessageBuilder {

    private StringJoiner stringJoiner;

    private ApiErrorMessageBuilder() {
        stringJoiner = new StringJoiner("\n");
    }

    public ApiErrorMessageBuilder notFound(String content) {
        stringJoiner.add("Not found: " + content);
        return this;
    }

    public ApiErrorMessageBuilder badParameter(String parameterName, String content) {
        stringJoiner.add("Bad parameter " + parameterName + ": " + content);
        return this;
    }

    public String build() {
        return stringJoiner.toString();
    }

    public static ApiErrorMessageBuilder getInstance() {
        return new ApiErrorMessageBuilder();
    }
}
