import asyncio
import logging
import os
import sys

import grpc

# Add gen directory to path
sys.path.append(os.path.join(os.path.dirname(__file__), 'gen'))

from aegisops import retrieval_pb2, retrieval_pb2_grpc

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("RetrievalService")

class RetrievalService(retrieval_pb2_grpc.RetrievalServiceServicer):
    async def RetrieveContext(self, request, context):
        logger.info(
            f"Received RetrieveContext request: query='{request.query}', "
            f"max_results={request.max_results}, tenant_id='{request.tenant_id}'"
        )
        
        # Mock documents returned
        docs = [
            retrieval_pb2.Document(
                doc_id="doc-001",
                content=f"Matching documentation snippet for query: {request.query}",
                score=0.95
            ),
            retrieval_pb2.Document(
                doc_id="doc-002",
                content=(
                    "AEGISOPS standard operating procedure for Database CPU spikes: "
                    "verify connection pool size and query execution plans."
                ),
                score=0.88
            )
        ]
        
        return retrieval_pb2.QueryResponse(documents=docs[:request.max_results] if request.max_results > 0 else docs)

async def serve():
    server = grpc.aio.server()
    retrieval_pb2_grpc.add_RetrievalServiceServicer_to_server(RetrievalService(), server)
    listen_addr = "[::]:50051"
    server.add_insecure_port(listen_addr)
    logger.info(f"Starting async gRPC Retrieval server on {listen_addr}...")
    await server.start()
    await server.wait_for_termination()

if __name__ == "__main__":
    asyncio.run(serve())
