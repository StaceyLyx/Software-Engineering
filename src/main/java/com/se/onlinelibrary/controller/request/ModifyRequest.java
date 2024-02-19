package com.se.onlinelibrary.controller.request;

import lombok.Data;

@Data
public class ModifyRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}
