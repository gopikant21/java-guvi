package org.example.jpademo.service;

import org.example.jpademo.model.InvoiceDiscountingDeal;
import org.example.jpademo.repository.InvoiceDiscountingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InvoiceDiscountingServiceImpl implements InvoiceDiscountingService {

    @Autowired
    private InvoiceDiscountingRepository invoiceDiscountingRepository;

    @Override
    public void save(InvoiceDiscountingDeal deal) {
        LocalDateTime now = LocalDateTime.now();
        if (deal.getCreatedAt() == null) {
            deal.setCreatedAt(now);
        }
        deal.setUpdatedAt(now);

        if (deal.getFundedAmount() <= 0) {
            double fundedAmount = deal.getInvoiceAmount() - (deal.getInvoiceAmount() * deal.getDiscountRate() / 100.0);
            deal.setFundedAmount(fundedAmount);
        }

        if (deal.getDealStatus() == null || deal.getDealStatus().trim().isEmpty()) {
            deal.setDealStatus("CREATED");
        }

        invoiceDiscountingRepository.save(deal);
    }

    @Override
    public InvoiceDiscountingDeal getById(int id) {
        return invoiceDiscountingRepository.findById(id).orElse(null);
    }

    @Override
    public List<InvoiceDiscountingDeal> getAll() {
        return invoiceDiscountingRepository.findAll();
    }

    @Override
    public void update(int id, InvoiceDiscountingDeal deal) {
        deal.setId(id);
        deal.setUpdatedAt(LocalDateTime.now());
        invoiceDiscountingRepository.save(deal);
    }

    @Override
    public void delete(int id) {
        invoiceDiscountingRepository.deleteById(id);
    }

    @Override
    public InvoiceDiscountingDeal getByInvoiceNumber(String invoiceNumber) {
        return invoiceDiscountingRepository.getByInvoiceNumber(invoiceNumber);
    }

    @Override
    public List<InvoiceDiscountingDeal> getByAnchorCorporate(String anchorCorporate) {
        return invoiceDiscountingRepository.getByAnchorCorporate(anchorCorporate);
    }

    @Override
    public List<InvoiceDiscountingDeal> getBySellerName(String sellerName) {
        return invoiceDiscountingRepository.getBySellerName(sellerName);
    }

    @Override
    public List<InvoiceDiscountingDeal> getByChannel(String channel) {
        return invoiceDiscountingRepository.getByChannel(channel);
    }

    @Override
    public List<InvoiceDiscountingDeal> getByStatus(String status) {
        return invoiceDiscountingRepository.getByDealStatus(status);
    }

    @Override
    public List<InvoiceDiscountingDeal> getHighYieldDeals(double minRate) {
        return invoiceDiscountingRepository.getHighYieldDeals(minRate);
    }

    @Override
    public List<InvoiceDiscountingDeal> getByTenureRange(int minTenure, int maxTenure) {
        return invoiceDiscountingRepository.getByTenureRange(minTenure, maxTenure);
    }

    @Override
    public List<InvoiceDiscountingDeal> topRiskDeals(int limit) {
        return invoiceDiscountingRepository.sortByRiskScoreDesc().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public void fundDeal(int id, LocalDate fundedOn) {
        InvoiceDiscountingDeal deal = getById(id);
        if (deal == null) {
            return;
        }

        deal.setFundedOn(fundedOn);
        deal.setDealStatus("FUNDED");
        deal.setDelinquent(false);
        deal.setUpdatedAt(LocalDateTime.now());
        invoiceDiscountingRepository.save(deal);
    }

    @Override
    public void repayDeal(int id, LocalDate repaidOn) {
        InvoiceDiscountingDeal deal = getById(id);
        if (deal == null) {
            return;
        }

        deal.setRepaidOn(repaidOn);
        deal.setDealStatus("REPAID");
        deal.setDelinquent(false);
        deal.setUpdatedAt(LocalDateTime.now());
        invoiceDiscountingRepository.save(deal);
    }

    @Override
    public int markOverdueDeals(LocalDate referenceDate) {
        List<InvoiceDiscountingDeal> overdue = invoiceDiscountingRepository.findPotentialOverdueDeals(referenceDate);
        for (InvoiceDiscountingDeal deal : overdue) {
            deal.setDealStatus("OVERDUE");
            deal.setDelinquent(true);
            deal.setUpdatedAt(LocalDateTime.now());
        }
        invoiceDiscountingRepository.saveAll(overdue);
        return overdue.size();
    }

    @Override
    public double activePortfolioExposure() {
        return invoiceDiscountingRepository.activePortfolioExposure();
    }

    @Override
    public double averageDiscountRate() {
        return invoiceDiscountingRepository.averageDiscountRate();
    }

    @Override
    public double expectedGrossYield() {
        return getAll().stream()
                .filter(d -> "FUNDED".equalsIgnoreCase(d.getDealStatus()) || "OVERDUE".equalsIgnoreCase(d.getDealStatus()))
                .mapToDouble(d -> d.getInvoiceAmount() - d.getFundedAmount())
                .sum();
    }

    @Override
    public Map<String, Long> countByStatus() {
        return getAll().stream()
                .collect(Collectors.groupingBy(InvoiceDiscountingDeal::getDealStatus, Collectors.counting()));
    }
}

