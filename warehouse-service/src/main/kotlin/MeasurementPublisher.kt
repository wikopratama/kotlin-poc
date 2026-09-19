import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MeasurementPublisher(host: String, private val exchangeName: String) {
    private val channel: Channel = ConnectionFactory()
        .apply { this.host = host }
        .newConnection()
        .createChannel()
        .also { it.exchangeDeclare(exchangeName, "fanout") }

    fun publish(measurement: SensorMeasurement) {
        val json = Json.encodeToString(measurement)
        channel.basicPublish(exchangeName, "", null, json.toByteArray())
    }
}
