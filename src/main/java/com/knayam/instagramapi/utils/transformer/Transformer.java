package com.knayam.instagramapi.utils.transformer;

public interface Transformer<A, B> {

	public B transformTo(A a);
}
