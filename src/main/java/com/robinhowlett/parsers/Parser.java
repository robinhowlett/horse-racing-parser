package com.robinhowlett.parsers;

public interface Parser<T, S> {

    T parse(S source);
}
