package com.instantmessaging.dto;

public class UserMessageDto {
    private String senderPhone;
    private String receiverPhone;
    private String message;

    public UserMessageDto() {
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserMessageDto{");
        sb.append("senderPhone='").append(senderPhone).append('\'');
        sb.append(", receiverPhone='").append(receiverPhone).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
