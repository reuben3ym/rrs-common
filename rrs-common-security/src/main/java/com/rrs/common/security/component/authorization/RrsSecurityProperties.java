package com.rrs.common.security.component.authorization;

import com.rrs.common.security.component.resource.AuthorizeRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RrsSecurityProperties {
    private List<AuthorizeRequest> authorizeRequests = new ArrayList<>();
}
