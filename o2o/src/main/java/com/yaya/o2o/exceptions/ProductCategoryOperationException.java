package com.yaya.o2o.exceptions;

public class ProductCategoryOperationException extends RuntimeException {

    private static final long serialVersionUID = 9096366746929673694L;

    public ProductCategoryOperationException(String msg) {
        super(msg);
    }
}
