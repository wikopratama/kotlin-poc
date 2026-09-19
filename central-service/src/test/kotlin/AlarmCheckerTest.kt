import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AlarmCheckerTest {
    private val thresholds = Thresholds(temperature = 35.0, humidity = 50.0)

    @Test
    fun `no alarm when temperature is under threshold`() {
        val measurement = SensorMeasurement("wh-1", "t1", SensorType.TEMPERATURE, 30.0)
        assertNull(AlarmChecker.check(measurement, thresholds))
    }

    @Test
    fun `alarm when temperature exceeds threshold`() {
        val measurement = SensorMeasurement("wh-1", "t1", SensorType.TEMPERATURE, 40.0)
        assertTrue(AlarmChecker.check(measurement, thresholds)!!.contains("ALARM"))

    }@Test
    fun `alarm when humidity exceeds threshold`() {
        val measurement = SensorMeasurement("wh-1", "h1", SensorType.HUMIDITY, 60.0)
        assertTrue(AlarmChecker.check(measurement, thresholds)!!.contains("ALARM"))
    }
}