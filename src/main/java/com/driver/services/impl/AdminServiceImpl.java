//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.AdminRepository;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.services.AdminService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	AdminRepository adminRepository1;
	@Autowired
	DriverRepository driverRepository1;
	@Autowired
	CustomerRepository customerRepository1;

	public AdminServiceImpl() {
	}

	public void adminRegister(Admin admin) {
		this.adminRepository1.save(admin);
	}

	public Admin updatePassword(Integer adminId, String password) {
		Admin admin = (Admin)this.adminRepository1.findById(adminId).get();
		admin.setPassword(password);
		this.adminRepository1.save(admin);
		return admin;
	}

	public void deleteAdmin(int adminId) {
		Admin admin = (Admin)this.adminRepository1.findById(adminId).get();
		this.adminRepository1.delete(admin);
	}

	public List<Driver> getListOfDrivers() {
		List<Driver> drivers = this.driverRepository1.findAll();
		return drivers;
	}

	public List<Customer> getListOfCustomers() {
		List<Customer> customerList = this.customerRepository1.findAll();
		return customerList;
	}
}
