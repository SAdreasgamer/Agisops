package com.aegisops.orchestrator.grpc;

import com.aegisops.proto.retrieval.QueryResponse;
import com.aegisops.proto.retrieval.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class PythonConnectivityTest {

    @Autowired
    private RetrievalClient retrievalClient;

    @BeforeEach
    public void assumeServiceRunning() {
        Assumptions.assumeTrue(isServiceAvailable("localhost", 50051),
                "Python gRPC Retrieval service is not running on port 50051. Skipping test.");
    }

    private boolean isServiceAvailable(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Test
    public void testCallActualPythonService() {
        System.out.println(">>> Sending request to Python retrieval service on localhost:50051...");
        QueryResponse response = retrievalClient.retrieveContext("database spike", 2, "tenant-pd-1");
        
        assertNotNull(response);
        System.out.println(">>> Received response from Python retrieval service!");
        for (Document doc : response.getDocumentsList()) {
            System.out.println(">>> Document ID: " + doc.getDocId());
            System.out.println(">>> Content: " + doc.getContent());
            System.out.println(">>> Score: " + doc.getScore());
        }
        assertTrue(response.getDocumentsCount() > 0);
    }
}
