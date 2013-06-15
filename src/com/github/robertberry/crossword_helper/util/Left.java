package com.github.robertberry.crossword_helper.util;

public class Left<A, B> extends Either<A, B> {
    public final A leftValue;

    public Left(A a) {
        leftValue = a;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public Left<A, B> left() {
        return this;
    }

    @Override
    public Right<A, B> right() {
        throw new RuntimeException("Called 'right' on a Left");
    }
}
