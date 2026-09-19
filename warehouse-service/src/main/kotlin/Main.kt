import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val config = WarehouseConfig(
        warehouseId = System.getenv("WAREHOUSE_ID") ?: "warehouse-1",
        temperaturePort = System.getenv("TEMP_PORT")?.toInt() ?: 3344,
        humidityPort = System.getenv("HUMIDITY_PORT")?.toInt() ?: 3355
    )
    val publisher = MeasurementPublisher(config.rabbitHost, config.exchangeName)
    val temperature = UdpSensorListener(config.temperaturePort, SensorType.TEMPERATURE, config.warehouseId).listen()
    val humidity = UdpSensorListener(config.humidityPort, SensorType.HUMIDITY, config.warehouseId).listen()

    println("Warehouse service '${config.warehouseId}' listening on UDP ${config.temperaturePort}/${config.humidityPort}")

    merge(temperature, humidity).collect { measurement ->
        println("Forwarding: $measurement")
        publisher.publish(measurement)
    }
}