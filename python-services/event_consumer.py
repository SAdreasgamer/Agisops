import asyncio
import logging
import json
from aiokafka import AIOKafkaConsumer

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("EventConsumer")

async def consume():
    logger.info("Initializing async Kafka event consumer...")
    consumer = AIOKafkaConsumer(
        'aegisops.events.raw',
        bootstrap_servers='localhost:9092',
        group_id="python-consumer-group",
        auto_offset_reset='earliest'
    )
    
    # Start the consumer
    await consumer.start()
    logger.info("Kafka consumer started successfully. Listening for events on 'aegisops.events.raw'...")
    try:
        async for msg in consumer:
            try:
                payload = json.loads(msg.value.decode('utf-8'))
                logger.info(f"Consumed event from Kafka: Topic={msg.topic}, Partition={msg.partition}, Offset={msg.offset}")
                logger.info(f"Parsed Event ID: {payload.get('eventId')}, Event Type: {payload.get('eventType')}, Source: {payload.get('source')}")
                logger.info(f"Full Payload: {payload}")
            except Exception as e:
                logger.error(f"Error parsing message value: {msg.value}. Error: {e}")
    finally:
        # Will leave consumer group; perform clean up
        await consumer.stop()
        logger.info("Kafka consumer stopped.")

if __name__ == "__main__":
    asyncio.run(consume())
