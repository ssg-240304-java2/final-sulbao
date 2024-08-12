package com.finalproject.sulbao.product.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCategory {
    탁주(1,"탁주"),
    과실주(2,"과실주"),
    전통소주(3,"전통소주"),
    약주(4,"약주"),
    기타(5,"기타");

    private final int key;
    private final String value;
}
