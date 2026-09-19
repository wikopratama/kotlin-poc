import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.test.Test
import kotlin.test.assertEquals

class UdpSensorListenerTest {
    @Test
    fun `emits parsed measurement for a received packet`() = runBlocking {
        val port = 34567
        val listener = UdpSensorListener(port, SensorType.TEMPERATURE, "wh-1")
        val flow = listener.listen()

        val sendSocket = DatagramSocket()
        val message = "sensor_id=t1; value=40".toByteArray()

        var result: SensorMeasurement? = null
        val collectJob = GlobalScope.launch {
            withTimeout(5000) {
                result = flow.first()
            }
        }
        delay(200)
        sendSocket.send(DatagramPacket(message, message.size, InetAddress.getByName("localhost"), port))
        collectJob.join()
        sendSocket.close()

        assertEquals("t1", result?.sensorId)
        assertEquals(40.0, result?.value)
    }
}