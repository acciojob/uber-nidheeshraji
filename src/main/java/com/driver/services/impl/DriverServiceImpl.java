//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.driver.services.impl;

import com.driver.model.Cab;
import com.driver.model.Driver;
import com.driver.repository.CabRepository;
import com.driver.repository.DriverRepository;
import com.driver.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverService {
	@Autowired
	DriverRepository driverRepository3;
	@Autowired
	CabRepository cabRepository3;

	public DriverServiceImpl() {
	}

	public void register(String mobile, String password) {
		Driver driver = new Driver();
		driver.setMobile(mobile);
		driver.setPassword(password);
		Cab cab = driver.getCab();
		cab.setAvailable(true);
		cab.setPerKmRate(10);
		this.driverRepository3.save(driver);
		this.cabRepository3.save(cab);
	}

	public void removeDriver(int driverId) {
		Driver driver = (Driver)this.driverRepository3.findById(driverId).get();
		this.driverRepository3.delete(driver);
	}

	public void updateStatus(int driverId) {
		Driver driver = (Driver)this.driverRepository3.findById(driverId).get();
		Cab cab = driver.getCab();
		cab.setAvailable(false);
		this.cabRepository3.save(cab);
	}
}
