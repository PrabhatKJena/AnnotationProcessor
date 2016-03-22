package edu.pk.dto;

import edu.pk.annotation.Comparator;

public class Product {
	String category;
	double price;

	@Comparator("ProductCategory")
	public int compareByCategory(Product that) {
		if (this == that)
			return 0;
		return this.category.compareTo(that.category);
	}
}
