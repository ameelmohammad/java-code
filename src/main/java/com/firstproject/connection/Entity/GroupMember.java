package com.firstproject.connection.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "group_member")
@Getter
@Setter
public class GroupMember {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "group_id")
	    private Group group;

	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user;

	    @Column(name = "status")
	    private String status;

	    // getters and setters

	    public Long getId() {
	        return id;
	    }

	    public Group getGroup() {
	        return group;
	    }

	    public void setGroup(Group group) {
	        this.group = group;
	    }

	    public User getUser() {
	        return user;
	    }

	    public void setUser(User user) {
	        this.user = user;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }
	}