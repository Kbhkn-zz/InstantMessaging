package com.instantmessaging.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tbl_message_of_users")
public class MessageOfUsers implements Serializable {
    @Id
    @GenericGenerator(name = "message_id_generator", strategy = "increment")
    @GeneratedValue(generator = "message_id_generator")
    private Long id;

    @Column(name = "sender_message")
    private String senderMessage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="sender_user_id")
    private User senderUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="receiver_user_id")
    private User receiverUser;

    public MessageOfUsers() {
    }

    public MessageOfUsers(User senderUser, User receiverUser, String senderMessage) {
        this.senderUser = senderUser;
        this.receiverUser = receiverUser;
        this.senderMessage = senderMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderMessage() {
        return senderMessage;
    }

    public void setSenderMessage(String senderMessage) {
        this.senderMessage = senderMessage;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public User getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(User receiverUser) {
        this.receiverUser = receiverUser;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MessageOfUsers{");
        sb.append("id=").append(id);
        sb.append(", senderMessage='").append(senderMessage).append('\'');
        sb.append(", senderUser=").append(senderUser.getPhoneNumber()+'\'');
        sb.append(", receiverUser=").append(receiverUser.getPhoneNumber()+'\'');
        sb.append('}');
        return sb.toString();
    }
}
