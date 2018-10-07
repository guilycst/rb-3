package br.guilycst.bst;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BinarySearchTreeTests {

	private BinarySearchTree<Integer> intTree;
	Integer[] elements = { 8, 3, 1, 6, 4, 7, 10, 14, 13 };

	@Before
	public void setUp() {
		intTree = new BinarySearchTree<>();
		Arrays.stream(elements).forEach(c -> intTree.add(c));
	}

	@Test
	public void testFind() {
		Arrays.stream(elements).forEach(c -> {
			Optional<Node<Integer>> el = intTree.find(c);
			Assert.assertTrue(el.isPresent());
			Assert.assertTrue(el.get().getKey().isPresent());
			Assert.assertEquals(c, el.get().getKey().get());
		});
	}

	@Test
	public void testInsert() {
		List<TreeStructAssertion<Integer>> assertions = new ArrayList<TreeStructAssertion<Integer>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{ //
				add(assertTreeStruct(8).l(3).r(10));
				add(assertTreeStruct(3).l(1).r(6));
				add(assertTreeStruct(6).l(4).r(7));
				add(assertTreeStruct(4).l(null).r(null));
				add(assertTreeStruct(7).l(null).r(null));
				add(assertTreeStruct(10).l(null).r(14));
				add(assertTreeStruct(14).l(13).r(null));
				add(assertTreeStruct(13).l(null).r(null));
			}
		};

		assertions.forEach(c -> {
			c.assertNode(intTree.find(c.key));
		});

	}

	@Test
	public void testDeleteNodeNoChildren() {
		intTree.remove(4);

		List<TreeStructAssertion<Integer>> assertions = new ArrayList<TreeStructAssertion<Integer>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{ //
				add(assertTreeStruct(8).l(3).r(10));
				add(assertTreeStruct(3).l(1).r(6));
				add(assertTreeStruct(6).l(null).r(7));
				add(assertTreeStruct(7).l(null).r(null));
				add(assertTreeStruct(10).l(null).r(14));
				add(assertTreeStruct(14).l(13).r(null));
				add(assertTreeStruct(13).l(null).r(null));
			}
		};

		assertions.forEach(c -> {
			c.assertNode(intTree.find(c.key));
		});
	}

	@Test
	public void testDeleteNodeOneChildren() {
		intTree.remove(14);

		List<TreeStructAssertion<Integer>> assertions = new ArrayList<TreeStructAssertion<Integer>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{ //
				add(assertTreeStruct(8).l(3).r(10));
				add(assertTreeStruct(3).l(1).r(6));
				add(assertTreeStruct(6).l(4).r(7));
				add(assertTreeStruct(4).l(null).r(null));
				add(assertTreeStruct(7).l(null).r(null));
				add(assertTreeStruct(10).l(null).r(13));
				// add(assertTreeStruct(14).l(13).r(null));
				add(assertTreeStruct(13).l(null).r(null));
			}
		};

		assertions.forEach(c -> {
			c.assertNode(intTree.find(c.key));
		});
	}

	@Test
	public void testDeleteNodeBothChildren() {
		intTree.remove(6);

		List<TreeStructAssertion<Integer>> assertions = new ArrayList<TreeStructAssertion<Integer>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{ //
				add(assertTreeStruct(8).l(3).r(10));
				add(assertTreeStruct(3).l(1).r(4));
				add(assertTreeStruct(4).l(null).r(7));
				// add(assertTreeStruct(4).l(null).r(null));
				add(assertTreeStruct(7).l(null).r(null));
				add(assertTreeStruct(10).l(null).r(14));
				add(assertTreeStruct(14).l(13).r(null));
				add(assertTreeStruct(13).l(null).r(null));
			}
		};

		assertions.forEach(c -> {
			c.assertNode(intTree.find(c.key));
		});
	}

	@Test
	public void traverseTest() {
		Object[] expectedElements = { 1, 3, 4, 6, 7, 8, 10, 13, 14 };

		Object[] actualElements = intTree.toArray();
		assertArrayEquals(expectedElements, actualElements);
	}

	private <T extends Comparable<T>> TreeStructAssertion<T> assertTreeStruct(T key) {
		return new TreeStructAssertion<T>(key);
	}

	private static class TreeStructAssertion<T extends Comparable<T>> {
		T key;
		T left;
		T right;

		TreeStructAssertion(T key) {
			this.key = key;
		}

		TreeStructAssertion<T> l(T key) {
			left = key;
			return this;
		}

		TreeStructAssertion<T> r(T key) {
			right = key;
			return this;
		}

		void assertNode(Optional<Node<T>> node) {
			assertTrue("Node not present", node.isPresent());
			assertNode(node, key);
			assertNode(node.get().getLeft(), left);
			assertNode(node.get().getRight(), right);
		}

		void assertNode(Optional<Node<T>> node, T key) {
			if (key == null) {
				Assert.assertFalse("Expected node to NOT be present: " + node.toString(), node.isPresent());
			} else {
				Assert.assertTrue("Expected node to BE present: " + node.toString(), node.get().getKey().isPresent());
				Assert.assertEquals(key, node.get().getKey().get());
			}
		}
	}

}
