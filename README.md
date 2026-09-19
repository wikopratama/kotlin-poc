# kotlin-poc

Warehouse sensor monitoring service. Sensors will send readings through UDP to warehouse-service,
which will forwards them via RabbitMQ to central-service.
central-service will check the thresholds and logs an alert when conditions valid.

Modules:
- common: SensorMeasurement, SensorType, MeasurementParser
- warehouse-service: UDP listeners, forwards to RabbitMQ
- central-service: consumes from RabbitMQ, checks thresholds, logs alarms

Ports:
- 3344 temperature, e.g. sensor_id=t1; value=30, threshold 35
- 3355 humidity, e.g. sensor_id=h1; value=40, threshold 50
- 4344 temperature, e.g. sensor_id=t2; value=30, threshold 35
- 4355 humidity, e.g. sensor_id=h2; value=40, threshold 50

Tests: ./gradlew test

How to run it:

1. docker compose up -d
2. Terminal 1 — central-service
   ./gradlew :central-service:run
3. Terminal 2 — warehouse-service
   ./gradlew :warehouse-service:run
4. Terminal 3 — simulate sensors
   echo -n "sensor_id=t1; value=40" | nc -u -w0 localhost 3344
5. Terminal 4 — warehouse-service
   WAREHOUSE_ID=warehouse-2 TEMP_PORT=4344 HUMIDITY_PORT=4355 ./gradlew :warehouse-service:run

Step 4 should trigger an alarm in central-service's console since 40 > 35.
Try 3355 with a humidity reading under 50, it will forward it without alarm.

Note: Multiple warehouses:

WAREHOUSE_ID=warehouse-2 TEMP_PORT=4344 HUMIDITY_PORT=4355 ./gradlew :warehouse-service:run