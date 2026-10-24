package dev.jakubbalinski.services;

import dev.jakubbalinski.models.DrawResult;
import dev.jakubbalinski.models.Member;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class DrawService {

    private final Random random;

    public DrawService() {
        this(new SecureRandom());
    }

    public DrawService(Random random) {
        this.random = random;
    }

    public List<DrawResult> getDrawResults(List<Member> members) {

        if (members.size() < 2) {
            throw new IllegalArgumentException("Do losowania potrzeba minimum 2 osób!");
        }

        List<Member> receivers = new ArrayList<>(members);

        do {
            Collections.shuffle(receivers, random);
        } while (hasSelfAssignment(members, receivers));

        return IntStream.range(0, members.size())
                .mapToObj(i -> new DrawResult(members.get(i), receivers.get(i)))
                .toList();
    }

    private boolean hasSelfAssignment(List<Member> givers, List<Member> receivers) {
        return IntStream.range(0, givers.size())
                .anyMatch(i -> givers.get(i).id().equals(receivers.get(i).id()));
    }
}