package org.example.jpademo.repository;

import org.example.jpademo.model.InvoiceDiscountingDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceDiscountingRepository extends JpaRepository<InvoiceDiscountingDeal, Integer> {

    InvoiceDiscountingDeal getByInvoiceNumber(String invoiceNumber);

    List<InvoiceDiscountingDeal> getByAnchorCorporate(String anchorCorporate);

    List<InvoiceDiscountingDeal> getBySellerName(String sellerName);

    List<InvoiceDiscountingDeal> getByChannel(String channel);

    List<InvoiceDiscountingDeal> getByDealStatus(String dealStatus);

    @Query("SELECT d FROM InvoiceDiscountingDeal d WHERE d.discountRate >= :minRate")
    List<InvoiceDiscountingDeal> getHighYieldDeals(@Param("minRate") double minRate);

    @Query("SELECT d FROM InvoiceDiscountingDeal d WHERE d.tenureDays BETWEEN :minTenure AND :maxTenure")
    List<InvoiceDiscountingDeal> getByTenureRange(@Param("minTenure") int minTenure, @Param("maxTenure") int maxTenure);

    @Query("SELECT d FROM InvoiceDiscountingDeal d WHERE d.expectedMaturityDate < :referenceDate AND d.dealStatus = 'FUNDED'")
    List<InvoiceDiscountingDeal> findPotentialOverdueDeals(@Param("referenceDate") LocalDate referenceDate);

    @Query("SELECT d FROM InvoiceDiscountingDeal d ORDER BY d.riskScore DESC")
    List<InvoiceDiscountingDeal> sortByRiskScoreDesc();

    @Query("SELECT d FROM InvoiceDiscountingDeal d ORDER BY d.expectedMaturityDate ASC")
    List<InvoiceDiscountingDeal> sortByMaturityDateAsc();

    @Query("SELECT COALESCE(SUM(d.fundedAmount), 0) FROM InvoiceDiscountingDeal d WHERE d.dealStatus IN ('FUNDED', 'OVERDUE')")
    double activePortfolioExposure();

    @Query("SELECT COALESCE(AVG(d.discountRate), 0) FROM InvoiceDiscountingDeal d")
    double averageDiscountRate();
}

