package allDemo.controller;

import allDemo.entity.KYCApplicant;
import allDemo.service.KYCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class KYCConsoleController {

    private KYCService service;

    @Autowired
    public KYCConsoleController(KYCService service) {
        this.service = service;
    }

    public void start() {

        KYCApplicant a1 = new KYCApplicant(
                1, "Amit Sharma", "Raj Sharma", "Sunita Sharma",
                "Male", LocalDate.of(1995, 5, 10),
                "111122223333", "ABCDE1234F", "P12345",
                "amit@gmail.com", "9876543210", "9123456780",
                "MG Road", "Delhi", "Delhi", "110001",
                "India", 1200000, "Salaried",
                "HDFC Bank", "PENDING", "MEDIUM"
        );

        a1.verifyAadhaar();
        a1.verifyPAN();
        a1.verifyEmail();
        a1.verifyPhone();

        service.register(a1);

        KYCApplicant a2 = new KYCApplicant(
                2, "Neha Verma", "Suresh Verma", "Anita Verma",
                "Female", LocalDate.of(1998, 8, 20),
                "444455556666", "PQRSX5678L", "P67890",
                "neha@gmail.com", "9999999999", "9000000000",
                "Andheri West", "Mumbai", "Maharashtra", "400053",
                "India", 400000, "Self-Employed",
                "Freelancer", "PENDING", "HIGH"
        );

        a2.verifyAadhaar();
        a2.verifyPAN();

        service.register(a2);

        System.out.println("\nALL APPLICANTS");
        service.getAll().forEach(System.out::println);

        System.out.println("\nAPPROVE KYC ID 1");
        service.approve(1);

        System.out.println(service.find(1));

        System.out.println("\nHIGH RISK APPLICANTS");
        service.riskWise("HIGH").forEach(System.out::println);

        System.out.println("\nTOTAL APPLICANTS: " + service.count());

        System.out.println("\nDELETE APPLICANT 2");
        service.remove(2);

        service.getAll().forEach(System.out::println);
    }
}
