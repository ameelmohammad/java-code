package com.firstproject.connection.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firstproject.connection.Entity.GroupRequestDTO;
import com.firstproject.connection.Repository.UserRepository;
import com.firstproject.connection.Service.GroupService;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired 
    private GroupService groupService;
   
    @Autowired 
    private UserRepository userRepo;

//    @PostMapping("/create")
//    public ResponseEntity<?> createGroup(@RequestBody GroupCreationRequest request, @RequestParam Long ownerId) {
//        Optional<User> ownerOpt = userRepo.findById(ownerId);
//        if (ownerOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Owner not found");
//
//        Group group = groupService.createGroup(ownerOpt.get(), request);
//        return ResponseEntity.ok(group);
//    }
    
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody GroupRequestDTO request) {
        return groupService.createGroup(request);
    }
}
