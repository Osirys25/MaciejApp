package org.maciejowka.maciejapp.events.data;

/**
 * Created by maciej on 13.09.17.
 */
public class Result<T> {
    private final boolean success;
    private static final String EMPTY_MESSAGE = "";
    private final T data;
    private String message;

    private Result(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static <A> Result<A> success(A data) {
        return new Result<>(true, data, EMPTY_MESSAGE);
    }

    public static <A> Result<A> failure() {
        return new Result<>(false, null, EMPTY_MESSAGE);
    }

    public static <A> Result<A> failure(String message) {
        return new Result<>(false, null, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
