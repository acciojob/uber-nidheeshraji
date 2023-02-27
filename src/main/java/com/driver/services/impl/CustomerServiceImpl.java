package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.CabRepository;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Autowired
	CabRepository cabRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		Customer newcustomer=new Customer();
		newcustomer.setPassword(customer.getPassword());
		newcustomer.setMobile(customer.getMobile());
		customerRepository2.save(newcustomer);

	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
        List<Cab>cabList=cabRepository2.findAll();
		Cab cab=null;
		int n=cabList.size();
		Driver driver = null;
		int temp=Integer.MAX_VALUE;
		for(int i=0;i<n;i++)
		{
			 cab=cabList.get(i);
			if(cab.getAvailable())
			{
			   Driver newdriver=cab.getDriver();
				if(temp<newdriver.getDriverId())
						temp=newdriver.getDriverId();

			}
			if(i==n-1)
				throw new Exception("No cab available!");

		}
		driver=driverRepository2.findById(temp).get();
		cab=driver.getCab();
		cab.setAvailable(false);
		Customer customer=customerRepository2.findById(customerId).get();

		TripBooking tripBooking=new TripBooking();
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setStatus(TripStatus.CONFIRMED);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setCustomer(customer);
		List<TripBooking>ans=driver.getTripBookingList();
		ans.add(tripBooking);
		tripBookingRepository2.save(tripBooking);
		driverRepository2.save(driver);
		cabRepository2.save(cab);
		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
		tripBookingRepository2.deleteById(tripId);
		tripBooking.setStatus(TripStatus.CANCELED);


	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
       TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
	   tripBooking.setStatus(TripStatus.COMPLETED);
		int dis=tripBooking.getDistanceInKm();
		Driver driver=tripBooking.getDriver();
		Cab cab=driver.getCab();
		int rate=cab.getPerKmRate();
		tripBooking.setBill(10*dis);
		cab.setAvailable(true);
		tripBookingRepository2.save(tripBooking);
		cabRepository2.save(cab);


	}
}
