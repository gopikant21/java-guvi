package org.example.jpademo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_discounting_deal")
public class InvoiceDiscountingDeal {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false, unique = true, length = 64)
    private String invoiceNumber;

    @Column(nullable = false, length = 120)
    private String anchorCorporate;

    @Column(nullable = false, length = 120)
    private String sellerName;

    @Column(nullable = false, length = 80)
    private String sector;

    @Column(nullable = false)
    private double invoiceAmount;

    @Column(nullable = false)
    private double discountRate;

    @Column(nullable = false)
    private int tenureDays;

    @Column(nullable = false)
    private double riskScore;

    @Column(nullable = false, length = 32)
    private String dealStatus;

    @Column(nullable = false)
    private double fundedAmount;

    @Column(nullable = false)
    private LocalDate expectedMaturityDate;

    private LocalDate fundedOn;

    private LocalDate repaidOn;

    @Column(nullable = false, length = 40)
    private String channel;

    @Column(nullable = false)
    private boolean delinquent;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public InvoiceDiscountingDeal() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getAnchorCorporate() {
        return anchorCorporate;
    }

    public void setAnchorCorporate(String anchorCorporate) {
        this.anchorCorporate = anchorCorporate;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getTenureDays() {
        return tenureDays;
    }

    public void setTenureDays(int tenureDays) {
        this.tenureDays = tenureDays;
    }

    public double getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(double riskScore) {
        this.riskScore = riskScore;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public double getFundedAmount() {
        return fundedAmount;
    }

    public void setFundedAmount(double fundedAmount) {
        this.fundedAmount = fundedAmount;
    }

    public LocalDate getExpectedMaturityDate() {
        return expectedMaturityDate;
    }

    public void setExpectedMaturityDate(LocalDate expectedMaturityDate) {
        this.expectedMaturityDate = expectedMaturityDate;
    }

    public LocalDate getFundedOn() {
        return fundedOn;
    }

    public void setFundedOn(LocalDate fundedOn) {
        this.fundedOn = fundedOn;
    }

    public LocalDate getRepaidOn() {
        return repaidOn;
    }

    public void setRepaidOn(LocalDate repaidOn) {
        this.repaidOn = repaidOn;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isDelinquent() {
        return delinquent;
    }

    public void setDelinquent(boolean delinquent) {
        this.delinquent = delinquent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

