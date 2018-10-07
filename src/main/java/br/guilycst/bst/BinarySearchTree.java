package br.guilycst.bst;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BinarySearchTree<T extends Comparable<T>> implements Collection<T> {

	private Optional<Node<T>> root = Optional.empty();

	public Optional<Node<T>> find(T key) {
		assertKeyNotNull(key);
		return find(Optional.empty(), root, key, false);
	}

	@Override
	public Iterator<T> iterator() {
		return new BinarySearchTreeIterator();
	}

	@Override
	public int size() {
		return traverse(root).size();
	}

	@Override
	public boolean isEmpty() {
		return !root.isPresent();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		try {
			return find((T) o).isPresent();
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public Object[] toArray() {
		return traverse(root).toArray();
	}

	@Override
	public <U> U[] toArray(U[] a) {
		return traverse(root).toArray(a);
	}

	@Override
	public boolean add(T e) {
		insert(e);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		try {
			return delete((T) o);
		} catch (ClassCastException e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsAll(Collection<?> c) {
		try {
			return c.stream().allMatch(p -> find((T) p).isPresent());
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		collection.stream().forEach(c -> add(c));
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		collection.stream().forEach(c -> remove(c));
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		Collection<T> traversed = traverse(root);
		traversed.removeAll(collection);
		traversed.forEach(c -> remove(c));
		return true;
	}

	@Override
	public void clear() {
		root = Optional.empty();
	}

	private Optional<Node<T>> find(Optional<Node<T>> parent, Optional<Node<T>> current, T key, boolean findParent) {
		return current.map(x -> x.getKey().map(y -> key.compareTo(y)).map(z -> {
			if (z > 0)
				return find(current, x.getRight(), key, findParent).orElse(null);
			else if (z < 0)
				return find(current, x.getLeft(), key, findParent).orElse(null);
			return findParent ? parent.orElse(null) : x;
		})).map(x -> (Node<T>) x.orElse(null));
	}

	private void insert(T key) {
		assertKeyNotNull(key);
		if (root.isPresent())
			insert(root, key);
		else
			root = Node.of(key);
	}

	private void insert(Optional<Node<T>> current, T key) {
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

	private Optional<Node<T>> findLeftmost(Optional<Node<T>> current) {
		return current.map(c -> {
			while (c.getLeft().isPresent()) {
				c = c.getLeft().get();
			}
			return c;
		});
	}

	private void deleteFromParent(Node<T> child) {
		find(Optional.empty(), root, child.getKey().get(), true).ifPresent(parent -> parent.purge(child));
		;
	}

	private boolean delete(T key) {
		return find(Optional.empty(), root, key, true) // find parent node
				.map(node -> {
					node.any(key).map(child -> {
						if (child.hasChildren()) {
							if (child.getLeft().isPresent() && child.getRight().isPresent()) { // has both children

								/*
								 * Deleting a node with two children: call the node to be deleted D. Do not
								 * delete D. Instead, choose either its in-order predecessor node or its
								 * in-order successor node as replacement node E (s. figure). Copy the user
								 * values of E to D.[note 2] If E does not have a child simply remove E from its
								 * previous parent G. If E has a child, say F, it is a right child. Replace E
								 * with F at E's parent.
								 */
								findLeftmost(Optional.of(child)).ifPresent(leftmost -> {
									Optional<Node<T>> leftmostChild = leftmost.any();
									if (leftmostChild.isPresent()) {
										find(Optional.of(node), Optional.of(child), leftmostChild.get().getKey().get(),
												true).ifPresent(c -> c.replace(leftmost, leftmostChild));
									} else {
										deleteFromParent(leftmost);
									}
									child.setKey(leftmost.getKey());
								});
							} else {
								node.replace(child, child.any()); // Deleting a node with one child: remove the node and
																	// replace it with its child.
							}
						} else
							node.purge(child); // Deleting a node with no children: simply remove the node from the
												// tree.
						return true;
					});
					return true;
				}).orElse(false);
	}

	private void traverse(Optional<Node<T>> node, Consumer<T> c) {
		if (!node.isPresent())
			return;
		Node<T> unwraped = node.get();
		traverse(node.get().getLeft(), c);
		c.accept(unwraped.getKey().get());
		traverse(node.get().getRight(), c);
	}

	private Collection<T> traverse(Optional<Node<T>> node) {
		List<T> elements = new LinkedList<T>();
		traverse(root, c -> elements.add(c));
		return elements;
	}

	private class BinarySearchTreeIterator implements Iterator<T> {

		private Iterator<T> innetIterator;

		BinarySearchTreeIterator() {
			innetIterator = traverse(root).iterator();
		}

		@Override
		public boolean hasNext() {
			return innetIterator.hasNext();
		}

		@Override
		public T next() {
			return innetIterator.next();
		}

	}

}
