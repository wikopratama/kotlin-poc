object MeasurementParser {
    fun parse(raw: String, type: SensorType, warehouseId: String): SensorMeasurement {
        require(raw.isNotBlank()) { "Empty measurement" }

        val fields = raw.split(";")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .associate { field ->
                val parts = field.split("=", limit = 2).map { it.trim() }
                require(parts.size == 2) { "Malformed field: $field" }
                parts[0] to parts[1]
            }
        val sensorId = fields["sensor_id"] ?: throw IllegalArgumentException("Missing sensor_id field")
        val rawValue = fields["value"] ?: throw IllegalArgumentException("Missing value field")
        val value = rawValue.toDoubleOrNull() ?: throw IllegalArgumentException("Non-numeric value: $rawValue")

        return SensorMeasurement(
            warehouseId = warehouseId,
            sensorId = sensorId,
            type = type,
            value = value
        )
    }
}