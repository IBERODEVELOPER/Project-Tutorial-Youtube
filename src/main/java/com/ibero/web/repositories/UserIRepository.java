package com.ibero.web.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ibero.web.entities.UserEntity;

@Repository
public interface UserIRepository extends CrudRepository<UserEntity, Integer>, PagingAndSortingRepository<UserEntity, Integer>{

	public UserEntity findByUsername(String username);
	public UserEntity findByEmail(String email);
	
	@Modifying
    @Query("UPDATE UserEntity u SET u.otp = :otp WHERE u.id = :id")
    public int updateOtpById(@Param("id") Integer id, @Param("otp") String otp);
	
    public boolean existsByIdAndOtp(@Param("id") Integer id, @Param("otp") String otp);
    
    @Modifying
    @Query("UPDATE UserEntity u SET u.password = :password WHERE u.id = :id")
    public void updatePass(@Param("id") Integer id, @Param("password") String userPassword);

}
