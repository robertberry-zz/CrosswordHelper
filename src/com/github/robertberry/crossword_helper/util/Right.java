package com.github.robertberry.crossword_helper.util;

public class Right<A, B> extends Either<A, B> {
    public final B rightValue;

    public Right(B b) {
        rightValue = b;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public Left<A, B> left() {
        throw new RuntimeException("Called 'left' on a Right.");
    }

    @Override
    public Right<A, B> right() {
        return this;
    }
}
