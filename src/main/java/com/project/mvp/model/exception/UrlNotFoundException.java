package com.project.mvp.model.exception;

public class UrlNotFoundException extends RuntimeException
{
    public UrlNotFoundException(String message)
    {
        super(message);
    }
}
