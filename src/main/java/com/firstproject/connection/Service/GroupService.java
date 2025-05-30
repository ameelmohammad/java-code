package com.firstproject.connection.Service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.firstproject.connection.Entity.Group;
import com.firstproject.connection.Entity.GroupMember;
import com.firstproject.connection.Entity.GroupMemberStatus;
import com.firstproject.connection.Entity.GroupRequestDTO;
import com.firstproject.connection.Entity.MemberDTO;
import com.firstproject.connection.Entity.ReferralRequest;
import com.firstproject.connection.Entity.User;
import com.firstproject.connection.Repository.GroupMemberRepository;
import com.firstproject.connection.Repository.GroupRepository;
import com.firstproject.connection.Repository.ReferralRequestRepository;
import com.firstproject.connection.Repository.UserRepository;


@Service
public class GroupService {
	
	    @Autowired 
	    private GroupRepository groupRepo;
	    @Autowired 
	    private GroupMemberRepository groupMemberRepo;
	    @Autowired
	    private ReferralRequestRepository referralRepo;
	    @Autowired
	    private UserRepository userRepo;
	    
	    
//	    public ResponseEntity<?> createGroup(GroupRequestDTO request) {
//	    	System.out.println("----------------------------");
//	        if (request.getOwnerId() == null) {
//	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Owner ID must not be null");
//	        }
//
//	        Optional<User> ownerOpt = userRepo.findById(request.getOwnerId());
//	        if (ownerOpt.isEmpty()) {
//	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid owner ID");
//	        }
//
//	        User owner = ownerOpt.get();
//
//	        Group group = new Group();
//	        group.setName(request.getGroupName());
//	        group.setOwner(owner);
//	        group.setCreatedBy(owner);
//	        groupRepo.save(group);
//
//	        // Handle members (status is not expected from request anymore)
//	        if (request.getMembers() != null) {
////	            for (MemberDTO member : request.getMembers()) {
////	                Optional<User> userOpt = userRepo.findByEmailOrMobile(member.getEmail(), member.getMobile());
////
////	                if (userOpt.isPresent()) {
////	                    // Already registered - backend sets status to ACTIVE
////	                    GroupMember gm = new GroupMember();
////	                    gm.setGroup(group);
////	                    gm.setUser(userOpt.get());
////	                    gm.setStatus(GroupMemberStatus.ACTIVE); // Automatically assigned
////	                    groupMemberRepo.save(gm);
////	                } else {
////	                    // Not registered yet - create referral with status INVITED
////	                    ReferralRequest referral = new ReferralRequest();
////	                    referral.setGroup(group);
////	                    referral.setEmail(member.getEmail());
////	                    referral.setMobile(member.getMobile());
////	                    referral.setInvitedName(member.getName());
////	                    referral.setStatus("INVITED"); // Backend assigns
////	                    referral.setRegistered(false);
////	                    referralRepo.save(referral);
////	                    System.out.println("Referral saved for: " + referral.getEmail() + ", group: " + group.getName());
////
////	                }
////	            }
////	        }
////
////	        return ResponseEntity.ok("Group created successfully.");
////	    }
//	        	for (MemberDTO member : request.getMembers()) {
//	        	    Optional<User> userByEmail = Optional.empty();
//	        	    Optional<User> userByMobile = Optional.empty();
//
//	        	    String email = member.getEmail() != null ? member.getEmail().trim().toLowerCase() : null;
//	        	    String mobile = member.getMobile() != null ? member.getMobile().trim() : null;
//
//	        	    if (email != null && !email.isBlank()) {
//	        	        userByEmail = userRepo.findByEmail(email);
//	        	    }
//
//	        	    if (mobile != null && !mobile.isBlank()) {
//	        	        userByMobile = userRepo.findByMobile(mobile);
//	        	    }
//
//	        	    if (userByEmail.isEmpty() && userByMobile.isEmpty()) {
//	        	        // User does NOT exist => Save referral
//	        	        ReferralRequest referral = new ReferralRequest();
//	        	        referral.setGroup(group);
//	        	        referral.setEmail(email);
//	        	        referral.setMobile(mobile);
//	        	        referral.setInvitedName(member.getName());
//	        	        referral.setStatus("INVITED");
//	        	        referral.setRegistered(false);
//	        	        referralRepo.save(referral);
//	        	        System.out.println("Referral saved for: " + email + ", group: " + group.getName());
//	        	    } else {
//	        	        // User exists => Add to group as ACTIVE
//	        	        User existingUser = userByEmail.orElse(userByMobile.get());
//	        	        GroupMember gm = new GroupMember();
//	        	        gm.setGroup(group);
//	        	        gm.setUser(existingUser);
//	        	        gm.setStatus(GroupMemberStatus.ACTIVE);
//	        	        groupMemberRepo.save(gm);
//	        	    }
//	        	}
//	        	 
//
//	        }
//
//	        return ResponseEntity.ok("Group created successfully.");
//	    
//}
	    
	    public ResponseEntity<?> createGroup(GroupRequestDTO request) {

	        if (request.getOwnerId() == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Owner ID must not be null");
	        }

	        Optional<User> ownerOpt = userRepo.findById(request.getOwnerId());
	        if (ownerOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid owner ID");
	        }

	        User owner = ownerOpt.get();

	        Group group = new Group();
	        group.setName(request.getGroupName());
	        group.setOwner(owner);
	        group.setCreatedBy(owner);
	        groupRepo.save(group);

	        if (request.getMembers() != null) {
	            for (MemberDTO member : request.getMembers()) {
	                Optional<User> userByEmail = Optional.empty();
	                Optional<User> userByMobile = Optional.empty();

	                String email = member.getEmail() != null ? member.getEmail().trim().toLowerCase() : null;
	                String mobile = member.getMobile() != null ? member.getMobile().trim() : null;

	                if (email != null && !email.isBlank()) {
	                    userByEmail = userRepo.findByEmail(email);
	                }

	                if (mobile != null && !mobile.isBlank()) {
	                    userByMobile = userRepo.findByMobile(mobile);
	                }

	                if (userByEmail.isEmpty() && userByMobile.isEmpty()) {
	                    // User does NOT exist => Save referral
	                    ReferralRequest referral = new ReferralRequest();
	                    referral.setGroup(group);
	                    referral.setEmail(email);
	                    referral.setMobile(mobile);
	                    referral.setInvitedName(member.getName());
	                    referral.setStatus("INVITED");
	                    referral.setRegistered(false);
	                    referralRepo.save(referral);
	                    System.out.println("Referral saved for: " + email + ", group: " + group.getName());
	                } else {
	                    // User exists => Add to group as ACTIVE
	                    User existingUser = userByEmail.orElse(userByMobile.get());
	                    GroupMember gm = new GroupMember();
	                    gm.setGroup(group);
	                    gm.setUser(existingUser);
	                    gm.setStatus(GroupMemberStatus.ACTIVE);
	                    groupMemberRepo.save(gm);
	                }
	            }
	        }

	        return ResponseEntity.ok("Group created successfully.");
	    }
}


