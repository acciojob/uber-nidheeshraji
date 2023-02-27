//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.driver.services.impl;

import com.driver.model.Cab;
import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.model.TripBooking;
import com.driver.model.TripStatus;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.services.CustomerService;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	CustomerRepository customerRepository2;
	@Autowired
	DriverRepository driverRepository2;
	@Autowired
	TripBookingRepository tripBookingRepository2;

	public CustomerServiceImpl() {
	}

	public void register(Customer customer) {
		this.customerRepository2.save(customer);
	}

	public void deleteCustomer(Integer customerId) {
		Customer customer = (Customer)this.customerRepository2.findById(customerId).get();
		List<TripBooking> tripBookingList = customer.getTripBookings();
		Iterator var4 = tripBookingList.iterator();

		while(var4.hasNext()) {
			TripBooking tripBooking = (TripBooking)var4.next();
			if (tripBooking.getStatus() == TripStatus.CONFIRMED) {
				tripBooking.setStatus(TripStatus.CANCELED);
			}
		}

		this.customerRepository2.delete(customer);
	}

	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception {
		TripBooking tripBooking = new TripBooking();
		Driver driver = null;
		List<Driver> driverList = this.driverRepository2.findAll();
		Iterator var8 = driverList.iterator();

		while(true) {
			Driver driver1;
			do {
				do {
					if (!var8.hasNext()) {
						if (driver == null) {
							throw new Exception("No cab available!");
						}

						Customer customer = (Customer)this.customerRepository2.findById(customerId).get();
						tripBooking.setCustomer(customer);
						tripBooking.setDriver(driver);
						tripBooking.setStatus(TripStatus.CONFIRMED);
						tripBooking.setFromLocation(fromLocation);
						tripBooking.setToLocation(toLocation);
						driver.getCab().setAvailable(false);
						tripBooking.setDistanceInKm(distanceInKm);
						int rate = driver.getCab().getPerKmRate();
						tripBooking.setBill(distanceInKm * rate);
						customer.getTripBookings().add(tripBooking);
						this.customerRepository2.save(customer);
						driver.getTripBookingList().add(tripBooking);
						this.driverRepository2.save(driver);
						return tripBooking;
					}

					driver1 = (Driver)var8.next();
				} while(!driver1.getCab().getAvailable());
			} while(driver != null && driver.getDriverId() <= driver1.getDriverId());

			driver = driver1;
		}
	}

	public void cancelTrip(Integer tripId) {
		TripBooking tripBooking = (TripBooking)this.tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.CANCELED);
		tripBooking.setBill(0);
		tripBooking.getDriver().getCab().setAvailable(true);
		this.tripBookingRepository2.save(tripBooking);
	}

	public void completeTrip(Integer tripId) {
		TripBooking tripBooking = (TripBooking)this.tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.COMPLETED);
		int distance = tripBooking.getDistanceInKm();
		Driver driver = tripBooking.getDriver();
		Cab cab = driver.getCab();
		int rate = cab.getPerKmRate();
		tripBooking.setBill(distance * rate);
		tripBooking.getDriver().getCab().setAvailable(true);
		this.tripBookingRepository2.save(tripBooking);
	}
}
