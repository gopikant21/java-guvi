package org.example.jpademo.service;

import org.example.jpademo.model.InvoiceDiscountingDeal;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface InvoiceDiscountingService {

    void save(InvoiceDiscountingDeal deal);

    InvoiceDiscountingDeal getById(int id);

    List<InvoiceDiscountingDeal> getAll();

    void update(int id, InvoiceDiscountingDeal deal);

    void delete(int id);

    InvoiceDiscountingDeal getByInvoiceNumber(String invoiceNumber);

    List<InvoiceDiscountingDeal> getByAnchorCorporate(String anchorCorporate);

    List<InvoiceDiscountingDeal> getBySellerName(String sellerName);

    List<InvoiceDiscountingDeal> getByChannel(String channel);

    List<InvoiceDiscountingDeal> getByStatus(String status);

    List<InvoiceDiscountingDeal> getHighYieldDeals(double minRate);

    List<InvoiceDiscountingDeal> getByTenureRange(int minTenure, int maxTenure);

    List<InvoiceDiscountingDeal> topRiskDeals(int limit);

    void fundDeal(int id, LocalDate fundedOn);

    void repayDeal(int id, LocalDate repaidOn);

    int markOverdueDeals(LocalDate referenceDate);

    double activePortfolioExposure();

    double averageDiscountRate();

    double expectedGrossYield();

    Map<String, Long> countByStatus();
}

