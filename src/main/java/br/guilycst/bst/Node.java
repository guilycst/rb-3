package br.guilycst.bst;

import java.util.Optional;

public class Node<T extends Comparable<T>> {

	private Optional<Node<T>> left = Optional.empty();
	private Optional<Node<T>> right = Optional.empty();
	private Optional<T> key = Optional.empty();

	public Node(T key) {
		this.key = Optional.of(key);
	}

	public Optional<Node<T>> getLeft() {
		return left;
	}

	void setLeft(Optional<Node<T>> left) {
		this.left = left;
	}

	public Optional<Node<T>> getRight() {
		return right;
	}

	void setRight(Optional<Node<T>> right) {
		this.right = right;
	}

	public Optional<T> getKey() {
		return key;
	}

	public static <T extends Comparable<T>> Optional<Node<T>> of(T key) {
		return Optional.of(new Node<T>(key));
	}

}
