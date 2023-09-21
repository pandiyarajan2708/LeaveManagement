package com.LeaveManagement.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LeaveManagement.Model.Leave;
import com.LeaveManagement.Repository.LeaveRepository;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {
	
	@Autowired
	LeaveRepository repository;

	@PostMapping("/LeaveRequest")
	public ResponseEntity<?> LeaveRequest(@RequestBody Leave leaveRequest){
		
		String uid=generateUID();
		leaveRequest.setId(uid);
		return ResponseEntity.ok().body(repository.save(leaveRequest));
	}
	
	@GetMapping("/getByUserName/{userName}")
	public ResponseEntity<?> getByUserName(@PathVariable String userName)
	{
		Optional<Leave> leaveOptional = repository.findByUserName(userName);
		
		if(leaveOptional.isPresent()) {
			Leave leave = leaveOptional.get();
			return ResponseEntity.ok().body(leave);
		}
		else {
			return ResponseEntity.ok().body("No data found for given UserName");
		}
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllUser()
	{
		List<Leave> leaveRequest = repository.findAll();
		
		if(leaveRequest.isEmpty())
		{
			ResponseEntity.ok().body("No Data found");
		}
		
		return ResponseEntity.ok().body(leaveRequest);
	}
	
	@PutMapping("/LeaveRequest/{id}")
	public ResponseEntity<?> UpdateRequestedLeave(@PathVariable String id, @RequestBody Leave leaveRequest){
		
		Optional<Leave> leave = repository.findById(id);
		if(leave.isPresent()) {
			Leave requestedLeave = leave.get();
			requestedLeave.setId(id);
			requestedLeave.setStatus(leaveRequest.getStatus());
			requestedLeave.setComments(leaveRequest.getComments());
			repository.save(requestedLeave);
			return ResponseEntity.ok().body(requestedLeave);
		}
		
		return ResponseEntity.ok().body(repository.save(leaveRequest));
	}
	
	@GetMapping("/getLeaveRequest/{startDate}/{endDate}")
	public ResponseEntity<?> RequestedLeavesBetween(@PathVariable String startDate, @PathVariable String endDate  )
	{
		List<Leave> leaveRequests= repository.findAllByStartDateBetween(startDate,endDate);
		
		return ResponseEntity.ok().body(leaveRequests);
	}
	
	public static String generateUID() {
		// Generate a random alphanumeric ID with 10 digits
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			int index = (int) (Math.random() * characters.length());
			sb.append(characters.charAt(index));
		}
		return sb.toString();
	}
	
}
