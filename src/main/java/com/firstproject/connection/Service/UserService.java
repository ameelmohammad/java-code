package com.firstproject.connection.Service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firstproject.connection.Entity.GroupMember;
import com.firstproject.connection.Entity.LoginRequest;
import com.firstproject.connection.Entity.ReferralRequest;
import com.firstproject.connection.Entity.User;
import com.firstproject.connection.Repository.GroupMemberRepository;
import com.firstproject.connection.Repository.LoginRequestRepository;
import com.firstproject.connection.Repository.ReferralRequestRepository;
import com.firstproject.connection.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	@Autowired
    private UserRepository userRepository;	
	
	@Autowired
	private LoginRequestRepository loginRequestRepository;
	
	@Autowired
	private ReferralRequestRepository referralRepo;
    
	@Autowired 
    private GroupMemberRepository groupMemberRepo;

	
	public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean mobileExists(String mobile) {
        return userRepository.findByMobile(mobile).isPresent();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByIdentifier(String identifier) {
        Optional<User> user = userRepository.findByEmail(identifier);
        if (user.isEmpty()) {
            user = userRepository.findByMobile(identifier);
        }
        return user;
    }
    
    public void logLoginAttempt(String identifier,String password) {
        LoginRequest request = new LoginRequest();
        request.setIdentifier(identifier);
        request.setPassword(password);
        loginRequestRepository.save(request); 
    }
    
//    public User registerUser(User user) {
//        userRepository.save(user);
//        List<ReferralRequest> referrals = referralRepo.findByEmailOrMobile(user.getEmail(), user.getMobile());
//        for (ReferralRequest r : referrals) {
//            GroupMember gm = new GroupMember();
//            gm.setGroup(r.getGroup());
//            gm.setUser(user);
//            if ("PENDING".equalsIgnoreCase(r.getStatus())) {
//                gm.setStatus("JOINED");
//            } else {
//                gm.setStatus("ACTIVE");
//            }
//            groupMemberRepo.save(gm);
////            gm.setStatus("ACTIVE");
//            groupMemberRepo.save(gm);
//
////            r.setStatus("JOINED");
//            referralRepo.save(r);
//            r.setRegistered(true);
//        }
//        return user;
//    }
    
//    @Transactional
//    public User registerUser(User user) {
//        // 1. Save the user to the user repository
//    	System.out.println("---------------");
//        
//        
//        String email = user.getEmail() != null ? user.getEmail().trim().toLowerCase() : null;
//        String mobile = user.getMobile() != null ? user.getMobile().trim() : null;
//
//        
//        
//        user.setEmail(email);
//        user.setMobile(mobile);
//        userRepository.save(user);
//        
//        List<ReferralRequest> referrals = referralRepo.findByEmailOrMobile(email, mobile);
//        System.out.println("Searching referrals for email: " + email + ", mobile: " + mobile);
//        System.out.println("Referrals size: " + referrals.size());
//
//        
//        // 2. Check if the user was invited (by email or mobile)
////        List<ReferralRequest> referrals = referralRepo.findByEmailOrMobile(user.getEmail(), user.getMobile());
//        
//        
//
//        for (ReferralRequest referral : referrals) {
//        	
//        	System.out.println("Referral found for group ID: " + referral.getGroup().getId());
//            // 3. Create a new group member entry
//            GroupMember groupMember = new GroupMember();
//            groupMember.setGroup(referral.getGroup());
//            groupMember.setUser(user);
//
//            // 4. Set status appropriately
//            groupMember.setStatus("JOINED"); // or use enum like GroupMemberStatus.JOINED
//
//            // 5. Save the group member
//            groupMemberRepo.save(groupMember);
//
//            // 6. Update referral status and flag
//            referral.setStatus("JOINED");
//            referral.setRegistered(true);
//            referralRepo.save(referral);
//            System.out.println("User registered: " + user.getEmail());
//            System.out.println("Referrals found: " + referrals.size());
//
//        }
//
//        return user;
//        
//    }
    
 // After user is successfully registered
//    public void postRegistrationLogic(User newUser) {
//        List<ReferralRequest> referrals = referralRepo.findByEmailOrMobile(newUser.getEmail(), newUser.getMobile());
//
//        for (ReferralRequest referral : referrals) {
//            referral.setRegistered(true);
//            referral.setStatus("ACCEPTED"); // Or another appropriate value
//            referralRepo.save(referral);
//
//            // Add to group as ACTIVE member
//            GroupMember gm = new GroupMember();
//            gm.setGroup(referral.getGroup());
//            gm.setUser(newUser);
//            gm.setStatus(GroupMemberStatus.ACTIVE);
//            groupMemberRepo.save(gm);
//        }
//    }
    
    @Transactional
    public User registerUser(User user) {
        System.out.println("----- Starting user registration -----");

        // Normalize email and mobile
        String email = user.getEmail() != null ? user.getEmail().trim().toLowerCase() : null;
        String mobile = user.getMobile() != null ? user.getMobile().trim() : null;

        user.setEmail(email);
        user.setMobile(mobile);

        // Save the user first
        userRepository.save(user);

        // Fetch referral requests matching email/mobile (case-insensitive)
        List<ReferralRequest> referrals = referralRepo.findByEmailOrMobile(email, mobile);
        System.out.println("Searching referrals for email: " + email + ", mobile: " + mobile);
        System.out.println("Referrals size: " + referrals.size());

        for (ReferralRequest referral : referrals) {
            if (referral.getGroup() != null) {
                System.out.println("Referral matched for group ID: " + referral.getGroup().getId());

                // Check if user is already a member of the group
                boolean exists = groupMemberRepo.existsByUserAndGroup(user, referral.getGroup());
                if (!exists) {
                    GroupMember groupMember = new GroupMember();
                    groupMember.setGroup(referral.getGroup());
                    groupMember.setUser(user);
                    groupMember.setStatus("JOINED"); // Or use enum if preferred
                    groupMemberRepo.save(groupMember);
                    System.out.println("Group member created.");
                } else {
                    System.out.println("User is already a group member. Skipping group creation.");
                }

                // Update referral status and flag
                referral.setStatus("JOINED");
                referral.setRegistered(true);
                referralRepo.save(referral);
                System.out.println("Referral updated to JOINED and registered=true.");
            } else {
                System.out.println("Referral group is null. Skipping this referral.");
            }
        }

        System.out.println("User registration complete for: " + user.getEmail());
        return user;
    }


}
