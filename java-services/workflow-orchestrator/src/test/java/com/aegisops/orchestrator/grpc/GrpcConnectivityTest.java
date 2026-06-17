package com.aegisops.orchestrator.grpc;

import com.aegisops.proto.retrieval.QueryRequest;
import com.aegisops.proto.retrieval.QueryResponse;
import com.aegisops.proto.retrieval.Document;
import com.aegisops.proto.retrieval.RetrievalServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class GrpcConnectivityTest {

    @Autowired
    private RetrievalClient retrievalClient;

    private Server grpcServer;

    @BeforeEach
    public void startMockServer() throws IOException {
        // Start a mock gRPC server on port 50051
        grpcServer = ServerBuilder.forPort(50051)
                .addService(new RetrievalServiceGrpc.RetrievalServiceImplBase() {
                    @Override
                    public void retrieveContext(QueryRequest request, StreamObserver<QueryResponse> responseObserver) {
                        QueryResponse response = QueryResponse.newBuilder()
                                .addDocuments(Document.newBuilder()
                                        .setDocId("doc-test-123")
                                        .setContent("Test document content from Java Mock Server")
                                        .setScore(0.99f)
                                        .build())
                                .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }
                })
                .build()
                .start();
    }

    @AfterEach
    public void stopMockServer() {
        if (grpcServer != null) {
            grpcServer.shutdownNow();
        }
    }

    @Test
    public void testGrpcCallToMockServer() {
        QueryResponse response = retrievalClient.retrieveContext("test query", 1, "tenant-999");
        assertNotNull(response);
        assertEquals(1, response.getDocumentsCount());
        Document doc = response.getDocuments(0);
        assertEquals("doc-test-123", doc.getDocId());
        assertEquals("Test document content from Java Mock Server", doc.getContent());
        assertEquals(0.99f, doc.getScore(), 0.001);
    }
}
