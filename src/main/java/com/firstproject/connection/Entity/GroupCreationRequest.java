package com.firstproject.connection.Entity;

import java.util.List;

import com.firstproject.connection.Service.*;

import lombok.Data;

@Data
public class GroupCreationRequest {
	 private String groupName;
	    private List<ContactDTO> contacts;

}
