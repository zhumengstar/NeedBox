package com.yaya.o2o.exceptions;

public class ProductOperationException extends RuntimeException {

    private static final long serialVersionUID = 7997653303473453358L;

    public ProductOperationException(String msg) {
        super(msg);
    }
}
