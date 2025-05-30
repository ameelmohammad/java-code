package com.firstproject.connection.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.firstproject.connection.Entity.ReferralRequest;

@Repository
public interface ReferralRequestRepository extends JpaRepository<ReferralRequest, Long> {

	
//	@Query("SELECT r FROM ReferralRequest r WHERE r.email = :email OR r.mobile = :mobile")
//	List<ReferralRequest> findByEmailOrMobile(@Param("email") String email, @Param("mobile") String mobile);

	@Query("SELECT r FROM ReferralRequest r WHERE LOWER(TRIM(r.email)) = LOWER(TRIM(:email)) OR TRIM(r.mobile) = TRIM(:mobile)")
	List<ReferralRequest> findByEmailOrMobile(@Param("email") String email, @Param("mobile") String mobile);


}