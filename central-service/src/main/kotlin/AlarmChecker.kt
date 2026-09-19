object AlarmChecker {
    fun check(measurement: SensorMeasurement, thresholds: Thresholds): String? {
        val threshold = when (measurement.type) {
            SensorType.TEMPERATURE -> thresholds.temperature
            SensorType.HUMIDITY -> thresholds.humidity
        }
        if (measurement.value <= threshold) return null
        return "ALARM: ${measurement.type} threshold exceeded in warehouse ${measurement.warehouseId} " +
                "(sensor ${measurement.sensorId}): ${measurement.value} > $threshold"
    }
}