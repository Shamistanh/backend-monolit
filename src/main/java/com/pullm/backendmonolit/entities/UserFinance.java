package com.pullm.backendmonolit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_finance")
public class UserFinance {

    @Id
    private Long id;
    private BigDecimal balance;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpense;

    @MapsId
    @ToString.Exclude
    @JoinColumn(name = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

}
