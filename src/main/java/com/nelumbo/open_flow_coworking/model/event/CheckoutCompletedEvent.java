package com.nelumbo.open_flow_coworking.model.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class CheckoutCompletedEvent extends ApplicationEvent {

    private final UUID clientId;
    private final UUID branchId;

    public CheckoutCompletedEvent(Object source, UUID clientId, UUID branchId) {
        super(source);
        this.clientId = clientId;
        this.branchId = branchId;
    }
}
