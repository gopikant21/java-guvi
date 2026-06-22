package org.example.jpademo.controller;

import org.example.jpademo.model.InvoiceDiscountingDeal;
import org.example.jpademo.service.InvoiceDiscountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoice-discounting")
public class InvoiceDiscountingRestController {

    @Autowired
    private InvoiceDiscountingService invoiceDiscountingService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody InvoiceDiscountingDeal deal) {
        invoiceDiscountingService.save(deal);
        return ResponseEntity.ok("Deal created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDiscountingDeal> getById(@PathVariable int id) {
        InvoiceDiscountingDeal deal = invoiceDiscountingService.getById(id);
        if (deal == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deal);
    }

    @GetMapping
    public List<InvoiceDiscountingDeal> getAll() {
        return invoiceDiscountingService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody InvoiceDiscountingDeal deal) {
        invoiceDiscountingService.update(id, deal);
        return ResponseEntity.ok("Deal updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        invoiceDiscountingService.delete(id);
        return ResponseEntity.ok("Deal deleted successfully");
    }

    @GetMapping("/invoice/{invoiceNumber}")
    public InvoiceDiscountingDeal getByInvoiceNumber(@PathVariable String invoiceNumber) {
        return invoiceDiscountingService.getByInvoiceNumber(invoiceNumber);
    }

    @GetMapping("/anchor/{anchorCorporate}")
    public List<InvoiceDiscountingDeal> getByAnchor(@PathVariable String anchorCorporate) {
        return invoiceDiscountingService.getByAnchorCorporate(anchorCorporate);
    }

    @GetMapping("/seller/{sellerName}")
    public List<InvoiceDiscountingDeal> getBySeller(@PathVariable String sellerName) {
        return invoiceDiscountingService.getBySellerName(sellerName);
    }

    @GetMapping("/channel/{channel}")
    public List<InvoiceDiscountingDeal> getByChannel(@PathVariable String channel) {
        return invoiceDiscountingService.getByChannel(channel);
    }

    @GetMapping("/status/{status}")
    public List<InvoiceDiscountingDeal> getByStatus(@PathVariable String status) {
        return invoiceDiscountingService.getByStatus(status);
    }

    @GetMapping("/high-yield")
    public List<InvoiceDiscountingDeal> getHighYield(@RequestParam double minRate) {
        return invoiceDiscountingService.getHighYieldDeals(minRate);
    }

    @GetMapping("/tenure")
    public List<InvoiceDiscountingDeal> getByTenureRange(@RequestParam int min, @RequestParam int max) {
        return invoiceDiscountingService.getByTenureRange(min, max);
    }

    @GetMapping("/risk/top")
    public List<InvoiceDiscountingDeal> topRiskDeals(@RequestParam(defaultValue = "5") int limit) {
        return invoiceDiscountingService.topRiskDeals(limit);
    }

    @PostMapping("/{id}/fund")
    public ResponseEntity<String> fundDeal(@PathVariable int id, @RequestParam String fundedOn) {
        invoiceDiscountingService.fundDeal(id, LocalDate.parse(fundedOn));
        return ResponseEntity.ok("Deal funded");
    }

    @PostMapping("/{id}/repay")
    public ResponseEntity<String> repayDeal(@PathVariable int id, @RequestParam String repaidOn) {
        invoiceDiscountingService.repayDeal(id, LocalDate.parse(repaidOn));
        return ResponseEntity.ok("Deal repaid");
    }

    @PostMapping("/mark-overdue")
    public ResponseEntity<String> markOverdue(@RequestParam String referenceDate) {
        int count = invoiceDiscountingService.markOverdueDeals(LocalDate.parse(referenceDate));
        return ResponseEntity.ok("Marked overdue deals: " + count);
    }

    @GetMapping("/analytics/exposure")
    public double activeExposure() {
        return invoiceDiscountingService.activePortfolioExposure();
    }

    @GetMapping("/analytics/avg-discount-rate")
    public double averageDiscountRate() {
        return invoiceDiscountingService.averageDiscountRate();
    }

    @GetMapping("/analytics/expected-gross-yield")
    public double expectedGrossYield() {
        return invoiceDiscountingService.expectedGrossYield();
    }

    @GetMapping("/analytics/count-by-status")
    public Map<String, Long> countByStatus() {
        return invoiceDiscountingService.countByStatus();
    }
}

