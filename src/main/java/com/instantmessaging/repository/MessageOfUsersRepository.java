package com.instantmessaging.repository;

import com.instantmessaging.domain.MessageOfUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageOfUsersRepository extends JpaRepository<MessageOfUsers, Long> {
    @Query("Select m from MessageOfUsers m where m.receiverUser.phoneNumber = :receiverPhoneNo")
    Optional<MessageOfUsers> getMessageByReceiverPhoneNo(@Param("receiverPhoneNo") String receiverPhoneNo);

    @Query("Select m from MessageOfUsers m where m.senderUser.phoneNumber = :no1 and m.receiverUser.phoneNumber = :no2")
    Optional<MessageOfUsers> getMessageBySenderUserPhoneAndReceiverUserPhone(@Param("no1") String no1, @Param("no2") String no2);

}
