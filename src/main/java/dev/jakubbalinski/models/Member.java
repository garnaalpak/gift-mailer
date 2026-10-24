package dev.jakubbalinski.models;

import java.util.concurrent.atomic.AtomicLong;

public record Member(
        Long id,
        String name,
        String mail
) {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public Member(String name, String mail){
        this(ID_GENERATOR.getAndIncrement(), name,mail);
    }
}
