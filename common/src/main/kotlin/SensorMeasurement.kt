import kotlinx.serialization.Serializable

@Serializable
data class SensorMeasurement(
    val warehouseId: String,
    val sensorId: String,
    val type: SensorType,
    val value: Double
)
