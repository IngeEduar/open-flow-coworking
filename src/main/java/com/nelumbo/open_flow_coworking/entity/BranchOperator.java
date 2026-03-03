package com.nelumbo.open_flow_coworking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "branch_operator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchOperator extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backoffice_user_id", nullable = false)
    private User user;
}
