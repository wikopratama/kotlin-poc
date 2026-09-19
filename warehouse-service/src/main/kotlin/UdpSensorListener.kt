import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketTimeoutException

class UdpSensorListener(private val port: Int, val type: SensorType, private val warehouseId: String) {
    fun listen(): Flow<SensorMeasurement> = flow {
        val socket = DatagramSocket(port)
        socket.soTimeout = 500
        val buffer = ByteArray(1024)
        try {
            while (currentCoroutineContext().isActive) {
                val packet = DatagramPacket(buffer, buffer.size)
                try {
                    socket.receive(packet)
                } catch (e: SocketTimeoutException) {
                    continue
                }
                val raw = String(packet.data, 0, packet.length)
                try {
                    emit(MeasurementParser.parse(raw, type, warehouseId))
                } catch (e: IllegalArgumentException) {
                }
            }
        } finally {
            socket.close()
        }
    }.flowOn(Dispatchers.IO)
}
