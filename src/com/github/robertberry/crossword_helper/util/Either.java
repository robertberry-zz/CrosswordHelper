package com.github.robertberry.crossword_helper.util;

abstract public class Either<A, B> {
    abstract public boolean isLeft();
    abstract public boolean isRight();

    abstract public Left<A, B> left();
    abstract public Right<A, B> right();
}
