package edu.pk.dto;

import edu.pk.annotation.Comparator;

public class Employee {
	private Long empId;
	private String name;

	public Employee(Long empId, String name) {
		super();
		this.empId = empId;
		this.name = name;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Comparator("EmployeeNameComparator11")
	public int compareByName(Employee that) {
		if (this == that)
			return 0;
		return this.name.compareTo(that.name);
	}

}
