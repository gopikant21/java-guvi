package composition;

import java.util.ArrayList;
import java.util.List;

// Document class
class Document {
    String docName;

    public Document(String docName) {
        this.docName = docName;
    }
}

// Loan class (Composition)
class Loan {
    String loanId;
    List<Document> documents;

    public Loan(String loanId) {
        this.loanId = loanId;
        this.documents = new ArrayList<>(); // created inside → composition
    }

    public void addDocument(String docName) {
        documents.add(new Document(docName));
    }

    public void displayDocuments() {
        System.out.println("Loan ID: " + loanId);
        for (Document doc : documents) {
            System.out.println("Document: " + doc.docName);
        }
    }
}

// Main
public class CompositionDemo {
    public static void main(String[] args) {
        Loan loan = new Loan("LN1001");

        loan.addDocument("Aadhar");
        loan.addDocument("PAN");
        loan.addDocument("Agreement");

        loan.displayDocuments();
    }
}
