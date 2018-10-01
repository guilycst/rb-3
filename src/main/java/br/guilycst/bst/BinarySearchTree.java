package br.guilycst.bst;

import java.util.Optional;

public class BinarySearchTree<T extends Comparable<T>> {

	private Optional<Node<T>> root = Optional.empty();

	public Optional<Node<T>> find(T key) {
		assertKeyNotNull(key);
		return find(root, key);
	}

	protected Optional<Node<T>> find(Optional<Node<T>> current, T key) {
		return current.map(x -> x.getKey().map(y -> key.compareTo(y)).map(z -> {
			if (z > 0)
				return find(x.getRight(), key).orElse(null);
			else if (z < 0)
				return find(x.getLeft(), key).orElse(null);
			return x;
		})).map(x -> (Node<T>) x.get());
	}

	protected void insert(T key) {
		assertKeyNotNull(key);
		if (root.isPresent())
			insert(root, key);
		else
			root = Node.of(key);
	}

	protected void insert(Optional<Node<T>> current, T key) {
		current.ifPresent(cNode -> cNode.getKey().map(cNodeKey -> key.compareTo(cNodeKey)) //
				.ifPresent(diff -> {
					if (diff >= 0) {
						if (cNode.getRight().isPresent())
							insert(cNode.getRight(), key);
						else
							cNode.setRight(Node.of(key));
						return;
					} else {
						if (cNode.getLeft().isPresent())
							insert(cNode.getLeft(), key);
						else
							cNode.setLeft(Node.of(key));
						return;
					}

				}));
	}

	private void assertKeyNotNull(T key) {
		if (key == null)
			throw new IllegalArgumentException("Key cannot be null");
	}

}
