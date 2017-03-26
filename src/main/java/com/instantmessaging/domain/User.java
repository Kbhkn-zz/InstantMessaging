package com.instantmessaging.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Kbhkn on 25.03.2017.
 */
@Entity
@Table(name = "tbl_user")
public class User implements Serializable {
    @Id
    @GenericGenerator(name = "user_id_generator", strategy = "increment")
    @GeneratedValue(generator = "user_id_generator")
    private long userId;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "public_key", unique = true)
    private long publicKey;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "senderUser", cascade = CascadeType.ALL)
    private List<MessageOfUsers> senderUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiverUser", cascade = CascadeType.ALL)
    private List<MessageOfUsers> receiverUser;

    public User(String phoneNumber, long publicKey) {
        this.phoneNumber = phoneNumber;
        this.publicKey = publicKey;
    }

    public User() {
    }

    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(long publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("userId=").append(userId);
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", publicKey='").append(publicKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
