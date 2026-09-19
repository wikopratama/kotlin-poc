data class WarehouseConfig(
    val warehouseId: String,
    val temperaturePort: Int = 3344,
    val humidityPort: Int = 3355,
    val rabbitHost: String = "localhost",
    val exchangeName: String = "warehouse.measurements"
)