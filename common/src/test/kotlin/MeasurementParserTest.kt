import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MeasurementParserTest {
    @Test
    fun `parses valid temperature measurement`() {
        val result = MeasurementParser.parse("sensor_id=t1; value=30", SensorType.TEMPERATURE, "wh-1")
        assertEquals("t1", result.sensorId)
        assertEquals(30.0, result.value)
        assertEquals(SensorType.TEMPERATURE, result.type)
        assertEquals("wh-1", result.warehouseId)
    }

    @Test
    fun `parses valid humidity measurement`() {
        val result = MeasurementParser.parse("sensor_id=h1; value=40", SensorType.HUMIDITY, "wh-1")
        assertEquals("h1", result.sensorId)
        assertEquals(40.0, result.value)
        assertEquals(SensorType.HUMIDITY, result.type)
    }

    @Test
    fun `parses measurement without spaces around separators`() {
        val result = MeasurementParser.parse("sensor_id=t1;value=30", SensorType.TEMPERATURE, "wh-1")
        assertEquals("t1", result.sensorId)
        assertEquals(30.0, result.value)
    }

    @Test
    fun `fails on missing value field`() {
        assertFailsWith<IllegalArgumentException> {
            MeasurementParser.parse("sensor_id=t1", SensorType.TEMPERATURE, "wh-1")
        }
    }

    @Test
    fun `fails on non-numeric value`() {
        assertFailsWith<IllegalArgumentException> {
            MeasurementParser.parse("sensor_id=t1; value=abc", SensorType.TEMPERATURE, "wh-1")
        }
    }
    @Test
    fun `fails on empty input`() {
        assertFailsWith<IllegalArgumentException> {
            MeasurementParser.parse("", SensorType.TEMPERATURE, "wh-1")
        }
    }
}