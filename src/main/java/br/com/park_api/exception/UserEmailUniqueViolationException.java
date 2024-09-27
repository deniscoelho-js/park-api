package br.com.park_api.exception;

public class UserEmailUniqueViolationException extends RuntimeException{
    public UserEmailUniqueViolationException(String message){
        super(message);
    }
}
