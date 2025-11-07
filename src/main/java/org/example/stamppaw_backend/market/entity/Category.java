package org.example.stamppaw_backend.market.entity;


import lombok.Getter;

@Getter
public enum Category {
    CLOTHING_GOODS("의류굿즈"),
    ACRYLIC_GOODS("아크릴굿즈"),
    TSHIRT("티셔츠"),
    KEYHOLDER("키홀더"),
    CAP("모자");

    private final String label;
    Category(String label) { this.label = label; }
}

