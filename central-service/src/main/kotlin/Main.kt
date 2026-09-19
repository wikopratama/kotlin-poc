fun main() {
    val consumer = MeasurementConsumer(
        host = "localhost",
        exchangeName = "warehouse.measurements",
        thresholds = Thresholds()
    )
    consumer.start()
    println("Central monitoring service started. Waiting for measurements...")
    Thread.currentThread().join()
}
