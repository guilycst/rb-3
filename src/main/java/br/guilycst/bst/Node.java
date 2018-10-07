package br.guilycst.bst;

import java.util.Optional;

public class Node<T extends Comparable<T>> {

	private Optional<Node<T>> left = Optional.empty();
	private Optional<Node<T>> right = Optional.empty();
	private Optional<T> value = Optional.empty();

	public Node(T value) {
		this.value = Optional.of(value);
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
		return value;
	}

	public boolean hasChildren() {
		return left.isPresent() || right.isPresent();
	}

	public Optional<Node<T>> any(T key) {
		return Optional.ofNullable(child(left, key).orElse(child(right, key).orElse(null)));
	}

	private Optional<Node<T>> child(Optional<Node<T>> child, T otherKey) {
		Optional<Optional<Node<T>>> map = child
				.map(node -> node.value.map(key -> key.compareTo(otherKey)).map(x -> x == 0 ? node : null));
		return map.orElse(Optional.empty());
	}

	public static <T extends Comparable<T>> Optional<Node<T>> of(T key) {
		return Optional.of(new Node<T>(key));
	}

	void purge(final Node<T> node) {
		replace(node, Optional.empty());
	}

	void setKey(Optional<T> key) {
		this.value = key;
	}

	public Optional<Node<T>> any() {
		return Optional.ofNullable(left.orElse(right.orElse(null)));
	}

	void replace(Node<T> node, Optional<Node<T>> any) {

		left.filter(child -> child == node).ifPresent(c -> left = any);

		right.filter(child -> child == node).ifPresent(c -> right = any);

	}

	@Override
	public String toString() {
		return value.map(m -> "Value: ".concat(m.toString()))
				.map(m -> m.concat(" Left: ").concat(left.map(ml -> ml.getKey().get().toString()).orElse("nil")))
				.map(m -> m.concat(" Right: ").concat(right.map(mr -> mr.getKey().get().toString()).orElse("nil")))
				.orElse("Empty");
	}

}
