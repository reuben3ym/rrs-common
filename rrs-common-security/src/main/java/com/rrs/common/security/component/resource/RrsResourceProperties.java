package com.rrs.common.security.component.resource;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RrsResourceProperties {
    private List<AuthorizeRequest> authorizeRequests =new ArrayList<>();
    private List<String> addFilterBeforeUsernamePasswordAuthenticationFilter;
    private List<String> ignoreUrls = new ArrayList<>();
    private Boolean inner = false;
}
