package com.aegisops.orchestrator.grpc;

import com.aegisops.proto.retrieval.QueryRequest;
import com.aegisops.proto.retrieval.QueryResponse;
import com.aegisops.proto.retrieval.RetrievalServiceGrpc;
import io.grpc.Channel;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Service;

@Service
public class RetrievalClient {

    private final RetrievalServiceGrpc.RetrievalServiceBlockingStub blockingStub;

    public RetrievalClient(GrpcChannelFactory channelFactory) {
        Channel channel = channelFactory.createChannel("retrieval");
        this.blockingStub = RetrievalServiceGrpc.newBlockingStub(channel);
    }

    public QueryResponse retrieveContext(String query, int maxResults, String tenantId) {
        QueryRequest request = QueryRequest.newBuilder()
                .setQuery(query)
                .setMaxResults(maxResults)
                .setTenantId(tenantId)
                .build();
        
        return blockingStub.retrieveContext(request);
    }
}
