package com.firstproject.connection.Entity;

import java.util.List;

import lombok.Data;

@Data
public class GroupRequestDTO {
	 private Long ownerId;
	    private String groupName;
	    private List<MemberDTO> members;

}
