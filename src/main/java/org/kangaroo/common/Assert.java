package org.kangaroo.common;

import java.util.Collection;
import java.util.Map;

public class Assert {
	public Assert() {
	}

	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	public static void hasLength(String text, String message) {
		if (UtilCollection.isEmpty(text)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void hasLength(String text) {
		hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	public static void hasText(String text, String message) {
		if (UtilCollection.isEmpty(text)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void hasText(String text) {
		hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	public static void notEmpty(Object[] array, String message) {
		if (array == null || array.length == 0) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(Object[] array) {
		notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}

	public static void noNullElements(Object[] array, String message) {
		if (array != null) {
			Object[] var2 = array;
			int var3 = array.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				Object element = var2[var4];
				if (element == null) {
					throw new IllegalArgumentException(message);
				}
			}
		}

	}

	public static void noNullElements(Object[] array) {
		noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
	}

	public static void notEmpty(Collection collection, String message) {
		if (UtilCollection.isEmpty(collection)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(Collection collection) {
		notEmpty(collection,
				"[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	public static void notEmpty(Map map, String message) {
		if (UtilCollection.isEmpty(map)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(Map map) {
		notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
	}
}
