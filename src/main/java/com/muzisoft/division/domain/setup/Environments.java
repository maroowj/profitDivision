package com.muzisoft.division.domain.setup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table
public class Environments {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long seq;

    @Column(nullable = false)
    private int duesDate;

    @Column(nullable = false)
    private int duesAmount;

    @Column(nullable = false)
    private float interestRate;

    @Column(nullable = false)
    private int conversionRate;

    @Column(nullable = false)
    private int recommenderDividendRate;

    @Column
    private int cuttingRate;

    @Column
    private long totalBalance;

    public static Environments create(int duesDate, int duesAmount, float interestRate, int conversionRate, int recommenderDividendRate, int cuttingRate, long totalBalance) {
        Environments environments = new Environments();

        environments.setDuesDate(duesDate);
        environments.setDuesAmount(duesAmount);
        environments.setInterestRate(interestRate);
        environments.setConversionRate(conversionRate);
        environments.setRecommenderDividendRate(recommenderDividendRate);
        environments.setCuttingRate(cuttingRate);
        environments.setTotalBalance(totalBalance);
        return environments;
    }


    public void updateTotalBalance(long totalBalance) {
        setTotalBalance(totalBalance);
    }

    public void updateInterestRate(float interestRate) {
        setInterestRate(interestRate);
    }

    public void updateRecommenderDividendRate(int recommenderDividendRate) {
        setRecommenderDividendRate(recommenderDividendRate);
    }

    public void updateConversionRate(int conversionRate) {
        setConversionRate(conversionRate);
    }
}
