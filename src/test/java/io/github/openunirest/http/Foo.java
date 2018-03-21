package io.github.openunirest.http;

import java.util.Objects;

public class Foo<T> {
    public T bar;

    public Foo(){ }

    public Foo(T bar) {
        this.bar = bar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Foo foo = (Foo) o;
        return Objects.equals(bar, foo.bar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bar);
    }
}
